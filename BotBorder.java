package com.example.damindu.airgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Damindu on 2/18/2017.
 */
public class BotBorder extends GameObject
{
    private Bitmap image;

    public BotBorder(Bitmap image, int x,int y){

        this.x=x;
        this.y =y;

        width = 20;
        height = 400;
        dx= GamePanel.moveSpeed;
        this.image = Bitmap.createBitmap(image,0,0,width,height);
    }
    public  void  update(){
        x+=dx;
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image,x,y,null);
    }
}
