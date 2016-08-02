package edu.uw.mczhou.shoot;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Maxwhale on 5/6/16.
 */
public class PowerUp {
    public int id; //powerup id
    public Paint paint; //paint color
    public float x; //x cord
    public float y; //y cord
    public float dy; //speed

    public PowerUp(int x, int y) {
        this.id = 1; //because only available currently
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(60);
        this.dy = 16;
        paint.setColor(Color.CYAN);
        this.x = x;
        this.y = y;
    }

}
