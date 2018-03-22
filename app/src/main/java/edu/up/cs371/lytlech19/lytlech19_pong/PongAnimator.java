package edu.up.cs371.lytlech19.lytlech19_pong;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created on 3/19/2018.
 * @author Chris Lytle
 */

public class PongAnimator implements edu.up.cs371.lytlech19.lytlech19_pong.Animator {

    // instance variables
    private int surfaceWidth; // Width of Surface
    private int surfaceHeight; // Height of Surface
    private int wallWidth = 25; // Width of each wall
    private int paddleHeight = 175; // Height of Paddle
    private int paddleWidth = 25; // Width of Paddle
    private int xPaddle = 150; // X location of paddle
    private int yPaddle = 200; // Y location of Paddle
    private ArrayList<Ball> ballsInPlay = new ArrayList<>();
    private ArrayList<Ball> ballsToRemove = new ArrayList<>();

    /**
     * Interval between animation frames: .03 seconds (i.e., about 33 times
     * per second).
     *
     * @return the time interval between frames, in milliseconds.
     */
    public int interval() {
        return 30;
    }

    /**
     * The background color: a light blue.
     *
     * @return the background color onto which we will draw the image.
     */
    public int backgroundColor() {
        // create/return the background color
        return Color.rgb(0, 95, 0);
    }

    /**
     * Action to perform on clock tick
     *
     * @param g the graphics object on which to draw
     */
    public void tick(Canvas g) {

        surfaceWidth = g.getWidth();
        surfaceHeight = g.getHeight();

        // Draws Paddle
        drawPaddle(g);

        // Draw Walls
        drawWalls(g);

        for(Ball B : ballsInPlay){
            // Updates counts of yCount and xCount
            if(B.yBackwards){B.yCount--;}
            else{B.yCount++;}

            if(B.xBackwards){B.xCount--;}
            else {B.xCount++;}

            // xNum and yNum are the x and y locations based on the surface width and surface height
            int xNum = (int) (B.xCount*B.xVelocity)%surfaceWidth;
            int yNum = (int) (B.yCount*B.yVelocity)%surfaceHeight;

            // Call method checkBallHitWall
            checkIfWallHit(B, xNum, yNum);

            // Call method checkIfPaddleHit
            checkIfPaddleHit(B, xNum, yNum);

            B.drawBall(g, xNum, yNum);
        }

        for(Ball B : ballsToRemove){
            ballsInPlay.remove(B);
        }
    }

    /**
     * Tells that we never pause.
     *
     * @return indication of whether to pause
     */
    public boolean doPause() {
        return false;
    }

    /**
     * Tells that we never stop the animation.
     *
     * @return indication of whether to quit.
     */
    public boolean doQuit() {
        return false;
    }

    /**
     * reverse the ball's direction when the screen is tapped
     */
    public void onTouch(MotionEvent event) {
        // Change paddle's Y dimensions based on the Y location of touch.
        yPaddle = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            addBall(true);
        }
    }

    /**
     * Spawn new ball at random
     * @param random
     */
    public void addBall(Boolean random){
        Ball tempBall = new Ball();
        tempBall.spawnNewBallRandom(surfaceWidth, surfaceHeight);
        ballsInPlay.add(tempBall);
    }

    /**
     * Draws a small boarder around the edge.
     * @param g
     */
    public void drawWalls(Canvas g){
        // Instance Variables
        Paint wallPaint = new Paint();
        Paint goalPaint = new Paint();
        wallPaint.setColor(Color.GREEN);
        goalPaint.setColor(0x999900);

        // Draw Walls
        g.drawRect(0, 0, wallWidth, surfaceHeight, goalPaint);
        g.drawRect(0, 0, surfaceWidth, wallWidth, wallPaint);
        g.drawRect(surfaceWidth - wallWidth, 0, surfaceWidth, surfaceHeight, wallPaint);
        g.drawRect(0, surfaceHeight - wallWidth, surfaceWidth, surfaceHeight, wallPaint);
    }

    /**
     * Method to draw Paddle
     * @param g
     */
    public void drawPaddle(Canvas g){
        xPaddle = 100;
        Paint paddleColor = new Paint();
        paddleColor.setColor(Color.BLUE);
        g.drawRect(xPaddle, yPaddle - (paddleHeight/2), xPaddle + paddleWidth,
                yPaddle + (paddleHeight/2), paddleColor);
    }

    /**
     * Method to check if ball has hit a wall based on the x and y locations.
     * Updates directions of ball accordingly
     * @param xLocation
     * @param yLocation
     */
    public void checkIfWallHit(Ball ball, int xLocation, int yLocation){
        // Checks if ball hits location of wall
        // Check if ball hit right wall
        if(xLocation + ball.ballRadius > surfaceWidth - wallWidth){
            ball.xBackwards = true;
        }
        // Check if ball hit left wall, reset ball
        if(xLocation - ball.ballRadius < 0){
            ballsToRemove.add(ball);
        }
        // Check if ball hit top bottom wall
        if(yLocation + ball.ballRadius > surfaceHeight - wallWidth){
            ball.yBackwards = true;
        }
        // Check if ball hit top wall
        if(yLocation - ball.ballRadius < wallWidth){
            ball.yBackwards = false;
        }
    }

    /**
     * Method to check if paddle is hit based on the given x and y locations
     * Updates valuables accordingly
     * @param xLocation
     * @param yLocation
     */
    public void checkIfPaddleHit(Ball ball, int xLocation, int yLocation){
        if((xLocation - ball.ballRadius < (xPaddle + paddleWidth))&&
                (yLocation - ball.ballRadius > yPaddle - (paddleHeight/2))&&
                (yLocation + ball.ballRadius < yPaddle + (paddleHeight/2)))
        {
            ball.xBackwards = false;
        }
    }
}

