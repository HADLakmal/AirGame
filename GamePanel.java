package com.example.damindu.airgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

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
    private long smokeStart;

    private ArrayList<Bullet> bullets;
    private long bulletStrat;

    public static final int width = 2000;
    public static final int height = 1480;
    public static final int moveSpeed = 5;
    Random rand = new Random();

    ArrayList<TopBorders> topBorderses;
    ArrayList<BotBorder> botBorders;

    public int maxBorHeight;
    public int minBorHeight;

    private boolean topDown = true;
    private boolean botDown = true;

    //increase to slow down difficulty
    private int difficulty = 20;

    private int bestScore;

    private boolean newGamestart;


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
        //add bullet
        bullets = new ArrayList<Bullet>();
        bulletStrat = System.nanoTime();

        //Borders init
        topBorderses = new ArrayList<TopBorders>();

        botBorders = new ArrayList<BotBorder>();



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

            //max and min border set
            maxBorHeight = 30+playerTank.getScore()/difficulty;
            //only maximum for half of the screen
            if (maxBorHeight>height/4) maxBorHeight = height/4;
            minBorHeight = 5 + playerTank.getScore()/difficulty;

            //detect collition
            for (int x=0; x<topBorderses.size();x++)
            {
                if (collision(topBorderses.get(x),playerTank))
                {
                    playerTank.setPlaying(false);
                }
            }
            for (int x=0; x<botBorders.size();x++)
            {
                if (collision(botBorders.get(x),playerTank))
                {
                    playerTank.setPlaying(false);
                }
            }
            //update borders
            updateBottomBorder();
            updateTopBorder();

            //bullet adding to update
            long bulletTime = (System.nanoTime()-bulletStrat)/1000000;
            if (bulletTime>(2200-playerTank.getScore()/4))
            {
                //first bullet always going down the middle
                if (bullets.size()==0) bullets.add(new Bullet(BitmapFactory.decodeResource(getResources(),R.drawable.bullet),-10,height/2,72*3,95,playerTank.getScore(),7));
                else
                {

                    bullets.add(new Bullet(BitmapFactory.decodeResource(getResources(),R.drawable.bullet),-10,(int)((rand.nextDouble()*height))-maxBorHeight-20,72*3,95,playerTank.getScore(),7));
                }
                // reset the timer
                bulletStrat = System.nanoTime();
            }
            //loop every bullet check collision and away from the screen
            for (int x =0; bullets.size()>x;x++){
                bullets.get(x).update();
                //Player hit with bullet
                if (collision(bullets.get(x),playerTank)){
                    bullets.remove(x);
                    playerTank.setPlaying(false);
                    break;
                }
                //bullet away from the screen
                if (bullets.get(x).getX()>width){
                    bullets.remove(x);
                }
            }



            //smoke adding to update
            long elapsed = (System.nanoTime()-smokeStart)/1000000;
            if(elapsed>400){
                smokes.add(new Smoke(playerTank.getX()+350,playerTank.getY()+170));
                smokeStart = System.nanoTime();
            }
            for (int i =0; i<smokes.size();i++){
                smokes.get(i).update();
                if(smokes.get(i).getX()>width){
                    smokes.remove(i);
                }

            }
        }
        else {
            newGamestart =false;
            if (!newGamestart)
            newGame();
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

            for (Bullet bullet : bullets)
            {
                bullet.draw(canvas);
            }

            //draw the boders
            for (TopBorders t : topBorderses) t.draw(canvas);
            for (BotBorder b : botBorders) b.draw(canvas);
            text(canvas);

            canvas.restoreToCount(savedState);

        }
        else
        {
            newGamestart = false;
            if (!newGamestart)
            {
                newGame();
            }
        }
    }
    public boolean collision(GameObject bullet , GameObject player)
    {
        if (Rect.intersects(bullet.getRectangle(),player.getRectangle())) return true;
        return false;
    }

    public void updateBottomBorder()
    {


        //every 10 point border up
        if (playerTank.getScore()%100==0) {
            botBorders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brickn),botBorders.get(botBorders.size()-1).getX()-20
                    ,(int)(rand.nextDouble()*maxBorHeight)+(height-250-maxBorHeight)));
        }

        for (int i=0 ; botBorders.size()>i;i++)
        {

            botBorders.get(i).update();
            if (botBorders.get(i).getX()>width)
            {
                botBorders.remove(i);
                if (botBorders.get(botBorders.size()-1).getHeight()>=maxBorHeight)
                {
                    botDown = false;
                }
                if (botBorders.get(botBorders.size()-1).getHeight()<=minBorHeight)
                {
                    botDown = true;
                }
                //add larger height to border
                if (botDown){

                    botBorders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brickn),botBorders.get(botBorders.size()-1).getX()-20,botBorders.get(botBorders.size()-1).getY()+1));
                }
                //add smaller height
                else {

                    botBorders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brickn), botBorders.get(botBorders.size() - 1).getX() - 20, botBorders.get(botBorders.size() - 1).getY() - 1));
                }
            }

        }
    }

    public void updateTopBorder()
    {
        if (playerTank.getScore()%50==0)
        {
            topBorderses.add(new TopBorders(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.brickn),topBorderses.get(topBorderses.size()-1).getX()-20,0,(int)(rand.nextDouble()*(maxBorHeight))+1));
        }

        for (int i=0 ; topBorderses.size()>i;i++)
        {
            topBorderses.get(i).update();
            if (topBorderses.get(i).getX()>width)
            {
                topBorderses.remove(i);
                if (topBorderses.get(topBorderses.size()-1).getHeight()>=maxBorHeight)
                {
                    topDown = false;
                }
                if (topBorderses.get(topBorderses.size()-1).getHeight()<=minBorHeight)
                {
                    topDown = true;
                }
                //add larger height to border
                if (topDown){

                    topBorderses.add(new TopBorders(BitmapFactory.decodeResource(getResources(), R.drawable.brickn),topBorderses.get(topBorderses.size()-1).getX()-20,0
                            ,topBorderses.get(topBorderses.size()-1).getHeight()+1));
                }
                //add smaller height
                else {
                    topBorderses.add(new TopBorders(BitmapFactory.decodeResource(getResources(), R.drawable.brickn), topBorderses.get(topBorderses.size() - 1).getX() - 20, 0
                            , topBorderses.get(topBorderses.size() - 1).getHeight() - 1));

                }
            }

        }

    }
    public void newGame(){
        botBorders.clear();
        topBorderses.clear();
        bullets.clear();
        smokes.clear();

        minBorHeight = 5;
        maxBorHeight = 30;
        if (playerTank.getScore()>bestScore) bestScore = playerTank.getScore();

        playerTank.resetScore();
        playerTank.setY(height/2);



        //top border continous
        for (int x =0 ; x*20<width+20;x++)
        {
            //init top border
            if (x==0){
                topBorderses.add(new TopBorders(BitmapFactory.decodeResource(getResources(),R.drawable.brickn),width,0,10));
            }
            else
            {
                topBorderses.add(new TopBorders(BitmapFactory.decodeResource(getResources(),R.drawable.brickn),width-x*20,0,topBorderses.get(x-1).getHeight()+1));
            }
        }
        //botom border continous
        for (int x =0 ; x*20<width+20;x++)
        {
            //init top border
            if (x==0){
                botBorders.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brickn),width,height-maxBorHeight));
            }
            else
            {
                botBorders.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brickn),width-x*20,botBorders.get(x-1).getY()-1));
            }
        }

        newGamestart = true;
    }
    public void text(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(80);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE : " + playerTank.getScore() * 2, 10, 50, paint);
        canvas.drawText("BEST : "+bestScore,10,150,paint);

        if (!playerTank.getPlaying()&&newGamestart)
        {
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint1.setTextSize(300);
            canvas.drawText("PRESS TO START",width/2-100,height/2,paint1);
        }
    }
}
