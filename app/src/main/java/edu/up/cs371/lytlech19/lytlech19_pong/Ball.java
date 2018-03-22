package edu.up.cs371.lytlech19.lytlech19_pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Defines a bal
 *
 * Created on 3/20/2018.
 * @author Chris Lytle
 */

public class Ball {

    // Instance Variables
    public Paint ballColor; // Set paint color for ball
    public int ballRadius; // Radius of ball
    public int xCount; // counts the number of logical clock ticks in x direction
    public int yCount; // counts the number of logical clock ticks in y direction
    public double xVelocity; // Velocity of ball in x direction
    public double yVelocity; // Velocity of ball in y direction
    public boolean xBackwards = false; // Whether the ball is moving backwards in x direction
    public boolean yBackwards = false; // Whether the ball is moving backwards in y direction

    public Ball(){
        ballInit();
    }

    /**
     *
     */
    public void ballInit(){
        ballColor = new Paint();
        ballColor.setColor(Color.WHITE);
        ballRadius = 30;
        xCount = 100;
        yCount = 100;
        xVelocity = 5.0;
        yVelocity = 5.0;
        xBackwards = false;
        yBackwards = false;
    }

    /**
     * Draw Ball on given canvas based on x and y locations
     * @param g
     * @param xLocation
     * @param yLocation
     */
    public void drawBall(Canvas g, int xLocation, int yLocation){
        // Draw the ball in the correct position.
        g.drawCircle(xLocation, yLocation, ballRadius, ballColor);
    }

    /**
     * Spawns new ball at Random location
     */
    public void spawnNewBallRandom(int surfaceX, int surfaceY){

        //Instance Variables
        Random rando = new Random();

        // Set random numbers for x and y count
        xCount = rando.nextInt(surfaceX);
        yCount = rando.nextInt(surfaceY);

        // Set direction of ball random
        int randInt = rando.nextInt()*2;
        if(randInt == 1){
            xBackwards = false;
        }
        else {
            xBackwards = false;
        }
        randInt = rando.nextInt()*2;
        if(randInt == 1){
            yBackwards = false;
        }
        else {
            yBackwards = false;
        }

        double maxVelocity = 25;
        xVelocity = rando.nextDouble()*maxVelocity;
        yVelocity = rando.nextDouble()*maxVelocity;

    }
}
