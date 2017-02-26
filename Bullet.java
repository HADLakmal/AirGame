package com.example.damindu.airgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Damindu on 2/16/2017.
 */
public class Bullet extends GameObject {

    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap res;
    public Bullet(Bitmap res,int x , int y , int w, int h, int s , int numFrames)
    {

        this.x = x;
        this.y = y;

        width = w;
        height = h;

        speed = 8+ (int)(rand.nextDouble()*score/30);

        //cap bullet speed
        if(speed>40) speed =40;

        Bitmap image[] = new Bitmap[numFrames];

        this.res = res;

        for (int i=0; image.length>i; i++)
        {
            image[i] = Bitmap.createBitmap(res,0,i*height,width,height);
        }
        animation.setFrames(image);
        animation.setDelay(100-speed);
    }

    public void update(){
        x+=speed;
        animation.update();
    }

    public void draw(Canvas canvas)
    {
        try {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch (Exception e){

        }
    }

    @Override
    public int getWidth()
    {

        //offset for more realistic collision
        return width-10;
    }

}
