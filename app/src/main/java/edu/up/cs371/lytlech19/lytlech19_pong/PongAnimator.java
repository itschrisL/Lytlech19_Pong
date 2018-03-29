package edu.up.cs371.lytlech19.lytlech19_pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * PongAnimator to handle the pong animation.  Calculates and updates locations of objects being
 * drawn on the playing field.
 *
 * Is passed through AnimationSurface which handles the needed thread
 *
 * Implements Animator interface
 *
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
    private int xOpponentPaddle = 150; // X location of opponent paddle
    private int yOpponentPaddle = 200; // Y location of Opponent paddle
    private int[] scores = new int[2]; // score count
    private ArrayList<Ball> ballsInPlay = new ArrayList<>(); // ArrayList of balls currently in play
    private ArrayList<Ball> ballsToRemove = new ArrayList<>(); // ArrayList of balls that need to be removed
    private ArrayList<Brick> bricksInPlay = new ArrayList<>(); // ArrayList of bricks in play
    private ArrayList<Brick> bricksToRemove = new ArrayList<>(); // ArrayList of bricks to remove
    private TextView textView1 = null;
    private TextView textView2 = null;
    private String dispString1; // String to be passed to text view display
    private String dispString2;
    private Ball targetBall; // Saved object for target ball, The ball the AI is trying to hit.  Used for testing purposes
    private Paint defaultBallColor = new Paint(); // Default ball color to reset ball color
    private Paint defaultTargetBallColor = new Paint(); // Default target ball color

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
        return Color.rgb(0, 45, 0);
    }

    /**
     * Action to perform on clock tick
     *
     * @param g the graphics object on which to draw
     */
    public void tick(Canvas g) {

        // Set default colors
        defaultBallColor.setColor(Color.WHITE);
        defaultTargetBallColor.setColor(Color.YELLOW);

        // Get width of surface width and height
        surfaceWidth = g.getWidth();
        surfaceHeight = g.getHeight();

        // Draws Paddle
        drawPaddle(g);

        // Draw Opponent paddle
        xOpponentPaddle = surfaceWidth - 100;
        drawOpponentPaddle(g);
        // Draw Walls
        drawWalls(g);

        // Draw Balls in play
        drawBallsInPlay(g);

        // Draw Bricks in play
        for(Brick Br : bricksInPlay){
            Br.drawBrick(g);
        }

        // Call Check if Brick Hit
        checkIfBrickHit();

        // Remove Balls
        for(Ball B : ballsToRemove){
            ballsInPlay.remove(B);
        }

        // Remove Bricks
        for(Brick Br : bricksToRemove){
            bricksInPlay.remove(Br);
        }

        // Set the target ball and change it's color
        if(ballsInPlay.size() > 0){
            targetBall = ballsInPlay.get(0);
        }
        // Update the Display strings
        updateDisplayString();
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
    }

    /**
     * Spawn new ball at random location
     */
    public void addBall(){
        Ball tempBall = new Ball();
        tempBall.spawnNewBallRandom(surfaceWidth, surfaceHeight);
        ballsInPlay.add(tempBall);
    }

    /**
     * Method to add brick to play
     */
    public void addBrick(){
        // Instance Variables
        Random random = new Random();
        int xCord;
        int yCord;
        xCord = random.nextInt(surfaceWidth);
        while (xCord <= 200 && xCord >= surfaceWidth - 200) {
            xCord = random.nextInt() * surfaceWidth;
        }
        yCord = random.nextInt(surfaceHeight);
        while (yCord <= 200 && yCord >= surfaceHeight - 200) {
            yCord = random.nextInt() * surfaceHeight;
        }

        Brick addBrick =  new Brick(xCord, yCord);
        bricksInPlay.add(addBrick);
    }

    /**
     * Method to draw the balls in play
     * @param g
     */
    private void drawBallsInPlay(Canvas g){
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

            // If ball is not the target ball but has the Color of the target ball, Change it
            if((B.ballColor.equals(defaultTargetBallColor) && (!(B.equals(targetBall))))){
                B.setBallColor(defaultBallColor);
            }

            // If target B is target ball, change its color
            if(B.equals(targetBall)){
                B.ballColor = defaultTargetBallColor;
            }
            B.drawBall(g, xNum, yNum);
        }
    }

    /**
     * Draws a small boarder around the edge.
     * @param g
     */
    private void drawWalls(Canvas g){
        // Instance Variables
        Paint wallPaint = new Paint();
        Paint goalPaint = new Paint();
        wallPaint.setColor(Color.rgb(10, 50, 130));
        goalPaint.setColor(Color.DKGRAY);

        // Draw Walls
        g.drawRect(0, 0, wallWidth, surfaceHeight, goalPaint); // Draw Human Goal
        g.drawRect(0, 0, surfaceWidth, wallWidth, wallPaint); // Draw regular wall
        g.drawRect(surfaceWidth - wallWidth, 0, surfaceWidth, surfaceHeight, goalPaint); // Draw opponent wall
        g.drawRect(0, surfaceHeight - wallWidth, surfaceWidth, surfaceHeight, wallPaint); // Draw regular wall
    }

    /**
     * Method to draw Paddle
     * @param g
     */
    private void drawPaddle(Canvas g){
        xPaddle = 100;
        Paint paddleColor = new Paint();
        paddleColor.setColor(Color.BLUE);
        if((yPaddle + (paddleHeight/2) > surfaceHeight - wallWidth)){
            yPaddle = surfaceHeight - wallWidth - (paddleHeight/2);
        }
        else if((yPaddle - (paddleHeight/2) < wallWidth)){
            yPaddle = wallWidth + (paddleHeight/2);
        }
        g.drawRect(xPaddle, yPaddle - (paddleHeight/2), xPaddle + paddleWidth,
                yPaddle + (paddleHeight/2), paddleColor);

        //if((yPaddle + (paddleHeight/2) < surfaceHeight - wallWidth) &&
        //        (yPaddle - (paddleHeight/2) > wallWidth)){
        //    g.drawRect(xPaddle, yPaddle - (paddleHeight/2), xPaddle + paddleWidth,
        //            yPaddle + (paddleHeight/2), paddleColor);
        //}
    }

    /**
     * Method to draw Opponent Paddle
     * @param g
     */
    private void drawOpponentPaddle(Canvas g){
        Paint paddleColor = new Paint();
        paddleColor.setColor(Color.BLUE);

        // TODO MOVE here later
        opponentStrategy();

        // Check if coordinates are within playing field
        if((yOpponentPaddle + (paddleHeight/2) < surfaceHeight - wallWidth) &&
                (yOpponentPaddle - (paddleHeight/2) > wallWidth)){
            // Draw Paddle
            g.drawRect(xOpponentPaddle, yOpponentPaddle - (paddleHeight/2),
                    xOpponentPaddle + paddleWidth, yOpponentPaddle + (paddleHeight/2), paddleColor);
        }
        else {
            // Do Nothing
        }
    }

    /**
     * Method for the opponent strategy, Updates opponent's y paddle location.
     * Targets the yellow ball or the target ball.
     * Has a set speed when moving up and down.
     */
    private void opponentStrategy(){
        int yChange = 0;
        int ySpeed = 20;
        Ball targetBall = null;
        int tempX = 0;
        if(ballsInPlay.size() == 0){
            yOpponentPaddle = surfaceHeight/2;
        }
        else {
            // Default set target ball to first ball in ballsInPlay
            targetBall = ballsInPlay.get(0);
        }
        for (Ball B : ballsInPlay){
            if(B.xCount > tempX){
                targetBall = B;
            }
        }
        if(targetBall != null){
            if(targetBall.yCount > yOpponentPaddle){
                //yOpponentPaddle++;
                yOpponentPaddle = yOpponentPaddle - ySpeed;
            }
            else {
                //yOpponentPaddle--;
                yOpponentPaddle = yOpponentPaddle + ySpeed;
            }
        }
    }

    /**
     * Method to check if ball has hit a wall based on the x and y locations.
     * Updates directions of ball accordingly
     * @param xLocation
     * @param yLocation
     */
    private void checkIfWallHit(Ball ball, int xLocation, int yLocation){
        // Checks if ball hits location of wall
        // Check if ball hit right wall
        if(xLocation + ball.ballRadius > surfaceWidth - wallWidth){
            ballsToRemove.add(ball);
            scores[0]++;
        }
        // Check if ball hit left wall, reset ball
        if(xLocation - ball.ballRadius < 0){
            ballsToRemove.add(ball);
            scores[1]++;
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
    private void checkIfPaddleHit(Ball ball, int xLocation, int yLocation){
        if((xLocation - ball.ballRadius < (xPaddle + paddleWidth))&&
                (yLocation - ball.ballRadius > yPaddle - (paddleHeight/2))&&
                (yLocation + ball.ballRadius < yPaddle + (paddleHeight/2)))
        {
            ball.xBackwards = false;
        }
        if((xLocation - ball.ballRadius < (xOpponentPaddle + paddleWidth))&&
                (yLocation - ball.ballRadius > yOpponentPaddle - (paddleHeight/2))&&
                (yLocation + ball.ballRadius < yOpponentPaddle + (paddleHeight/2)))
        {
            ball.xBackwards = true;
        }
    }

    /**
     * Method to check if brick has been hit by any ball
     */
    private void checkIfBrickHit(){
        for(Ball B : ballsInPlay){
            for (Brick Br : bricksInPlay){

                // Gets
                int rptBall = B.xCount + B.ballRadius; // X location of right of ball
                int lptBall = B.xCount - B.ballRadius; // X location of left of ball
                int tptBall = B.yCount - B.ballRadius; // Y location of top of ball
                int bptBall = B.yCount + B.ballRadius; // Y location of bottom of ball

                int rptBrick = Br.xCenter + (Br.brickWidth/2); // X location of right of brick
                int lptBrick = Br.xCenter - (Br.brickWidth/2); // X location of left of brick
                int tptBrick = Br.yCenter - (Br.brickHeight/2); // Y location of top of brick
                int bptBrick = Br.yCenter + (Br.brickHeight/2); // Y location of bottom of brick

                // Check right of ball
                if(rptBall <= rptBrick && rptBall >= lptBrick ){
                    // Check left of ball
                    if(lptBall <= rptBrick && lptBall >= lptBrick){
                        // Check top of ball
                        if(tptBall >= tptBrick && tptBall <= bptBrick){
                            // Check bottom of ball
                            if(bptBall >= tptBrick && bptBall <= bptBrick){
                                // Removes brick from ArrayList
                                bricksInPlay.remove(Br);
                                // Inverts X direction of ball.
                                if(B.xBackwards){
                                    B.xBackwards = false;
                                }
                                else {
                                    B.xBackwards = true;
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Set TestViews so that they can be updated by the animator
     * @param tv1
     */
    public void setTextViews(TextView tv1){
        this.textView1 = tv1;
    }

    /**
     * Send Updated Strings to text views
     */
    public void updateDisplayString(){
        // Check if TextViews are null, Should not happen
        if(textView1 != null && textView2 != null){
            // instance Variables
            String displayString1;
            String displayString2;

            String coordinateString;
            String velocityString;
            if(ballsInPlay.size() > 0){
                Ball ball = ballsInPlay.get(0);
                coordinateString = ball.coordinatesTOString();
                velocityString = ball.velocitiesTOString();
            }
            else {
                // Default Null Values
                coordinateString = "X : NULL, Y : NULL";
                velocityString = "X Velocity : NULL, Y Velocity : NULL";
            }

            String scoresString = scoreToString();

            displayString1 = coordinateString + ":" + velocityString;
            displayString2 = scoresString;

            dispString1 = displayString1;
            dispString2 = displayString2;

            //textView1.setText(displayString1);
            //textView2.setText(displayString2);
        }
    }

    /**
     * Converts Scores to a String
     * @return
     */
    public String scoreToString(){
        return "Score [" + scores[0] + ":" + scores[1] + "];";
    }

    public String getDispString1() {
        return dispString1;
    }
}

