package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

// info Handler, Runnable (https://es.stackoverflow.com/questions/38201/diferencia-entre-runnable-handler-thread)

public class MainActivity extends AppCompatActivity {

    // declarando widgets
    Button backBtn, playBtn, pauseBtn, forwardBtn;
    TextView titleText, currentTimeText, totalTimeText;
    SeekBar seekBar;

    // Media Player
    MediaPlayer mediaPlayer;

    /**
     * Handler (internamente usa un Thread) permite enviar y procesar objetos (Message y Runnables asociados con MessageQueue de un thread) mensajes entre 2 subprocesos de manera segura,
     * esto significa que el hilo de envío pone el mensaje en la cola de subprocesos
     * de destino y esta cola de destino procesará este mensaje en su momento apropiado.
     */

    // Handlers
    Handler handler = new Handler();

    // Variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicializar widgets
        backBtn = findViewById(R.id.back_btn);
        playBtn = findViewById(R.id.play_btn);
        pauseBtn = findViewById(R.id.pause_btn);
        forwardBtn = findViewById(R.id.forward_btn);

        titleText = findViewById(R.id.song_title);
        currentTimeText = findViewById(R.id.current_time_text);
        totalTimeText = findViewById(R.id.total_time_text);

        seekBar = findViewById(R.id.seekBar);

        // inicializar objeto mediaPlayer con el nombre del recurso dado, en este caso la canción
        mediaPlayer = MediaPlayer.create(this, R.raw.astronaut);

        System.out.println(getResources().getIdentifier(
                "astronaut",
                "raw",
                getPackageName()
        ));

        // setear el título de la canción
        titleText.setText(getResources().getIdentifier(
                "astronaut",
                "raw",
                getPackageName()
        ));


        /*seekBar.setClickable(false);*/
        seekBar.setClickable(true);

        // Agregando funcionalidad de los botones

        // play a la canción
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()) {
                    Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
                    playMusic();
                }
            }
        });

        /**
         * pause(): Pausa la canción actual
         */

        // pausar la canción
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    Toast.makeText(MainActivity.this, "Paused song...", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                }
            }
        });


        // avanzar 10seg a la canción
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if((temp + forwardTime) <= finalTime) {
                    /*startTime = startTime + forwardTime;*/
                    startTime += forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // retroceder 10seg de la canción
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if((temp - backwardTime) > 0) {
                    /*startTime = startTime - forwardTime;*/
                    startTime -= backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't Go Back!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(updateSongTime);
        /*mediaPlayer.stop();
        mediaPlayer.reset();*/
        mediaPlayer.release();

        mediaPlayer = null;
    }

    // start play the music
    private void playMusic() {
        mediaPlayer.start();

        /**
         * getDuration(): Obtener la duración total de una canción en milisegundos (ms)
         */

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        currentTimeText.setText(String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))

        ));

        totalTimeText.setText(String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))
        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(updateSongTime, 100);
    }


    // Creando el objeto runnable para actualizar la canción
    private Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            currentTimeText.setText(String.format(
                    "%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
                    ));

            seekBar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };


}