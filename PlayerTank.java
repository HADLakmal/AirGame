package com.example.damindu.airgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Damindu on 2/5/2017.
 */
public class PlayerTank extends GameObject {

    private Bitmap spriteSheet;
    private int score;
    private double dya;
    private boolean up;
    private boolean down;
    private boolean playing;
    private Animation animation =new Animation();
    private long startTime;

    public PlayerTank(Bitmap res, int w, int h,int numFrame) {
        this.spriteSheet = res;
        x = 100;
        y = GamePanel.width/2;
        dy = 0;
        score = 0;
        width =w;
        height = h;
        //add Tank image into a array
        Bitmap[] image = new Bitmap[numFrame];
        for (int r=0;r<image.length;r++){
            image[r]=Bitmap.createBitmap(spriteSheet,r*width,0,width,height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }
    public void setUp(boolean b){

        up = b;
    }
    public void setDown(boolean b){
        down = b;
    }
    public void update(){
        long elapsed =(System.nanoTime()-startTime)/1000000;
        if (elapsed>100){
            score++;
            startTime = System.nanoTime();
        }
        animation.update();
        if (up){
            dy = (int)(dya-=1.1);

        }
        else if (down){
            dy = (int)(dya+=1.1);
        }
        if (dy>14) dy=14;
        if (dy<-14) dy = -14;

        y+=dy*2;
        dy=0;

    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){
        return score;
    }
    public boolean getPlaying(){
        return playing;

    }
    public void setPlaying(Boolean b){playing = b;}
    public void resetDYA(){dya=0;}
    public void resetScore(){score=0;}
}
