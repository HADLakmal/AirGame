package com.example.damindu.airgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Damindu on 2/1/2017.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    public GamePanel(Context context){
        super(context);

        //add callback to the surfaceHolder to intercept event
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

        //make game panel focusable so it can handle event
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //we can safetly strat the game loop

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        //destroy
        boolean retry = true;
        while (retry){
            try {
                thread.setRunning(false);
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
                retry=false;
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

    public void update(){

    }


}
