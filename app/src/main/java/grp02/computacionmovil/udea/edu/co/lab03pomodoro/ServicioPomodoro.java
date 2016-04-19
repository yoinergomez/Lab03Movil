package grp02.computacionmovil.udea.edu.co.lab03pomodoro;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Pc1 on 14/04/2016.
 */
public class ServicioPomodoro extends Service {


    private int intervaloPomodoro;
    private int intervaloDescansoLargo;
    private int intervaloDescansoCorto;
    private PendingIntent pendingIntentAlarma;
    private int numeroNotificaciones;
    private int numeroPomodoros;
    private AlarmManager alarmManager;
    private Calendar calendar;
    private Notification notification;
    private PreferenciaCompartida preferencia;
    private Chronometer tiempo;

    /**
     * Se ejecuta solo una vez, eso significa que se ejecuta un solo servicio durante la ejecución
     * del pomodoro.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        numeroNotificaciones=0;
        numeroPomodoros=0;
        intervaloDescansoCorto=0;
        intervaloPomodoro=0;
        intervaloDescansoLargo=0;
        preferencia = new PreferenciaCompartida();
    }

    /**
     * Se ejecuta cada vez que una clase externa crea un intento y llama al método startService()
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
         Se captura la acción cuando el intento es mandado por el metodo iniciarServicioPomodoro()
         del MainActvity
          */
        if(intent.getAction().equals("intentIniciar")){
            //Capturo los parametros mandados por el intent
            preferencia.setEstadoServicio(this,true);
            Log.d("@@@", "Estado: "+preferencia.getEstadoServicio(this));
            intervaloPomodoro=intent.getExtras().getInt("intervaloPomodoro");
            intervaloDescansoCorto=intent.getExtras().getInt("intervaloDescansoCorto");
            intervaloDescansoLargo=intent.getExtras().getInt("intervaloDescansoLargo");

            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentMain, 0);

            //crecion de la notificacion en la barra de android
            notification = new NotificationCompat.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("Pomodoro")
                    .setContentText("Realizando tarea")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pIntent).build();


            iniciarPomodoro();
            /*
            llamar a este método permite que el servicio creado se ejecute en segundoplano y funcione
            con normalidad a pesar de que la app no sea visible o se haya cerrado, sino se
            utiliza este método cuando la app se cierra se destruye el servicio creado y el sistema
            crea uno nuevo
             */
            startForeground(1,notification);
            /*
         Se captura la acción cuando el intento es mandado por el metodo pararServicioPomodoro()
         del MainActvity
          */
        }else if(intent.getAction().equals("intentParar")){
            //se para la ejecucuón a segundo plano y se llama al onDestroy()
            preferencia.setEstadoServicio(this,false);
            preferencia.setEstado(this,0);
            Log.d("@@@", "Estado: "+preferencia.getEstadoServicio(this));
            if(existeAlarma()) {
                Toast.makeText(this, "Pomodoro cancelado", Toast.LENGTH_SHORT).show();
                stopForeground(true);
                stopSelf();
            }
                /*
         Se captura la acción cuando el intento es mandado por el AlarmaReceiver, es decir, se captura
         el intento cuando suena la alarma
          */
        }else if(intent.getAction().equals("intentAlarmaReceiver")){
            analizarEstadoPomodoro();
        }


        return START_STICKY;
    }


    /**
     * Inicia la alarma
     */
    public void iniciarPomodoro(){
        calendar=Calendar.getInstance();
        Intent intent = new Intent(getBaseContext(), AlarmaReceiver.class);
        pendingIntentAlarma = PendingIntent.getBroadcast(getBaseContext(), 1 , intent, 0);
        Toast.makeText(this.getBaseContext(), "Inicia pomodoro", Toast.LENGTH_SHORT).show();
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervaloPomodoro, pendingIntentAlarma);

        /*
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervaloPomodoro, pendingIntentAlarma);
        */
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP,intervaloPomodoro, pendingIntentAlarma);

    }


    /**
     * Ajusta la alarma para un nuevo pomodoro después de que ha pasado un tiempo de descanso
     */
    private void restaurarPomodoro(){
        cancelarPomodoro();
        calendar=Calendar.getInstance();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervaloPomodoro, pendingIntentAlarma);
    }


    /**
     * Ajusta la alarma para inciar un descanso corto o largo luego de haber transcurrido un pomodoro
     * @param intervaloDescanso
     */
    private void pausarPomodoro(int intervaloDescanso){
        cancelarPomodoro();
        calendar=Calendar.getInstance();
        int intervalo=intervaloDescanso;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervalo, pendingIntentAlarma);
    }


    /**
     * Analiza cúal es la acción que se debe realizar despúes de que la alarma ha sonado,
     * es decir, de acuerdo al número de veces que ha sonado la alarma durante la ejecución del servicio
     * o se inicia un nuevo pomodoro, o se inicia un periodo de descanso corto o se inicia un
     * periodo de descanso largo
     */
    public void analizarEstadoPomodoro(){
        numeroNotificaciones=numeroNotificaciones+1;
        Log.d("@@", "Numero notificaciones: "+ numeroNotificaciones);
        if(numeroNotificaciones % 4==2){
            numeroPomodoros=numeroPomodoros+1;
            Toast.makeText(this.getBaseContext(), "Inicia descanso", Toast.LENGTH_SHORT).show();
            if(numeroPomodoros==4){
                Log.d("@@", "Intervalo descanso: LARGO");
                preferencia.setEstado(this,2);
                notification = new NotificationCompat.Builder(this)
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setContentTitle("Pomodoro")
                        .setContentText("Pausa larga")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setVisibility(Notification.VISIBILITY_PUBLIC).build();
                pausarPomodoro(intervaloDescansoLargo);
            }else{
                Log.d("@@", "Intervalo descanso: CORTO");
                preferencia.setEstado(this, 1);
                notification = new NotificationCompat.Builder(this)
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setContentTitle("Pomodoro")
                        .setContentText("Pausa corta")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setVisibility(Notification.VISIBILITY_PUBLIC).build();
                pausarPomodoro(intervaloDescansoCorto);
            }

        }else if(numeroNotificaciones%4==0){
            preferencia.setEstado(this,0);
            if(numeroNotificaciones==16){
                numeroNotificaciones=0;
            }
            Toast.makeText(this.getBaseContext(), "Inicia pomodoro", Toast.LENGTH_SHORT).show();
            restaurarPomodoro();
        }
    }

    /**
     * Se cancela la alarma que ha sido creada.
     */
    public void cancelarPomodoro(){
        Log.d("@@", "CANCELA ALARMA");
        try {
            alarmManager.cancel(pendingIntentAlarma);
        }catch (NullPointerException e ){
            Log.d("@@", "NO EXISTE ALARMA PARA CANCELAR");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cancelarPomodoro();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean existeAlarma(){
        return alarmManager!=null;
    }
}
