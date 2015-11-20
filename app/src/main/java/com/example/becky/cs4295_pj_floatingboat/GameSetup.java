package com.example.becky.cs4295_pj_floatingboat;

/**
 * Created by Becky on 16/11/2015.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameSetup extends Thread {

    public SurfaceHolder surfaceHolder;
    public boolean running = true;
    public Canvas canvas;
    public Context context;
    public GameView gameView;

    public GameSetup(SurfaceHolder holder, Context ctxt, GameView aView)
    {
        surfaceHolder = holder;
        context = ctxt;
        gameView = aView;
        setName("MiniThread");
    }

    public void setRunning(boolean isRunning)
    {
        running = isRunning;
    }

    @Override
    public void run()
    {
        Canvas canvas = null;
        long previousFrameTime = System.currentTimeMillis();

        while (running)
        {
            try
            {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized(surfaceHolder)
                {
                    long currentTime = System.currentTimeMillis();
                    double elapsedTimeMS = currentTime - previousFrameTime;
                    GameView.ElapsedTime += elapsedTimeMS / 1000.0;
                    previousFrameTime = currentTime;
                }
            }
            finally
            {
                if (canvas != null)
                    surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
