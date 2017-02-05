package com.example.damindu.airgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Damindu on 2/4/2017.
 */
public class Background {

    private Bitmap image;
    private int x,y,dx;
    public Background(Bitmap image){
        this.image=image;
        dx = GamePanel.moveSpeed;
    }
    public void update(){

        x+=dx;
        if (x<-GamePanel.width) x=0;

    }
    public void draw(Canvas canvas){

        canvas.drawBitmap(image,x,y,null);
        if (x<0) canvas.drawBitmap(image,x+GamePanel.width,y,null);
    }


}
