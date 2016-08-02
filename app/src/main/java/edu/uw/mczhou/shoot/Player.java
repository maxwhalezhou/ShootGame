package edu.uw.mczhou.shoot;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Maxwhale on 5/5/16.
 */
public class Player {

    public float cx;
    public float cy;
    public float dx;
    public int powerUp;
    public Paint paint;
    public float radius;
    private int lives;

    public Player(float cx, float cy) {
        this.lives = 3;
        this.cx = cx;
        this.cy = cy;
        this.dx = 0;
        this.radius = 40f;
        this.powerUp = 0;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
    }

    public void setX(float cx) {
        this.cx = cx;
    }

    public void subtractLife() {
        this.lives--;
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setPowerUp(int powerUp) {
        this.powerUp = powerUp;
    }
}
