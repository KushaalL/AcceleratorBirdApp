package com.example.acceleratorbirdproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class MainActivityEnd extends AppCompatActivity {
    TextView score;
    ImageView restart;
    ImageView birdgif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_end);
        score = findViewById(R.id.textViewScore);
        score.setText("Score: "+getIntent().getStringExtra(MainActivity.scoreInfo));
        restart = findViewById(R.id.restart);
        birdgif = findViewById(R.id.gifEnd);
        Glide.with(this).asGif()
                .load(R.drawable.flappybirdgif)
                .into(birdgif);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBackStart = new Intent(MainActivityEnd.this,MainActivityStart.class);
                startActivity(goBackStart);
            }
        });
    }
}