package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

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



    }
}