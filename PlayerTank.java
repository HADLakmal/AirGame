package com.example.damindu.airgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Damindu on 2/5/2017.
 */
public class PlayerTank extends GameObject {

    private Bitmap spriteSheet;
    private int score;
    private boolean up;
    private boolean down;
    private boolean playing;
    private Animation animation =new Animation();
    private long startTime;

    public PlayerTank(Bitmap res, int w, int h,int numFrame) {
        this.spriteSheet = res;
        x = 100;
        y = GamePanel.height/2-100;
        dy = 0;
        score = 0;
        width =w;
        height = h;
        //add Tank image into a array
        Bitmap[] image = new Bitmap[numFrame];
        for (int r=0;r<image.length;r++){
            image[r]=Bitmap.createBitmap(spriteSheet,0,r*height,width,height);
        }
        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();
    }
    public void setUp(boolean b){

       up = b;
    }
    public void setDown(boolean b)
    {

        down =b;

    }
    public void update(){
        long elapsed =(System.nanoTime()-startTime)/1000000;
        if (elapsed>100){
            score++;
            startTime = System.nanoTime();
        }
        animation.update();
        if (up) dy+=1;
        if (down) dy+=1;

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
    public void resetDYA(){dy=0;}
    public void resetScore(){score=0;}

    @Override
    public Rect getRectangle(){

        return new Rect(x-20,y+40,x+width-20,y+width-20);
    }
}
