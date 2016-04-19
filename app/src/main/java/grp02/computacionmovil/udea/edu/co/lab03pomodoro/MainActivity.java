package grp02.computacionmovil.udea.edu.co.lab03pomodoro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private String nombreTarea;
    private TextView textView_nombreTarea;
    private TextView btnIniciar;
    private TextView btnParar;
    private Chronometer tiempo;
    private PreferenciaCompartida preferencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferencia = new PreferenciaCompartida();
        btnIniciar=(TextView)findViewById(R.id.textView_iniciar);
        btnParar=(TextView)findViewById(R.id.textView_detener);
        tiempo = (Chronometer) findViewById(R.id.cronometro);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnIniciar.setVisibility(TextView.INVISIBLE);
                btnParar.setVisibility(TextView.VISIBLE);
                int pomodoro = preferencia.getPomodoro(MainActivity.this);
                int corto = preferencia.getCortoTiempo(MainActivity.this);
                int largo = preferencia.getLargoTiempo(MainActivity.this);
                Log.d("@@@",pomodoro+"");
                Log.d("@@@", corto + "");
                Log.d("@@@", largo + "");
                iniciarServicioPomodoro(pomodoro, corto, largo);
            }
        });

        btnParar.setVisibility(TextView.INVISIBLE);
        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnIniciar.setVisibility(TextView.VISIBLE);
                btnParar.setVisibility(TextView.INVISIBLE);
                pararServicioPomodoro();
            }
        });

        textView_nombreTarea = (TextView) findViewById(R.id.textView_nombreTarea);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarNuevoPomodoro();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("@@", "onResume");

        // put your code here...

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mostrarDialogConfiguracion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("@@", "DESTROY");
        System.out.println("DESTROY MAIN");
    }

    public void agregarNuevoPomodoro() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo pomodoro");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nombreTarea = input.getText().toString();
                textView_nombreTarea.setText(nombreTarea);
                btnIniciar.setVisibility(TextView.VISIBLE);
                btnParar.setVisibility(TextView.INVISIBLE);
                pararServicioPomodoro();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**
     * Se llama al ServicioPomodoro y se le pasa parmatros para configurar los tiempos del pomodoro
    */
    private void iniciarServicioPomodoro(int pomodoro, int corto, int largo){
        Intent inicioServicio = new Intent(this,ServicioPomodoro.class);
        inicioServicio.setAction("intentIniciar");
        inicioServicio.putExtra("intervaloPomodoro", pomodoro);
        inicioServicio.putExtra("intervaloDescansoCorto", corto);
        inicioServicio.putExtra("intervaloDescansoLargo", largo);
        startService(inicioServicio);
        tiempo.setBase(SystemClock.elapsedRealtime());
        tiempo.start();
        controlCronometro();
    }


    /**
     * Se para el ServicioPomodoro
     */
    private void pararServicioPomodoro(){
        Intent pararServicio = new Intent(this,ServicioPomodoro.class);
        pararServicio.setAction("intentParar");
        startService(pararServicio);
        tiempo.stop();
        tiempo.setBase(SystemClock.elapsedRealtime());
    }

    public void mostrarDialogConfiguracion(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_configurar);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Configurar");

        //Inicio cargar los spinner
        ArrayAdapter<CharSequence> adapter;
        Spinner spinner_corto = (Spinner) dialog.findViewById(R.id.spinner_corto);
        adapter = ArrayAdapter.createFromResource(this, R.array.duracion_corta,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_corto.setAdapter(adapter);

        Spinner spinner_largo = (Spinner) dialog.findViewById(R.id.spinner_largo);
        adapter = ArrayAdapter.createFromResource(this, R.array.duracion_larga,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_largo.setAdapter(adapter);
        // Fin cargar los spinner

        ToggleButton toggleButton = (ToggleButton) dialog.findViewById(R.id.toggle_vibracion);
        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBarVolumen);
        Button button = (Button) dialog.findViewById(R.id.boton_guardar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarConfiguracion(dialog);
            }
        });

        Button button1 = (Button) dialog.findViewById(R.id.boton_debug);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugConfiguracion(dialog);
            }
        });

        System.out.println("@@" + preferencia.getEstadoServicio(this));
        toggleButton.setChecked(preferencia.getVibracion(this));
        seekBar.setProgress(preferencia.getNivelSonidoAlarma(this));
        spinner_corto.setSelection(preferencia.getCorto(this));
        spinner_largo.setSelection(preferencia.getLargo(this));

        /*
        AudioManager mobilemode = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mobilemode.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volumen, 0);
        */

        dialog.show();
    }

    public void guardarConfiguracion(Dialog view){

        Log.d("@@@", "GUARDANDO");

        ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggle_vibracion);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBarVolumen);
        Spinner spinner_corto = (Spinner) view.findViewById(R.id.spinner_corto);
        Spinner spinner_largo = (Spinner) view.findViewById(R.id.spinner_largo);

        preferencia.setPomodoro(this, 1500000);
        preferencia.setNivelSonidoAlarma(this, seekBar.getProgress());
        preferencia.setCorto(this, spinner_corto.getSelectedItemPosition());
        preferencia.setLargo(this, spinner_largo.getSelectedItemPosition());
        preferencia.setVibracion(this, toggleButton.isChecked());
        view.dismiss();
    }

    public void debugConfiguracion(Dialog view){
        guardarConfiguracion(view);
        preferencia.configuracionDebug(view.getContext());
        view.dismiss();
    }

    public void controlCronometro(){
        tiempo.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                final long milisegundos = SystemClock.elapsedRealtime() - chronometer.getBase();
                final int tiempoPomodoro = preferencia.getPomodoro(MainActivity.this);
                final int tiempoCorto = preferencia.getCortoTiempo(MainActivity.this);
                final int tiempoLargo = preferencia.getLargoTiempo(MainActivity.this);
                int estado = preferencia.getEstado(MainActivity.this);

                switch (estado){
                    case 0:
                        if (milisegundos>tiempoPomodoro){
                            chronometer.stop();
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                        }
                        break;
                    case 1:
                        if (milisegundos>tiempoCorto){
                            chronometer.stop();
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                        }
                        break;
                    case 2:
                        if (milisegundos>tiempoLargo){
                            chronometer.stop();
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                        }
                        break;
                }
            }
        });
    }
}
