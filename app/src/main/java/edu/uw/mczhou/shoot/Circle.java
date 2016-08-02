package edu.uw.mczhou.shoot;


import android.graphics.Color;

import java.util.Random;
import android.graphics.Paint;

/**
 * Created by Maxwhale on 5/5/16.
 */
public class Circle {

    private static final int RED = Color.rgb(255,20,147);
    private static final int YELLOW = Color.rgb(255,165,0);
    private static final int BLUE = Color.rgb(0,191,255);
    private static final int GREEN = Color.rgb(0,250,154);

    public float cx; //center x
    public float cy; //center y
    public float radius; //radius
    public Paint paint; //color of circle
    public int health; //health of circle
    private Random r;
    public float dy; //speed of circle


    public Circle(float cx, float cy) {
        r = new Random();
        this.cy = cy;
        this.radius = 30f;
        if(cx < radius) {
            this.cx = cx + radius;
        } else {
            this.cx = cx;
        }
        this.paint = chooseColor();
        if(paint.getColor() ==  RED) {
            this.health = 1;
        } else if (paint.getColor() == YELLOW) {
            this.health = 2;
        } else if (paint.getColor() == BLUE) {
            this.health = 3;
        } else if (paint.getColor() == GREEN){
            this.health = 4;
        }
        this.dy = getSpeed();
    }

    public float getRadius() {
        return this.radius;
    }

    public Paint getColor() {
        return this.paint;
    }

    public int getHealth() {
        return health;
    }

    public float getX() {
        return this.cx;
    }

    public float getY() {
        return this.cy;
    }

    public void setX(float cx) {
        this.cx = cx;
    }

    public void setY(float cy) {
        this.cy = cy;
    }


    private Paint chooseColor() {
        int num = r.nextInt(19);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(num < 11) {
            paint.setColor(RED); //red
            return paint;
        } else if (num < 15 && num >= 11) {
            paint.setColor(YELLOW); //yellow
            return paint;
        } else if (num == 15 || num == 16) {
            paint.setColor(BLUE); //cyan
            return paint;
        } else {
            paint.setColor(GREEN); //green
            return paint;
        }
    }

    private int getSpeed() {
        if(health == 1) {
            return 8;
        } else if(health == 2) {
            return 9;
        } else if(health == 3) {
            return 11;
        } else if (health == 4) {
            return 13;
        } else {
            return 0;
        }
    }
}