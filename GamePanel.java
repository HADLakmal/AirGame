package com.example.damindu.airgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import dalvik.system.PathClassLoader;

/**
 * Created by Damindu on 2/1/2017.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    private Background background;
    private PlayerTank playerTank;
    private ArrayList<Smoke> smokes;

    public static final int width = 1300;
    public static final int height = 860;
    public static final int moveSpeed = -5;
    private long smokeStart;

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
        playerTank = new PlayerTank(BitmapFactory.decodeResource(getResources(),R.drawable.tank),285,300,8);
        //add smoke
        smokes = new ArrayList<Smoke>();
        smokeStart = System.nanoTime();
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
        int counter = 0;
        while (retry && counter<1000){
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry=false;
            }catch (InterruptedException e){
                e.printStackTrace();

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
                playerTank.setUp(true);
            }
            return true;
        }
        if (event.getAction()==MotionEvent.ACTION_UP){
            playerTank.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){

        if (playerTank.getPlaying()) {
            background.update();
            playerTank.update();

            //smoke adding to update
            long elapsed = (System.nanoTime()-smokeStart)/1000000;
            if(elapsed>120){
                smokes.add(new Smoke(playerTank.getX(),playerTank.getY()+10));
                smokeStart = System.nanoTime();
            }
            for (int i =0; i<smokes.size();i++){
                smokes.get(i).update();
                if(smokes.get(i).getX()<-10){
                    smokes.remove(i);
                }

            }
        }
    }
    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = getWidth()/(width*1.f);
        final float scaleFactorY = getHeight()/(height*1.f);
        if (canvas!=null){

            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);

            playerTank.draw(canvas);

            for(Smoke s : smokes){
                s.draw(canvas);
            }

            canvas.restoreToCount(savedState);

        }
    }


}
