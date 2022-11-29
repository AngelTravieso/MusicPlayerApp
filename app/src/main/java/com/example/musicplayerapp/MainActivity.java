package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // declaring widgets
    Button backBtn, playBtn, pauseBtn, forwardBtn;
    TextView titleText, timeText;
    SeekBar seekBar;

    // Media Player
    MediaPlayer mediaPlayer;

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

        // initialize widgets
        backBtn = findViewById(R.id.back_btn);
        playBtn = findViewById(R.id.play_btn);
        pauseBtn = findViewById(R.id.pause_btn);
        forwardBtn = findViewById(R.id.forward_btn);

        titleText = findViewById(R.id.text_view);
        timeText = findViewById(R.id.time_left_text);

        seekBar = findViewById(R.id.seekBar);

        // media player
        mediaPlayer = MediaPlayer.create(this, R.raw.down_from_the_sky);

        seekBar.setClickable(false);

        // Adding functionalities for the buttons


        // play the music
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });


        // pause the music
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.pause();
            }
        });


        // advance the music 10seconds
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


        // rewind the music 10seconds
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

    // start play the music
    private void playMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        timeText.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))

        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(updateSongTime, 100);
    }


    // Creating the runnable for update the song
    private Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            timeText.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
                    ));

            seekBar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };


}