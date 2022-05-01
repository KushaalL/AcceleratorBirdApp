package com.example.acceleratorbirdproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    RadioGroup mode;
    RadioButton timed;
    RadioButton endless;
    String gamemode = "";
    static final String startString = "YoYoYo";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);
        startGame = findViewById(R.id.imageViewStart);
        creatorTitle = findViewById(R.id.creator);
        birdgif = findViewById(R.id.gifStart);
        mode = findViewById(R.id.mode);
        timed = findViewById(R.id.radioButton60Sec);
        endless = findViewById(R.id.radioButtonEndless);
        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==2131296741)
                    gamemode = "60";
                else
                    gamemode = "endless";
            }
        });
        Glide.with(this).asGif()
                .load(R.drawable.flappybirdgif)
                .into(birdgif);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timed.isChecked()||timed.isChecked())
                {
                    Intent startGame = new Intent(MainActivityStart.this,MainActivity.class);
                    startGame.putExtra(startString,gamemode);
                    startActivity(startGame);
                }
                else
                {
                    Toast.makeText(MainActivityStart.this,"Select Gamemode",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}