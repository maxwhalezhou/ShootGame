package edu.uw.mczhou.shoot;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Maxwhale on 5/5/16.
 */
public class Missile {

    public float x;
    public float y;
    public float dy;
    float length;
    public float width;
    public Paint paint;

    public Missile(float x, float y) {
        this.x = x;
        this.y = y;
        this.dy = 1f;
        this.length = 10f;
        this.width = 3f;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    public String toString() {
        return "x: " + x + ", y: " + y + ", dy: " + dy +  ", length: " + length + ", width: " + width + ", paint: " +
                paint.toString();
    }


}
