package com.example.acceleratorbirdproject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import com.jakewharton.processphoenix.ProcessPhoenix;
import java.util.concurrent.atomic.AtomicInteger;
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //Code from this program has been used from Beginning Android Games
    //Review SurfaceView, Canvas, continue
    GameSurface gameSurface;
    boolean timeStop;
    boolean endScreen;
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
    static AtomicInteger endTime;
    String gamemode;
    MediaPlayer scorePlayer;
    int z = 0;
    int pipeSpeed =5;
    private GestureDetectorCompat gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);
        timeStop = true;
        endScreen = false;
        gamemode = getIntent().getStringExtra(MainActivityStart.startString);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerationSensor,SensorManager.SENSOR_DELAY_FASTEST);
        time = new AtomicInteger(61);
        endTime = new AtomicInteger(3);
        scorePlayer = MediaPlayer.create(this,R.raw.scorenoise);
        gestureDetector = new GestureDetectorCompat(this,new GestureListener());
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
        z = (int)sensorEvent.values[0];
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
        Boolean ender;
        int screenWidth;
        int screenHeight;
        Typeface customTypeface;
        Boolean deathSound;
        MediaPlayer deathPlayer;
        public GameSurface(Context context) {
            super(context);
            holder=getHolder();
            style = new Paint();
            style.setARGB(255,255,183,0);
            style.setTextSize(300);
            customTypeface = ResourcesCompat.getFont(context, R.font.flappybirdfont);
            style.setTypeface(customTypeface);
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
            ender = true;
            Display screenDisplay = getWindowManager().getDefaultDisplay();
            Point sizeOfScreen = new Point();
            screenDisplay.getSize(sizeOfScreen);
            screenWidth=sizeOfScreen.x;
            screenHeight=sizeOfScreen.y;
            paintProperty= new Paint();
            paintProperty.setColor(Color.BLACK);
            deathSound = true;
            deathPlayer =MediaPlayer.create(MainActivity.this,R.raw.deathsound);
        }
        @Override
        public void run() {
            Log.d("Height",""+screenHeight);
            if(gamemode.equalsIgnoreCase("60"))
                new Timer().start();
            new endTimer().start();
            while (running == true)
            {
                if (holder.getSurface().isValid() == false)
                    continue;
                // https://developer.android.com/reference/android/graphics/Canvas
                Canvas canvas= holder.lockCanvas();

                cBird = new Rect(birdLeft+50,birdTop+80,birdLeft+birdWidth-53,birdTop+birdHeight-80);
                cBP = new Rect(pipeLeft,bPTop,pipeLeft+bPWidth,bPTop+bPHeight);
                cTP = new Rect(pipeLeft,bPTop-1500,pipeLeft+tPWidth,bPTop-1500+tPHeight);
                if((!cBird.intersect(cBP))&&(!cBird.intersect(cTP))&&alive)
                {
                    birdState = bird;
                    if(pipeLeft>-300)
                        pipeLeft-=pipeSpeed;
                    else
                    {
                        pipeLeft = 1300;
                        bPTop = (int)(Math.random()*1000)+500;
                        score++;
                        scorePlayer.start();
                    }
                    if(birdTop<0)
                        birdTop=0;
                    else if(birdTop>1924)
                        birdTop =1924;
                    else
                        birdTop-=(z*2);
                }
                else
                {
                    if(deathSound)
                    {
                        deathSound = false;
                        deathPlayer.start();
                    }
                    birdState = deadBird;
                    alive = false;
                    if(birdTop>screenHeight)
                        endScreen = true;
                    birdTop+=15;
                }
                canvas.drawBitmap(backgroundScaled,0,0,null);
                canvas.drawBitmap(bottomPipeScaled,pipeLeft,bPTop,null);
                canvas.drawBitmap(topPipeScaled,pipeLeft,bPTop-1500,null);
                if(birdTop<screenHeight)
                {
                    Log.d("Death","Death");
                    canvas.drawBitmap(birdState,birdLeft,birdTop,null);
                }

                if(gamemode.equalsIgnoreCase("60")&&!endScreen)
                {
                    style.setTextSize(175);
                    canvas.drawText("Time: "+time.intValue()+" Score: "+score,75,150,style);
                }
                else if(gamemode.equalsIgnoreCase("endless")&&!endScreen)
                {
                    style.setTextSize(200);
                    canvas.drawText("Score: "+score,275,150,style);
                }
                else if(endScreen)
                {
                    style.setTextSize(300);
                    canvas.drawText("Game Over",150,300,style);
                    style.setTextSize(250);
                    canvas.drawText("Score: "+score,275,500,style);
                    if(ender)
                    {
                        ender = false;
                        new endTimer().start();
                    }
                }
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
            while(time.intValue()>=0&&timeStop)
            {
                if(endScreen) {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(time.intValue()==0)
                {
                    endScreen= true;
                    alive = false;
                }
                time.getAndDecrement();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class endTimer extends Thread{
        public void run()
        {
            while(endTime.intValue()>=0&&endScreen)
            {
                if(endTime.intValue()==0)
                    ProcessPhoenix.triggerRebirth(getApplicationContext());
                endTime.getAndDecrement();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class GestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }
        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if(pipeSpeed==5)
            {
                pipeSpeed=10;
                Toast.makeText(MainActivity.this,"Fast",Toast.LENGTH_SHORT).show();
            }
            else
            {
                pipeSpeed=5;
                Toast.makeText(MainActivity.this,"Normal",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}//Activity