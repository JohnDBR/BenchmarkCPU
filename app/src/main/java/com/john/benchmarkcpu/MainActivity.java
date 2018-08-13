package com.john.benchmarkcpu;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.help_button) Button helpButton;
    @BindView(R.id.start_button) Button startButton;
    @BindView(R.id.comparing_button) Button comparingButton;
    @BindView(R.id.about_us_text_view) TextView aboutUsTextView;
    @BindView(R.id.score_text_view) TextView scoreTextView;

    private boolean counting = false;
    private int score = 0;
    private Handler handler;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handler = new Handler();
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        scoreTextView.setText(settings.getString("cpu_bench_score", "Puntaje: 0"));
    }

    public void startCounting(View view){
//        Toast.makeText(getApplicationContext(), ,
//                Toast.LENGTH_SHORT).show();
        scoreTextView.setText("Calculando...");
        helpButton.setEnabled(false);
        startButton.setEnabled(false);
        comparingButton.setEnabled(false);
        aboutUsTextView.setEnabled(false);
        counting = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                counting = false;
                scoreTextView.setText("Puntaje: " + score);
                settings.edit().putString("cpu_bench_score", "Puntaje: " + score).apply();
                score = 0;
                helpButton.setEnabled(true);
                startButton.setEnabled(true);
                comparingButton.setEnabled(true);
                aboutUsTextView.setEnabled(true);
            }
        }, 60000);
        Thread secondary = new Thread(){
            public void run(){
                while (counting) {
                    Thread thread = new Thread(){
                        public void run(){
                            Log.i("Thread", "Thread Running!" + score);
                            score++;
                        }
                    };
                    thread.start();
                }
            }
        };
        secondary.start();
    }

    public void openCompareActivity(View view) {
        startActivity(new Intent(this, CompareResults.class));
    }

    public void showInstructions(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Instrucciones")
                .setMessage("La aplicación evaluará el rendimiento del CPU en su dispositivo movil. " +
                        "La prueba tardará un minuto en el que no podra ejecutar ninguna otra opción" +
                        " de la apliación. Para mayor precision de la prueba se recomienda poner el " +
                        "celular en modo avión y no tener ninugna otra aplicación ejecutandose en background" +
                        ". Contactenos para conocer los detalles tecnicos de la prueba.")
                .setPositiveButton(android.R.string.yes, null).show();
    }

    public void showInfoAboutUs(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Acerca de nosotros")
                .setMessage("Esta aplicacion fue desarrollada por los estudiantes de septimo semestre John Barbosa, " +
                        "Sebastian Cabarcas, Juan Arellano, el dia 08-12-2018 en Colombia-Atlantico-Barranquilla, " +
                        " con propositos estudiantiles para la Universidad del Norte, en el departamento de " +
                        "Ingenieria de Sitemas, en el curso de Estructura del Computador II, y esta fue " +
                        "presentada y evaluada por el profesor Augusto Salazar.")
                .setPositiveButton(android.R.string.yes, null).show();
    }
}
