package com.example.damindu.airgame;

import android.graphics.Bitmap;

/**
 * Created by Damindu on 2/5/2017.
 */
public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long stratTime;
    private long delay;
    private boolean playedOnes;

    public void setFrames(Bitmap[] frames){
        this.frames = frames;
        currentFrame = 0;
        stratTime = System.nanoTime();
    }

    public void setDelay(long delay){
        this.delay = delay;
    }
    public void setCurrentFrame(int i){currentFrame=i;}

    public void update(){
        long elapsed = (System.nanoTime()-stratTime)/1000000;
         if (elapsed>delay){
             currentFrame++;
             stratTime = System.nanoTime();
         }
        if (currentFrame== frames.length){
            currentFrame =0;
            playedOnes = true;
        }
    }
    public Bitmap getImage(){

        return frames[currentFrame];
    }
    public int getCurrentFrame(){return currentFrame;}
    public boolean isPlayedOnes(){return playedOnes;}

}
