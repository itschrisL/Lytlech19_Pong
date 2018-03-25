package edu.up.cs371.lytlech19.lytlech19_pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created on 3/24/2018.
 *
 * @author Chris Lytle
 */

public class Brick {

    // Instance variables
    public int xCenter;
    public int yCenter;
    public int brickWidth;
    public int brickHeight;
    public Paint brickPaint;

    public Brick(int xCord, int yCord){
        // Setting up instance variables
        this.xCenter = xCord;
        this.yCenter = yCord;

        this.brickPaint = new Paint();
        this.brickPaint.setColor(Color.RED);

        brickHeight = 75;
        brickWidth = 30;
    }

    public void drawBrick(Canvas g){
        g.drawRect(xCenter - (brickWidth/2), yCenter - (brickHeight/2), xCenter + (brickWidth/2),
                yCenter + (brickHeight/2),brickPaint);
    }
}
