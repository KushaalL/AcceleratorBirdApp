package com.example.acceleratorbirdproject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //Code from this program has been used from Beginning Android Games
    //Review SurfaceView, Canvas, continue

    GameSurface gameSurface;
    Bitmap bird;
    Bitmap topPipe;
    Bitmap bottomPipe;
    int birdLeft = 50;
    int birdTop = 700;
    int pipeLeft=-100;
    int bPTop=2000;
    int birdWidth;
    int birdHeight;
    int tPWidth;
    int tPHeight;
    int bPWidth;
    int bPHeight;
    boolean goingUp =false;
    boolean goingDown = false;
    int z = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,orientationSensor,SensorManager.SENSOR_DELAY_FASTEST);

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
//        int x = (int)sensorEvent.values[0];
        z = (int)sensorEvent.values[0];
//        int y = (int)sensorEvent.values[1];
//        Log.d("Tag",x+" "+y+" "+z);
        if(z>0)
        {
            goingUp=true;
            goingDown=false;
        }
        else if(z<0)
        {
            goingDown=true;
            goingUp=false;
        }
        else
        {
            goingUp=false;
            goingDown=false;
        }
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
        Rect cBird;
        Rect cTP;
        Rect cBP;

        int screenWidth;
        int screenHeight;

        public GameSurface(Context context) {
            super(context);
            holder=getHolder();
            background=BitmapFactory.decodeResource(getResources(),R.drawable.background);
            topPipe = BitmapFactory.decodeResource(getResources(),R.drawable.toppipe);
            bottomPipe = BitmapFactory.decodeResource(getResources(),R.drawable.bottompipe);
            backgroundScaled = Bitmap.createScaledBitmap(background,1080,2150,false);
            topPipeScaled = Bitmap.createScaledBitmap(topPipe,200,1200,false);
            bottomPipeScaled = Bitmap.createScaledBitmap(bottomPipe,200,1200,false);
            bird= BitmapFactory.decodeResource(getResources(),R.drawable.bird);
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
            while (running == true){
                if (holder.getSurface().isValid() == false)
                    continue;
                // https://developer.android.com/reference/android/graphics/Canvas
                Canvas canvas= holder.lockCanvas();
                if(pipeLeft>-300)
                {
                    pipeLeft-=5;
                }
                else
                {
                     pipeLeft = 1300;
                     bPTop = (int)(Math.random()*1500)+500;

                }
                cBird = new Rect(birdLeft,birdTop,birdLeft+birdWidth,birdTop+birdHeight);
                cBP = new Rect(pipeLeft,bPTop,pipeLeft+bPWidth,bPTop+bPHeight);
                cTP = new Rect(pipeLeft,bPTop-1500,pipeLeft+tPWidth,bPTop-1500+tPHeight);

                canvas.drawBitmap(backgroundScaled,0,0,null);

                canvas.drawBitmap(bottomPipeScaled,pipeLeft,bPTop,null);
                canvas.drawBitmap(topPipeScaled,pipeLeft,bPTop-1500,null);
                canvas.drawRect(cTP,paintProperty);
                canvas.drawRect(cBird,paintProperty);
                canvas.drawBitmap( bird,birdLeft,birdTop,null);
                canvas.drawRect(cBP,paintProperty);

                if(cBird.intersect(cBP))
                {
                    Log.d("Hit","You Hit BottomPipe");
                }
                else if(cBird.intersect(cTP))
                {
                    Log.d("Hit","You Hit TopPipe");
                }
                holder.unlockCanvasAndPost(canvas);
                birdTop-=(z*2);
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
}//Activity}