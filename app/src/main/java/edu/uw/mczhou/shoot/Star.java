package edu.uw.mczhou.shoot;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Maxwhale on 5/11/16.
 */
public class Star {

    public int x; //x cord
    public int y; //y cord
    public Paint paint;
    public int dy; //speed
    public float radius;
    private Random r;

    public Star(int x, int y) {
        this.x = x;
        this.y = y;
        r = new Random();
        this.dy = pickSpeed();
        this.radius = pickSize();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
    }

    private int pickSpeed() {
        int speed = r.nextInt(10);
        if (speed < 7) {
            return 1;
        } else {
            return 2;
        }
    }

    private int pickSize() {
        int size = r.nextInt(10);
        if (size < 7) {
            return 2;
        } else if (size == 7 || size == 8) {
            return 1;
        } else {
            return 3;
        }
    }
}
