package grp02.computacionmovil.udea.edu.co.lab03pomodoro;

import android.app.AlarmManager;
import android.app.Notification;
<<<<<<< HEAD
import android.app.NotificationManager;
=======
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
=======
import android.os.IBinder;
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Calendar;
<<<<<<< HEAD
import java.util.Timer;
import java.util.TimerTask;
=======
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c

/**
 * Created by Pc1 on 14/04/2016.
 */
public class ServicioPomodoro extends Service {


<<<<<<< HEAD
    private Timer temporizador;
    private static final long INTERVALO_ACTUALIZACION = 10; // En ms
    public static MainActivity UPDATE_LISTENER;
    private double cronometro;
    private int cronometroInt;
=======
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
    private int intervaloPomodoro;
    private int intervaloDescansoLargo;
    private int intervaloDescansoCorto;
    private PendingIntent pendingIntentAlarma;
    private int numeroNotificaciones;
    private int numeroPomodoros;
    private AlarmManager alarmManager;
    private Calendar calendar;
<<<<<<< HEAD
    private Chronometer tiempo;
    private Notification notification;
    private Uri uri;
    private long []vibrador={10,2000};
    private NotificationManager notificationManager;
    private Intent intentMain;
    private PendingIntent pIntent;
    private Handler handler;
    private int minutos;
    private int segundos;


=======
    private Notification notification;
    private PreferenciaCompartida preferencia;
    private Chronometer tiempo;
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c

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
<<<<<<< HEAD
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                UPDATE_LISTENER.actualizarCronometro(formatoCronometro());
            }
        };
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        intentMain = new Intent(getApplicationContext(), MainActivity.class);
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentMain, 0);
    }

    public static void setUpdateListener(MainActivity mainActivity) {
        UPDATE_LISTENER = mainActivity;
=======
        preferencia = new PreferenciaCompartida();
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
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
<<<<<<< HEAD
=======
            preferencia.setEstadoServicio(this,true);
            Log.d("@@@", "Estado: "+preferencia.getEstadoServicio(this));
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
            intervaloPomodoro=intent.getExtras().getInt("intervaloPomodoro");
            intervaloDescansoCorto=intent.getExtras().getInt("intervaloDescansoCorto");
            intervaloDescansoLargo=intent.getExtras().getInt("intervaloDescansoLargo");

<<<<<<< HEAD
=======
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentMain, 0);

>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
            //crecion de la notificacion en la barra de android
            notification = new NotificationCompat.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("Pomodoro")
                    .setContentText("Realizando tarea")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
<<<<<<< HEAD
                    .setVibrate(vibrador)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(uri)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pIntent).build();
            notificationManager.notify(1,notification);
            iniciarPomodoro(intervaloPomodoro);
=======
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pIntent).build();


            iniciarPomodoro();
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
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
<<<<<<< HEAD

                Toast.makeText(this, "Pomodoro cancelado", Toast.LENGTH_SHORT).show();
                stopForeground(true);
                stopSelf();
=======
            preferencia.setEstadoServicio(this,false);
            preferencia.setEstado(this,0);
            Log.d("@@@", "Estado: "+preferencia.getEstadoServicio(this));
            if(existeAlarma()) {
                Toast.makeText(this, "Pomodoro cancelado", Toast.LENGTH_SHORT).show();
                stopForeground(true);
                stopSelf();
            }
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
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
<<<<<<< HEAD
    private void iniciarPomodoro(final int limite) {
        temporizador = new Timer();
        cronometro=0;
        cronometroInt=0;
        temporizador.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cronometro += 0.01;

                cronometroInt=(int) Math.floor(cronometro);
                if(limite == cronometroInt){
                    temporizador.cancel();
                    analizarEstadoPomodoro();
                }
                handler.sendEmptyMessage(0);
            }
        }, 0, INTERVALO_ACTUALIZACION);
    }

    public void pausarPomodoro(int intervaloDescanso){
        iniciarPomodoro(intervaloDescanso);
    }
    public void restaurarPomodoro(){
        iniciarPomodoro(intervaloPomodoro);
    }



    private void cancelarPomodoro() {
        if (temporizador != null)
            temporizador.cancel();

    }
=======
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


>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
    /**
     * Analiza cúal es la acción que se debe realizar despúes de que la alarma ha sonado,
     * es decir, de acuerdo al número de veces que ha sonado la alarma durante la ejecución del servicio
     * o se inicia un nuevo pomodoro, o se inicia un periodo de descanso corto o se inicia un
     * periodo de descanso largo
     */
<<<<<<< HEAD
    public void analizarEstadoPomodoro() {
        numeroNotificaciones = numeroNotificaciones + 1;
        Log.d("@@", "Numero notificaciones: " + numeroNotificaciones);
        if (numeroNotificaciones % 2 == 1) {
            numeroPomodoros = numeroPomodoros + 1;

            if (numeroPomodoros % 4 == 0) {
                Log.d("@@", "Intervalo descanso: LARGO");
=======
    public void analizarEstadoPomodoro(){
        numeroNotificaciones=numeroNotificaciones+1;
        Log.d("@@", "Numero notificaciones: "+ numeroNotificaciones);
        if(numeroNotificaciones % 4==2){
            numeroPomodoros=numeroPomodoros+1;
            Toast.makeText(this.getBaseContext(), "Inicia descanso", Toast.LENGTH_SHORT).show();
            if(numeroPomodoros==4){
                Log.d("@@", "Intervalo descanso: LARGO");
                preferencia.setEstado(this,2);
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
                notification = new NotificationCompat.Builder(this)
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setContentTitle("Pomodoro")
                        .setContentText("Pausa larga")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
<<<<<<< HEAD
                        .setVibrate(vibrador)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSound(uri)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setContentIntent(pIntent).build();
                notificationManager.notify(1, notification);
                pausarPomodoro(intervaloDescansoLargo);
            } else {
                Log.d("@@", "Intervalo descanso: CORTO");
=======
                        .setVisibility(Notification.VISIBILITY_PUBLIC).build();
                pausarPomodoro(intervaloDescansoLargo);
            }else{
                Log.d("@@", "Intervalo descanso: CORTO");
                preferencia.setEstado(this, 1);
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
                notification = new NotificationCompat.Builder(this)
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setContentTitle("Pomodoro")
                        .setContentText("Pausa corta")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
<<<<<<< HEAD
                        .setVibrate(vibrador)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSound(uri)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setContentIntent(pIntent).build();
                notificationManager.notify(1, notification);
                pausarPomodoro(intervaloDescansoCorto);
            }

        } else if (numeroNotificaciones % 2 == 0) {
            if (numeroNotificaciones == 8) {
                numeroNotificaciones = 1;
            }

            notification = new NotificationCompat.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("Pomodoro")
                    .setContentText("Realizando tarea")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setVibrate(vibrador)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(uri)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pIntent).build();
            notificationManager.notify(1, notification);
=======
                        .setVisibility(Notification.VISIBILITY_PUBLIC).build();
                pausarPomodoro(intervaloDescansoCorto);
            }

        }else if(numeroNotificaciones%4==0){
            preferencia.setEstado(this,0);
            if(numeroNotificaciones==16){
                numeroNotificaciones=0;
            }
            Toast.makeText(this.getBaseContext(), "Inicia pomodoro", Toast.LENGTH_SHORT).show();
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
            restaurarPomodoro();
        }
    }

<<<<<<< HEAD
=======
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

>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
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
<<<<<<< HEAD

    public String formatoCronometro(){
        minutos = (cronometroInt%3600)/60;
        segundos = (cronometroInt%3600)%60;
        String segundosStr=String.valueOf(segundos);
        String minutosStr=String.valueOf(minutos);

        if(segundos<10){
            segundosStr = "0"+segundos;
        }
        if(minutos<10){
            minutosStr = "0"+minutos;
        }
        return minutosStr+":"+segundosStr;
    }
=======
>>>>>>> 2fa03efb97d00318a3e28da00b010c2841dbc13c
}
