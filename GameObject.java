package com.example.damindu.airgame;

import android.graphics.Rect;

/**
 * Created by Damindu on 2/5/2017.
 */
public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int height;
    protected int width;

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getDx() {
        return dx;
    }

    public int getWidth() {
        return width;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getRectangle(){
        return new Rect(x,y,x+width,y+height);
    }

}
