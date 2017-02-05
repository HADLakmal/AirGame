package com.example.damindu.airgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import dalvik.system.PathClassLoader;

/**
 * Created by Damindu on 2/1/2017.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    private Background background;
    private PlayerTank playerTank;

    public static final int width = 1300;
    public static final int height = 860;
    public static final int moveSpeed = -5;

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

        //add Background
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grass));
        //iniziate the player
        playerTank = new PlayerTank(BitmapFactory.decodeResource(getResources(),R.drawable.tank),100,100,8);
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
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            if (!playerTank.getPlaying()){
                playerTank.setPlaying(true);
            }
            else{
                playerTank.setDown(true);
            }
            return true;
        }
        if (event.getAction()==MotionEvent.ACTION_UP){
            playerTank.setUp(true);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){

        if (playerTank.getPlaying()) {
            background.update();
            playerTank.update();
        }
    }
    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = getWidth()/(width*1.f);
        final float scaleFactorY = getHeight()/(height*1.f);
        if (canvas!=null){

            final int savedState = canvas.save();
            canvas.scale(scaleFactorX,scaleFactorY);
            background.draw(canvas);
            playerTank.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }


}
