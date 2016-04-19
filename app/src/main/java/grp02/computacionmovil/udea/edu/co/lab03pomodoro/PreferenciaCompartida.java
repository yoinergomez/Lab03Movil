package grp02.computacionmovil.udea.edu.co.lab03pomodoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

/**
 * Created by Esteban on 15/04/2016.
 */
public class PreferenciaCompartida {



    private SharedPreferences.Editor getEditor(Context context){
        SharedPreferences preferencias= getSharedPreferences(context);
        SharedPreferences.Editor editor=preferencias.edit();
        return editor;
    }

    private SharedPreferences getSharedPreferences(Context context){
        SharedPreferences preferencias= context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        return preferencias;
    }

    public void setNivelSonidoAlarma(Context context, int nivel){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("nivel", nivel);
        editor.commit();
    }

    public int getNivelSonidoAlarma(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt("nivel", 5);
    }

    public void setVibracion(Context context, boolean vibracion){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("vibracion", vibracion);
        editor.commit();
    }

    public boolean getVibracion(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("vibracion", false);
    }

    public void setCorto(Context context, int corto){
        switch (corto){
            case 0:
                setCortoTiempo(context,180000);
                break;
            case 1:
                setCortoTiempo(context,240000);
                break;
            case 2:
                setCortoTiempo(context,300000);
                break;
            default:
                setCortoTiempo(context,getCortoTiempo(context));
        }
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("corto", corto);
        editor.commit();
    }

    public int getCorto(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt("corto", 0);
    }

    public void setCortoTiempo(Context context, int corto){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("cortoTiempo", corto);
        editor.commit();
    }

    public int getCortoTiempo(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt("cortoTiempo", 180000);
    }

    public void setLargo(Context context, int largo){
        switch (largo){
            case 0:
                setLargoTiempo(context,600000);
                break;
            case 1:
                setLargoTiempo(context,900000);
                break;
            case 2:
                setLargoTiempo(context,1200000);
                break;
            default:
                setLargoTiempo(context,getLargoTiempo(context));
        }
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("largo", largo);
        editor.commit();
    }

    public int getLargo(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt("largo", 0);
    }

    public void setLargoTiempo(Context context, int largo){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("largoTiempo", largo);
        editor.commit();
    }

    public int getLargoTiempo(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt("largoTiempo", 600000);
    }

    public void setPomodoro(Context context, int tiempo){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("pomodoro", tiempo);
        editor.commit();
    }

    public int getPomodoro(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt("pomodoro", 1500000);
    }

    public void setEstadoServicio(Context context, boolean estado){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("estado", estado);
        editor.commit();
    }

    public boolean getEstadoServicio(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("estado", false);
    }

    public void setEstado(Context context, int estado){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("estadoTiempo", estado);
        editor.commit();
    }

    public int getEstado(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt("estadoTiempo", 0);
    }

    public void configuracionDebug(Context context){
        setPomodoro(context, getPomodoro(context) / 60);
        setCortoTiempo(context, getCortoTiempo(context) / 60);
        setLargoTiempo(context, getLargoTiempo(context) / 60);
        setVibracion(context, true);
        setNivelSonidoAlarma(context, 8);
        Log.d("@@@", getPomodoro(context) + "");
        Log.d("@@@", getCortoTiempo(context) + "");
        Log.d("@@@", getLargoTiempo(context) + "");
    }

}
