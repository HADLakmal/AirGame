package com.example.damindu.airgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Damindu on 2/18/2017.
 */
public class TopBorders extends GameObject {
    private Bitmap image;

    public TopBorders(Bitmap res,int x, int y, int h){

        height = h;
        width = 20;

        this.x = x;
        this.y = y;

        dx = GamePanel.moveSpeed;
        image = Bitmap.createBitmap(res,0,0,width,height);
    }
    public  void update(){
        x+=dx;
    }

    public void draw(Canvas canvas)
    {
        try {
            canvas.drawBitmap(image,x,y,null);
        }catch (Exception e){}
    }
}
