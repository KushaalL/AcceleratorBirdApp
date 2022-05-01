package com.example.acceleratorbirdproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivityStart extends AppCompatActivity {

    ImageView birdgif;
    ImageView startGame;
    TextView creatorTitle;
    TextView timed;
    TextView endless;
    String gamemode = "";
    MediaPlayer mP;
    static final String startString = "YoYoYo";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);
        startGame = findViewById(R.id.imageViewStart);
        creatorTitle = findViewById(R.id.creator);
        birdgif = findViewById(R.id.gifStart);
        timed = findViewById(R.id.textView60Seconds);
        endless = findViewById(R.id.textViewEndless);
        timed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamemode = "60";
                Toast.makeText(MainActivityStart.this,"Sixty Second Gamemode Selected",Toast.LENGTH_SHORT).show();
            }
        });
        endless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamemode = "endless";
                Toast.makeText(MainActivityStart.this,"Endless Gamemode Selected",Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(this).asGif()
                .load(R.drawable.flappybirdgif)
                .into(birdgif);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gamemode.equalsIgnoreCase("60")||gamemode.equalsIgnoreCase("endless"))
                {
                    Intent startGame = new Intent(MainActivityStart.this,MainActivity.class);
                    startGame.putExtra(startString,gamemode);
                    startActivity(startGame);
                }
                else
                    Toast.makeText(MainActivityStart.this,"Select Gamemode",Toast.LENGTH_SHORT).show();
            }
        });
        AudioManager manager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        mP = MediaPlayer.create(this,R.raw.theme);
        mP.setLooping(true);
        if(!manager.isMusicActive())
            if(!mP.isPlaying())
                mP.start();
    }
}