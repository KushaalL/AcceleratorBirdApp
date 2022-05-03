package com.example.acceleratorbirdproject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.atomic.AtomicInteger;
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //Code from this program has been used from Beginning Android Games
    //Review SurfaceView, Canvas, continue
    GameSurface gameSurface;
    Bitmap bird;
    Bitmap deadBird;
    Bitmap topPipe;
    Bitmap bottomPipe;
    Bitmap birdState;
    int birdLeft = 50;
    int birdTop = 700;
    int pipeLeft=1300;
    int bPTop=(int)(Math.random()*1000)+500;
    int birdWidth;
    int birdHeight;
    int tPWidth;
    int tPHeight;
    int bPWidth;
    int bPHeight;
    int score = 0;
    boolean alive = true;
    static AtomicInteger time;
    static final String scoreInfo = "Kushaal";
    String gamemode;
    MediaPlayer scorePlayer;
    int z = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);
        gamemode = getIntent().getStringExtra(MainActivityStart.startString);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerationSensor,SensorManager.SENSOR_DELAY_FASTEST);
        time = new AtomicInteger(60);
        if(gamemode.equalsIgnoreCase("60"))
            new Timer().start();
        scorePlayer = MediaPlayer.create(this,R.raw.scorenoise);
    }
    @Override
    protected void onPause(){
        super.onPause();
        gameSurface.pause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        gameSurface.resume();
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int x = (int)sensorEvent.values[2];
        z = (int)sensorEvent.values[0];
        int y = (int)sensorEvent.values[1];
        Log.d("Tag",x+" "+y+" "+z+"hey");
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    //----------------------------GameSurface Below This Line--------------------------
    public class GameSurface extends SurfaceView implements Runnable {
        //https://developer.android.com/reference/android/view/SurfaceView

        Thread gameThread;
        SurfaceHolder holder;
        volatile boolean running = false;
        Bitmap background;
        Bitmap backgroundScaled;
        Bitmap topPipeScaled;
        Bitmap bottomPipeScaled;
        Paint paintProperty;
        Paint style;
        Rect cBird;
        Rect cTP;
        Rect cBP;
        int screenWidth;
        int screenHeight;

        public GameSurface(Context context) {
            super(context);
            holder=getHolder();
            //Trying to add style for text
//            style.setARGB(100,255,183,0);
//            Typeface plain = Typeface.createFromFile(String.valueOf(R.font.flappybirdfont));
//            style.setTypeface(plain);
            background=BitmapFactory.decodeResource(getResources(),R.drawable.background);
            topPipe = BitmapFactory.decodeResource(getResources(),R.drawable.toppipe);
            bottomPipe = BitmapFactory.decodeResource(getResources(),R.drawable.bottompipe);
            backgroundScaled = Bitmap.createScaledBitmap(background,1080,2150,false);
            topPipeScaled = Bitmap.createScaledBitmap(topPipe,200,1200,false);
            bottomPipeScaled = Bitmap.createScaledBitmap(bottomPipe,200,1200,false);
            bird= BitmapFactory.decodeResource(getResources(),R.drawable.bird);
            deadBird= BitmapFactory.decodeResource(getResources(),R.drawable.deadbird);
            birdWidth = bird.getWidth();
            birdHeight = bird.getHeight();
            tPWidth = topPipeScaled.getWidth();
            bPWidth = bottomPipeScaled.getWidth();
            tPHeight = topPipeScaled.getHeight();
            bPHeight = bottomPipeScaled.getHeight();
            Display screenDisplay = getWindowManager().getDefaultDisplay();
            Point sizeOfScreen = new Point();
            screenDisplay.getSize(sizeOfScreen);
            screenWidth=sizeOfScreen.x;
            screenHeight=sizeOfScreen.y;
            System.out.println("X: "+screenWidth);
            System.out.println("Y: "+screenHeight);
            paintProperty= new Paint();
            paintProperty.setColor(Color.BLACK);
        }
        @Override
        public void run() {
            while (running == true&&time.intValue()>=0){
                if (holder.getSurface().isValid() == false)
                    continue;
                // https://developer.android.com/reference/android/graphics/Canvas
                Canvas canvas= holder.lockCanvas();
                if(gamemode.equalsIgnoreCase("60"))
                {
                    //add score and time
                }
                else
                {
                    //add score only
//                    canvas.drawText("Hey",0,0,style);
                }
                cBird = new Rect(birdLeft+50,birdTop+80,birdLeft+birdWidth-53,birdTop+birdHeight-80);
                cBP = new Rect(pipeLeft,bPTop,pipeLeft+bPWidth,bPTop+bPHeight);
                cTP = new Rect(pipeLeft,bPTop-1500,pipeLeft+tPWidth,bPTop-1500+tPHeight);
                if(!cBird.intersect(cBP)&&!cBird.intersect(cTP)&&alive)
                {
                    birdState = bird;
                    if(pipeLeft>-300)
                        pipeLeft-=10;
                    else
                    {
                        pipeLeft = 1300;
                        bPTop = (int)(Math.random()*1000)+500;
                        score++;
                        scorePlayer.start();
                        Log.d("Score",""+score);
                    }
                    birdTop-=(z*2);
                }
                else
                {
                    birdState = deadBird;
                    alive = false;
                    Log.d("Fall",""+birdTop);
                    if(birdTop>1630)
                    {
                        Log.d("death","died");
                        nextPart();
                    }
                    birdTop+=15;

                }
                canvas.drawBitmap(backgroundScaled,0,0,null);
                canvas.drawBitmap(bottomPipeScaled,pipeLeft,bPTop,null);
                canvas.drawBitmap(topPipeScaled,pipeLeft,bPTop-1500,null);
                canvas.drawBitmap(birdState,birdLeft,birdTop,null);
                holder.unlockCanvasAndPost(canvas);
            }
        }
        public void resume(){
            running=true;
            gameThread=new Thread(this);
            gameThread.start();
        }
        public void pause() {
            running = false;
            while (true) {
                try {
                    gameThread.join();
                } catch (InterruptedException e) {
                }
            }
        }
    }//GameSurface
    public class Timer extends Thread{
        public void run()
        {
            while(time.intValue()>=0)
            {
                if(time.intValue()==0)
                    nextPart();
                time.getAndDecrement();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void nextPart()
    {
        Log.d("death","hey");
        Intent endGame = new Intent(MainActivity.this,MainActivityEnd.class);
        endGame.putExtra("scoreInfo",Integer.toString(score));
        startActivity(endGame);
    }
}//Activity}