package grp02.computacionmovil.udea.edu.co.lab03pomodoro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by Pc1 on 14/04/2016.
 */
public class AlarmaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        AudioManager mobilemode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Log.d("@@", "VOLUMEN");
        Log.d("@@",mobilemode.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)+"");


        Intent actualizarServicio=new Intent(context,ServicioPomodoro.class);
        actualizarServicio.setAction("intentAlarmaReceiver");
        context.startService(actualizarServicio);


    }

}
