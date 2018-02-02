import javax.swing.*;
import java.awt.*;

/**
 * @author Logan Karstetter
 * Date: 01/26/2018
 */
public class PongPanel extends JPanel implements Runnable
{
    /** The width of the PongPanel */
    public static final int PWIDTH = 700;
    /** The height of the PongPanel */
    public static final int PHEIGHT = 400;

    /** The thread that runs the game loop */
    private Thread animator;
    /** Determines if the animator thread is running */
    private volatile boolean isRunning = false;
    /** Determines if the game is paused */
    private volatile boolean isPaused = false;
    /** Determines if the game has ended */
    private volatile boolean gameOver = false;

    /** The desired FPS/UPS */
    private int FPS;
    /** The amount of time allocated for each cycle of the game loop (in nanos) */
    private long loopPeriod;
    /** The time the game started (in nanos) */
    private long gameStartTime;
    /** The amount of time spent playing the game (in secs) */
    private int timeSpentInGame;

    /** The max number of times the animator thread will loop without sleeping
     * before it is forced to sleep/yield to let other threads execute */
    private static final int NUM_DELAYS_FOR_YIELD = 16;
    /** The max number of frames than can be skipped before the game is rendered */
    private static final int MAX_SKIPPED_FRAMES = 5;

    /** The graphics used to double buffer/render off-screen */
    private Graphics dbGraphics;
    /** The image created/rendered off-screen */
    private Image dbImage;

    /** The font used to display messages to the user */
    private Font pongFont;
    /** The font metrics used to help render the font messages */
    private FontMetrics fontMetrics;
    /** The color used to display the font/messages */
    private Color translucentWhite;

    /** The paddle on the left side of the screen */
    private Paddle leftPaddle;
    /** The paddle on the right side of the screen */
    private Paddle rightPaddle;
    /** The ball that bounces around the screen */
    private Ball ball;

    /** The KeyManager that handles KeyEvents for this PongPanel */
    private KeyManager keyManager;

    /** The score for the left paddle (player 1) */
    private int leftScore;
    /** The score for the left paddle (player 2) */
    private int rightScore;

    /**
     * Create a new PongPanel for playing Pong. The panel is responsible for running the game loop
     * which updates, renders, and draws the game at the desired FPS/UPS.
     */
    public PongPanel(int FPS)
    {
        //Get the FPS and calculate the period
        this.FPS = FPS;
        loopPeriod = ((long) 1000.0/FPS) * 1000000L; //ms -> nanos, could just be 1bill/FPS

        //Set the background color and size of the PongPanel
        setDoubleBuffered(false);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        //Create the font and color
        pongFont = new Font("", Font.PLAIN, 20);
        fontMetrics = this.getFontMetrics(pongFont);
        translucentWhite = new Color(255, 255, 255, 200);

        //Set the initial scores
        leftScore = 0;
        rightScore = 0;

        //Request focus to the panel so it can receive key events
        setFocusable(true);
        requestFocus();

        //Create and add the KeyManager to receive/direct concurrent key events
        keyManager = new KeyManager(this);
        addKeyListener(keyManager);

        //Create the paddles and ball
        leftPaddle = new Paddle(this, keyManager, true);
        rightPaddle = new Paddle(this, keyManager, false);
        ball = new Ball(this, leftPaddle, rightPaddle);
    }

    /**
     * Notifies this component that it now has a parent component.
     * This method informs the PongPanel that it has been added to a
     * parent container such as a JFrame. Once notified it starts the
     * game. This prevents the game starting before the user can see it.
     */
    public void addNotify()
    {
        super.addNotify();
        startGame();
    }

    /**
     * Start the game (initialize/start the animator thread).
     */
    private void startGame()
    {
        //Check that the animator thread is initialized
        if (animator == null || !isRunning)
        {
            //Create/start the animator
            animator = new Thread(this);
            animator.start();
        }
    }

    /**
     * Pause the game.
     */
    public void pauseGame()
    {
        isPaused = true;
    }

    /**
     * Resume the game.
     */
    public void resumeGame()
    {
        isPaused = false;
    }

    /**
     * Stop the game, set isRunning to false.
     */
    public void stopGame()
    {
        isRunning = false;
    }

    /**
     * Repeatably update, render, paint, and sleep such that the game loop takes close to the amount of
     * time allotted by the desired FPS (loopPeriod).
     */
    public void run()
    {
        //The time before the current loop/cycle begins
        long beforeTime;
        //The time after the gameUpdate, gameRender, and paintScreen method calls
        long afterTime;
        //The time taken to execute the gameUpdate, gameRender, and paintScreen methods
        long timeDifference;

        //The amount of time remaining in the loopPeriod that the thread can sleep for
        long sleepTime;
        //The amount of time the thread overslept
        long overSleepTime = 0L;

        //The number of times the thread has looped/cycled without sleeping (methods take too long)
        int numDelays = 0;
        //The total amount of excess time the methods took to perform, overTime = actual - loopPeriod
        long overTime = 0L; //excess

        //Get the time before the first loop
        gameStartTime = System.nanoTime();
        beforeTime = gameStartTime;

        //Game loop
        isRunning = true;
        while (isRunning)
        {
            gameUpdate(); //Update the game
            gameRender(); //Render to the buffer
            paintScreen(); //Draw the buffer to the screen (active rendering)

            //Get the time after the methods execute
            afterTime = System.nanoTime();
            timeDifference = afterTime - beforeTime; //The time it took to update, render, and paint

            //Calculate how much time is left for sleeping in this loopPeriod (1000000000/FPS)
            sleepTime = (loopPeriod - timeDifference) - overSleepTime;

            //Sleep
            if (sleepTime > 0) //There is time left in the loopPeriod
            {
                try
                {
                    Thread.sleep(sleepTime/1000000L); //nano -> ms
                }
                catch (InterruptedException e)
                {
                    //Do nothing
                }
                //Check if the animator overslept, overSleepTime will be deducted from the next sleepTime
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            }
            else //If we didn't get a chance to sleep this loopPeriod (sleepTime <= 0)
            {
                overTime = overTime - sleepTime; //Store the excess time (- because sleepTime is <= 0)
                overSleepTime = 0L; //Reset the oversleep time

                //See if the animator thread needs to yield
                if (++numDelays >= NUM_DELAYS_FOR_YIELD) //(it hasn't slept for NUM_DELAYS_FOR_YIELD cycles)
                {
                    Thread.yield();
                    numDelays = 0;
                }
            }

            //Get the beforeTime for the next cycle
            beforeTime = System.nanoTime();

            //If rendering and animation are taking too long, update the game without rendering it
            //This will get the UPS closer to the desired FPS
            int skips = 0;
            while ((overTime > loopPeriod) && (skips < MAX_SKIPPED_FRAMES))
            {
                //Update x times without rendering, won't be noticeable if MAX_SKIPPED_FRAMES is small
                overTime = overTime - loopPeriod;
                gameUpdate();
                skips++;
            }
        }

        //Running is false, so exit
        System.exit(0);
    }

    /**
     * Update the game elements as long as the game is not paused or over.
     */
    private void gameUpdate()
    {
        if (!gameOver)
        {
            //Update the keyManager and paddles
            keyManager.update();
            leftPaddle.update();
            rightPaddle.update();

            //Move the ball, but only if the game is not paused
            if (!isPaused)
            {
                //Update the ball
                ball.update();
            }

        }
    }

    /**
     * Render the game using double buffering. If it does not already exist, this
     * method creates an Image the size of the PongPanel and draws to it offscreen.
     * Drawing offscreen prevents flickering and then allows the paintScreen() method
     * to draw the entire screen as an image rather than in layers.
     */
    private void gameRender()
    {
        //If the double buffered image has not been created yet
        if (dbImage == null)
        {
            //Make an image to fit the panel
            dbImage = createImage(PWIDTH, PHEIGHT);
            if (dbImage == null)
            {
                return;
            }
            else
            {
                //Get the graphics context to draw the dbImage offscreen
                dbGraphics = dbImage.getGraphics();
            }
        }

        //Now that dbImage is created, clear the existing image/background
        dbGraphics.setColor(Color.BLACK);
        dbGraphics.fillRect(0, 0, PWIDTH, PHEIGHT);
        //Draw a white line down the center of the panel
        dbGraphics.setColor(Color.WHITE);
        dbGraphics.fillRect(PWIDTH/2, 0, 2, PHEIGHT);

        //Draw the game elements
        leftPaddle.draw(dbGraphics);
        rightPaddle.draw(dbGraphics);
        ball.draw(dbGraphics);

        //Print the game stats
        printStats(dbGraphics);
    }

    /**
     * Actively render/draw the dbImage (created in gameRender()) onto the screen/PongPanel.
     */
    private void paintScreen()
    {
        Graphics g;
        try
        {
            //Get the graphics context from the PongPanel
            g = this.getGraphics();

            if ((g != null) && (dbImage != null))
            {
                //Draw the game screen as an entire image
                g.drawImage(dbImage, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync(); //Sync the display (only applies to odd systems)
            g.dispose();
        }
        catch (NullPointerException e)
        {
            System.out.println("Graphics context error: " + e);
        }
    }

    /**
     * Print the game statistics onto the screen/PongPanel.
     */
    private void printStats(Graphics dbGraphics)
    {
        //Set the font and color
        dbGraphics.setFont(pongFont);
        dbGraphics.setColor(translucentWhite);

        dbGraphics.drawString("Score - " + leftScore + "/5", 5, PHEIGHT - 5);
        dbGraphics.drawString("Score - " + rightScore + "/5", PWIDTH - 115, PHEIGHT - 5);

        //Calculate the time playing as long as the game isn't over
        if (!gameOver)
        {
            timeSpentInGame = (int) ((System.nanoTime() - gameStartTime)/1000000000L);  // ns --> secs
            //Write out the time spent in game
            dbGraphics.drawString("Game time - " + timeSpentInGame, 5, 20);
        }
        else
        {
            dbGraphics.drawString("Game Over! Time - " + timeSpentInGame, 5, 20);
            dbGraphics.drawString("Created by - Logan Karstetter", 5, 45);
        }
    }

    /**
     * Increment the left score (player 1). The game ends once a player
     * has scored five times.
     */
    public void leftScored()
    {
        //Increment the score
        leftScore = leftScore + 1;
        if (leftScore == 5)
        {
            gameOver = true;
        }
    }

    /**
     * Increment the right score (player 2). The game ends once a player
     * has scored five times.
     */
    public void rightScored()
    {
        //Increment the score
        rightScore = rightScore + 1;
        if (rightScore == 5)
        {
            gameOver = true;
        }
    }

}
