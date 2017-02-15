package com.example.damindu.airgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
/**
 * Created by Damindu on 2/15/2017.
 */
public class Smoke extends GameObject {
    public int r;
    public Smoke(int x, int y){
        r=20;
        super.x=x;
        super.y=y;
    }
    public void update(){
        x-=10;
    }
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x - r, y - r, r, paint);
        canvas.drawCircle(x-r+10,y-r,r,paint);
        //canvas.drawCircle(x-r+4,y-r,r,paint);
    }
}
