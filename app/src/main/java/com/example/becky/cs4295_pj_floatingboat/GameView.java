package com.example.becky.cs4295_pj_floatingboat;

/**
 * Created by Becky on 16/11/2015.
 */
import com.example.becky.cs4295_pj_floatingboat.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    public static Activity act;
    public static Context con;
    public static Resources res;
    public static final int InitialTarget = 6;
    public static final int InitialBlocker = 4;
    public static final int MaxLife = 10;
    public static int NumOfHit;
    public static int NumOfLive;
    public static double CreateTime;
    public static double ElapsedTime;
    public static double ElapsedTime_count;
    public static boolean GameOver = true;
    public static boolean timeRunning = false;
    public static boolean posAdded = false;
    public static final int TARGET_SOUND_ID = 0;
    public static final int BLOCKER_SOUND_ID = 1;
    public static SoundPool soundPool;
    public static Map<Integer,Integer> soundMap;
    public static android.graphics.PointF boat = new android.graphics.PointF();
    public static ArrayList<Point> life = new ArrayList<Point>();
    public static ConcurrentMap<Integer,Point> targets =
            new ConcurrentHashMap<Integer,Point>();
    public static ConcurrentMap<Integer,Point> blockers =
            new ConcurrentHashMap<Integer,Point>();
    public static ConcurrentMap<Integer,ArrayList> target_velocities =
            new ConcurrentHashMap<Integer,ArrayList>();
    public static ConcurrentMap<Integer,ArrayList> blockerVelocities =
            new ConcurrentHashMap<Integer,ArrayList>();
    public static List<Integer> ti = new ArrayList<Integer>();
    public static List<Integer> bi = new ArrayList<Integer>();
    public static int min=0;
    public static int sec=0;


    public static double elapsedTime_c;
    public static long previousTime=0;
    public static double currentTime=0;
    public static double elapsedTimee=0;
    public static long pauseDuration=0;
    public static long pauseTime=0;
    public static boolean storedStatus=true;

    public static Bitmap boatBitmap;
    public static Bitmap lifeBitmap;
    public static Bitmap targetBitmap;
    public static Bitmap blockerBitmap;
    public static Bitmap bgBitmap;
    public static int NumOfTargets;
    public static int NumOfBlockers;
    public static ArrayList<Double> target_velocities_X = new ArrayList<Double>();
    public static ArrayList<Double> target_velocities_Y = new ArrayList<Double>();
    public static ArrayList<Double> blocker_Velocities_X = new ArrayList<Double>();
    public static ArrayList<Double> blocker_velocities_Y = new ArrayList<Double>();
    public static int boatInitialX;
    public static int boatInitialY;
    public int targetSpeed;
    public int blockerSpeed;
    public static int lifeHeight;
    public static int lifeWidth;
    public static int screenWidth;
    public static int screenHeight;
    public static int boatDiameterW;
    public static int boatDiameterH;
    public static int boatRadius;
    public static int targetDiameter;
    public static int targetRadius;
    public static int blockerDiameter;
    public static int blockerRadius;
    public static Paint bgPaint = new Paint();
    public static Handler handerA = new Handler();
    public static Handler handerB = new Handler();
    public static Runnable runnableA;
    public static Runnable runnableB;

    public GameView(Context context, AttributeSet attset) {
        super(context, attset);
        act = (Activity) context;
        con = context;
        res = getResources();
        getHolder().addCallback(this);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundMap = new HashMap<Integer, Integer>();
        soundMap.put(TARGET_SOUND_ID, soundPool.load(context, R.raw.apple, 1));
        soundMap.put(BLOCKER_SOUND_ID, soundPool.load(context, R.raw.ghost, 1));
    }





    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh)
    {
        if(AccSensor.getRunFlag()) {
            super.onSizeChanged(w, h, ow, oh);
            screenWidth = w;
            screenHeight = h;
            boatDiameterW = w / 10;
            boatDiameterH = h / 10;
            targetDiameter = w / 6;
            blockerDiameter = w / 6;
            boatRadius = boatDiameterH / 2;
            targetRadius = targetDiameter / 2;
            blockerRadius = blockerDiameter / 2;
            targetSpeed = w * 2;
            blockerSpeed = targetSpeed;
            boatInitialX = w / 2;
            boatInitialY = h / 2;
            boatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boat_in_game);
            boatBitmap = Bitmap.createScaledBitmap(boatBitmap, boatDiameterW, boatDiameterH, true);
            lifeWidth = w / 12;
            lifeHeight = lifeWidth;

            for (int i = 0; i < MaxLife; i++) {
                life.add(new Point((i * 40), 0));
            }

            lifeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.life);
            lifeBitmap = Bitmap.createScaledBitmap(lifeBitmap, lifeWidth, lifeHeight, true);

            targetBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
            targetBitmap = Bitmap.createScaledBitmap(targetBitmap, targetDiameter,
                    targetDiameter, true);

            blockerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
            blockerBitmap = Bitmap.createScaledBitmap(blockerBitmap, blockerDiameter,
                    blockerDiameter, true);

            bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
            bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenWidth, screenHeight, true);

            newGame();
        }

    }



    public static void stopGame() {
        targets.clear();
        blockers.clear();
        AccSensor.handler.removeCallbacks(AccSensor.abletorun);
        handerA.removeCallbacks(runnableA);
        handerB.removeCallbacks(runnableB);
    }




    public void releaseResources() {
        targets.clear();
        blockers.clear();
        soundPool.release();
        soundPool = null;
    }


    public static void newGame() {
        if(AccSensor.getRunFlag()) {
            NumOfLive = 3;

            boat.set(boatInitialX, boatInitialY);

            addTargetsAndBlockers();

            if (GameOver) {
                GameOver = false;
            }

            AccSensor.handler.postDelayed(AccSensor.abletorun, 10);

            runnableA = new Runnable() {
                @Override
                public void run() {
                    addBlocker();
                    handerA.postDelayed(runnableA, 10000);
                }
            };
            runnableB = new Runnable() {
                @Override
                public void run() {
                    addTarget();
                    handerB.postDelayed(runnableB, 2000);
                }
            };

            handerA.postDelayed(runnableA, 10000);
            handerB.postDelayed(runnableB, 2000);

            CreateTime = System.currentTimeMillis();
        }
    }


    public static void addTargetsAndBlockers() {
        if(AccSensor.getRunFlag()) {
            int randomX = screenWidth - targetDiameter;
            int randomY = screenHeight - targetDiameter;

            NumOfTargets = InitialTarget;
            NumOfBlockers = InitialBlocker;

            for (int i = 0; i < InitialTarget; i++) {
                Random Gen = new Random();
                int x = Gen.nextInt(randomX);
                int y = Gen.nextInt(randomY);
                Point pos = new Point(x, y);
                targets.put(i, pos);

                Random velGen = new Random();
                double ta1 = velGen.nextDouble() * 300.0;
                double ta2 = velGen.nextDouble() * 300.0;
                ArrayList<Double> vel = new ArrayList<Double>();
                vel.add(Math.sin(ta1));
                vel.add(Math.sin(ta2));
                target_velocities.put(i, vel);

                ti.add(i);
            }


            for (int i = 0; i < InitialBlocker; i++) {
                Random Gen = new Random();
                int x = Gen.nextInt(randomX);
                int y = Gen.nextInt(randomY);
                Point pos = new Point(x, y);
                blockers.put(i, pos);

                Random velGen = new Random();
                double ta1 = velGen.nextDouble() * 300.0;
                double ta2 = velGen.nextDouble() * 300.0;
                ArrayList<Double> vel = new ArrayList<Double>();
                vel.add(Math.sin(ta1));
                vel.add(Math.sin(ta2));
                blockerVelocities.put(i, vel);

                bi.add(i);
            }
            posAdded = true;
        }
    }


    public static void stopUpdatePositions(){
        Iterator<Integer> iterB = bi.iterator();
        while(iterB.hasNext()) {
            int i = iterB.next();
            Point blocker = blockers.get(i);
            if (blocker != null) {
                ArrayList<Double> vels = blockerVelocities.get(i);
                double xVelocity = vels.get(0);
                double yVelocity = vels.get(1);
                int changeX;
                int changeY;

                blocker.x += 0;
                blocker.y += 0;
                blockers.replace(i,blocker);
                blockerVelocities.replace(i, vels);
            }
        }

        Iterator<Integer> iter = ti.iterator();
        while(iter.hasNext()) {

            int i = iter.next();
            Point target = targets.get(i);
            if (target != null) {
                ArrayList<Double> vels = target_velocities.get(i);
                double xVelocity = vels.get(0);
                double yVelocity = vels.get(1);
                target.x += 0;
                target.y += 0;
                Log.v("Test", "Updated velocity: " + vels);
                targets.replace(i, target);
                target_velocities.replace(i, vels);
            }
        }
    }

    public static void updatePositions()
    {

        if (GameOver) {
            return;
        }
        if(AccSensor.getRunFlag()) {
            checkForCollisions();
            addThings();


            Iterator<Integer> iter = ti.iterator();
            while (iter.hasNext()) {

                int i = iter.next();
                Point target = targets.get(i);
                if (target != null) {
                    ArrayList<Double> vels = target_velocities.get(i);
                    double xVelocity = vels.get(0);
                    double yVelocity = vels.get(1);
                    int changeX;
                    int changeY;

                    if (target.x + targetDiameter > screenWidth || target.x <= 0) {
                        xVelocity *= -1;
                        vels.set(0, xVelocity);
                    }
                    if (target.y + targetDiameter > screenHeight || target.y <= 0) {
                        yVelocity *= -1;
                        vels.set(1, yVelocity);
                    }

                    if (xVelocity < 0) {
                        changeX = (int) Math.floor(xVelocity);
                    } else {
                        changeX = (int) Math.ceil(xVelocity);
                    }
                    if (yVelocity < 0) {
                        changeY = (int) Math.floor(yVelocity);
                    } else {
                        changeY = (int) Math.ceil(yVelocity);
                    }

                    target.x += changeX;
                    target.y += changeY;
                    Log.v("Test", "Updated velocity: " + vels);
                    targets.replace(i, target);
                    target_velocities.replace(i, vels);
                }
            }


            Iterator<Integer> iterB = bi.iterator();
            while (iterB.hasNext()) {
                int i = iterB.next();
                Point blocker = blockers.get(i);
                if (blocker != null) {
                    ArrayList<Double> vels = blockerVelocities.get(i);
                    double xVelocity = vels.get(0);
                    double yVelocity = vels.get(1);
                    int changeX;
                    int changeY;

                    if (blocker.x + blockerDiameter > screenWidth || blocker.x <= 0) {
                        xVelocity *= -1;
                        vels.set(0, xVelocity);
                    }
                    if (blocker.y + blockerDiameter > screenHeight || blocker.y <= 0) {
                        yVelocity *= -1;
                        vels.set(1, yVelocity);
                    }

                    if (xVelocity < 0) {
                        changeX = (int) Math.floor(xVelocity);
                    } else {
                        changeX = (int) Math.ceil(xVelocity);
                    }
                    if (yVelocity < 0) {
                        changeY = (int) Math.floor(yVelocity);
                    } else {
                        changeY = (int) Math.ceil(yVelocity);
                    }

                    blocker.x += changeX;
                    blocker.y += changeY;
                    blockers.replace(i, blocker);
                    blockerVelocities.replace(i, vels);
                }
            }
        }
    }


    public static void checkForCollisions() {
        if(AccSensor.getRunFlag()) {

            int boatX = (int) boat.x + boatRadius;
            int boatY = (int) boat.y + boatRadius;

            Iterator<Integer> colIter = ti.iterator();
            while (colIter.hasNext()) {

                int i = colIter.next();
                Point target = targets.get(i);
                if (target != null) {
                    ArrayList<Double> vels = target_velocities.get(i);
                    double xVelocity = vels.get(0);
                    double yVelocity = vels.get(1);
                    int centerX = target.x + targetRadius;
                    int centerY = target.y + targetRadius;

                    double distance = Math.sqrt(Math.pow(boat.x - centerX, 2) + Math.pow(boat.y - centerY, 2));

                    if (distance <= boatRadius + targetRadius) {

                        soundPool.play(soundMap.get(TARGET_SOUND_ID), 1, 1, 1, 0, 1f);
                        NumOfHit++;

                        targets.remove(i);
                        target_velocities.remove(i);
                        colIter.remove();


                        if (NumOfHit == 2) {
                            NumOfLive++;
                            NumOfHit = 0;
                        }
                        if (NumOfLive == 10) {

                            stopGame();
                            GameOverDialog(R.string.win);
                        }
                    }
                }
            }

            Iterator<Integer> colIterB = bi.iterator();
            while (colIterB.hasNext()) {
                int i = colIterB.next();
                Point blocker = blockers.get(i);
                if (blocker != null) {
                    ArrayList<Double> vels = blockerVelocities.get(i);
                    int centerX = blocker.x + blockerRadius;
                    int centerY = blocker.y + blockerRadius;

                    double distance = Math.sqrt(Math.pow(boat.x - centerX, 2) + Math.pow(boat.y - centerY, 2));

                    if (distance <= boatRadius + blockerRadius) {

                        soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
                        NumOfLive--;
                        blockers.remove(i);
                        blockerVelocities.remove(i);
                        colIterB.remove();

                        if (NumOfLive == 0) {

                            stopGame();
                            GameOverDialog(R.string.lose);
                        }
                    }
                }
            }
        }
    }

    public static void addThings() {
        if(AccSensor.getRunFlag()) {
            ElapsedTime = System.currentTimeMillis() - CreateTime;

            ElapsedTime = ElapsedTime / 1000;

            if (ElapsedTime % 2 == 0) {
                Log.v("Test", "Added blocker");
                addBlocker();
            }

            if (ElapsedTime % 5 == 0) {
                addTarget();
            }
        }
    }

    int time_count=1;
    String time_count_second;
    Timer T;



    public void t_onClick(View v)
    {
        //this is 'Pause' button click listener
        T.cancel();
    }


    double elapssed_time = 0;
    double previous_system_time = 0;
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

         

        canvas.drawBitmap(bgBitmap, 0, 0, null);

        canvas.drawBitmap(boatBitmap, boat.x, boat.y, null);
//        //onPause
//        if(!AccSensor.getRunFlag()){
//            storedStatus=AccSensor.getRunFlag();
//            pauseTime = System.currentTimeMillis();
//        }
//        if(AccSensor.getRunFlag()) {
//
//            //after pause
//            if(!storedStatus) {
//                pauseDuration = System.currentTimeMillis() - pauseTime;
//                previousTime = previousTime + pauseDuration;
//                storedStatus=true;
//            }
//            //end:after pause
//            //normal run
//            //end:normal run
//            ElapsedTime_count = System.currentTimeMillis() - CreateTime;
//            min = (int) (ElapsedTime_count/ 6000000);
//            sec = (int) (ElapsedTime_count % 6000000);
//
//
//        }
//
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(40);
//        canvas.drawText("Time Used: " + Integer.toString(min) + ":" + Integer.toString(sec), canvas.getWidth() - 350, 40, paint);
////

//        Paint paint = new Paint();
//        canvas.drawPaint(paint);
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(16);
//        canvas.drawText("My Text",0,0, paint);

            Iterator<Integer> targetIter = ti.iterator();
            while (targetIter.hasNext()) {
                int i = targetIter.next();
                if (targets.get(i) != null) {
                    Point point = targets.get(i);
                    canvas.drawBitmap(targetBitmap, point.x, point.y, null);
                }
            }

            Iterator<Integer> blockerIter = bi.iterator();
            while (blockerIter.hasNext()) {
                int i = blockerIter.next();
                if (blockers.get(i) != null) {
                    Point point = blockers.get(i);
                    canvas.drawBitmap(blockerBitmap, point.x, point.y, null);
                }
            }

            addBlood(canvas);
    }

    public static void addBlood(Canvas canvas)
    {

        if(AccSensor.getRunFlag()) {
            for (int i = 0; i < NumOfLive; i++) {
                Point point = life.get(i);
                canvas.drawBitmap(lifeBitmap, point.x, point.y, null);
            }
        }
    }

    public static void addTarget() {

        if(AccSensor.getRunFlag()) {
            int randomX = screenWidth - targetDiameter;
            int randomY = screenHeight - targetDiameter;
            if (NumOfLive > 5 && NumOfTargets > 5) {
                NumOfTargets--;
                int z = NumOfTargets;
                targets.remove(z);
            } else
                NumOfTargets++;
            int i = NumOfTargets;

            Random Gen = new Random();
            int x = Gen.nextInt(randomX);
            int y = Gen.nextInt(randomY);
            Point pos = new Point(x, y);
            targets.put(i, pos);

            Random velGen = new Random();
            double ta1 = velGen.nextDouble() * 360.0;
            double ta2 = velGen.nextDouble() * 360.0;
            ArrayList<Double> vel = new ArrayList<Double>();
            vel.add(Math.sin(ta1));
            vel.add(Math.sin(ta2));
            target_velocities.put(i, vel);

            ti.add(i);
        }
    }

    public static void addBlocker() {

        if(AccSensor.getRunFlag()) {
            int randomX = screenWidth - targetDiameter;
            int randomY = screenHeight - targetDiameter;
            if (NumOfLive < 5 && NumOfBlockers > 5)
                NumOfTargets--;
            else
                NumOfBlockers++;
            int i = NumOfBlockers;

            Random Gen = new Random();
            int x = Gen.nextInt(randomX);
            int y = Gen.nextInt(randomY);
            Point pos = new Point(x, y);
            blockers.put(i, pos);

            Random velGen = new Random();
            double ta1 = velGen.nextDouble() * 360.0;
            double ta2 = velGen.nextDouble() * 360.0;
            ArrayList<Double> vel = new ArrayList<Double>();
            vel.add(Math.sin(ta1));
            vel.add(Math.sin(ta2));
            blockerVelocities.put(i, vel);
            bi.add(i);
        }
    }



    public static void GameOverDialog(int messageId) {

        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(con);
        dialogBuilder.setCancelable(false);


        dialogBuilder.setMessage(res.getString(messageId));
        dialogBuilder.setPositiveButton(R.string.reset_game,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)  {
                        newGame();
                    }
                }
        );

        act.runOnUiThread(
                new Runnable() {
                    public void run() {
                        dialogBuilder.show();
                    }
                }
        );
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                retry = false;
            }
            finally {}
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {}

}