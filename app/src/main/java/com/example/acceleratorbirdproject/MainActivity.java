package com.example.acceleratorbirdproject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
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
    int birdLeft = 50;
    int birdTop = 700;
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
        Paint paintProperty;

        int screenWidth;
        int screenHeight;

        public GameSurface(Context context) {
            super(context);
            holder=getHolder();
            background=BitmapFactory.decodeResource(getResources(),R.drawable.background);
            backgroundScaled = Bitmap.createScaledBitmap(background,1080,2100,false);
            bird= BitmapFactory.decodeResource(getResources(),R.drawable.bird);
            Display screenDisplay = getWindowManager().getDefaultDisplay();
            Point sizeOfScreen = new Point();
            screenDisplay.getSize(sizeOfScreen);
            screenWidth=sizeOfScreen.x;
            screenHeight=sizeOfScreen.y;
            System.out.println("X: "+screenWidth);
            System.out.println("Y: "+screenHeight);

            paintProperty= new Paint();


        }

        @Override
        public void run() {
            while (running == true){
                if (holder.getSurface().isValid() == false)
                    continue;
                // https://developer.android.com/reference/android/graphics/Canvas
                Canvas canvas= holder.lockCanvas();
                canvas.drawBitmap(backgroundScaled,0,0,null);
                canvas.drawBitmap( bird,birdLeft,birdTop,null);
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