import java.awt.*;

/**
 * @author Logan Karstetter
 * Date: 01/26/2018
 */
public class Ball
{
    /** The radius of the ball */
    private int bRadius = 10;
    /** The x-coordinate of the ball */
    private int xPos;
    /** The y-coordinate of the ball */
    private int yPos;

    /** The number of pixels the ball will move horizontally per game loop/cycle */
    private int xStep = 10;
    /** The number of pixels the ball will move vertically per game loop/cycle */
    private int yStep = 10;

    /** The color of the ball */
    private Color bColor = Color.WHITE;

    /** A reference to the PongPanel */
    private PongPanel pPanel;
    /** A reference to the leftPaddle */
    private Paddle leftPaddle;
    /** A reference to the rightPaddle */
    private Paddle rightPaddle;

    /**
     * Creates a ball to be used in a game of Pong.
     * @param pPanel The PongPanel this ball resides within.
     * @param leftPaddle The left paddle in the Pong Panel.
     * @param rightPaddle The right paddle in the Pong Panel.
     */
    public Ball(PongPanel pPanel, Paddle leftPaddle, Paddle rightPaddle)
    {
        //Store the PongPanel
        this.pPanel = pPanel;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;

        //Set the ball's position
        resetBall();
    }

    /**
     * Move the ball by adding its xStep and yStep values to its current position.
     */
    public void update()
    {
        //Check if the ball has hit anything or gone off-screen
        hasHitPaddle();
        hasHitWall();

        //Move the ball
        xPos = xPos + xStep;
        yPos = yPos + yStep;
    }

    /**
     * Draw the ball on the screen.
     * @param dbGraphics The dbGraphics object from the PongPanel.
     */
    public void draw(Graphics dbGraphics)
    {
        dbGraphics.setColor(bColor);
        dbGraphics.fillOval(xPos, yPos, bRadius * 2, bRadius * 2);
    }

    /**
     * Determines whether the ball has hit either paddle. If so, the ball's vertical and horizontal
     * steps are inverted to make the ball appear to bounce off the paddle.
     */
    private void hasHitPaddle()
    {
        //Get the collision box for the ball
        Rectangle ballRect = new Rectangle(xPos, yPos, bRadius * 2, bRadius * 2);

        //Check if the ball has hit the left paddle
        if (ballRect.intersects(leftPaddle.getRectangle()))
        {
            //Move the ball in the other direction as if it bounced off the paddle
            xStep = -xStep;
            //Give the ball a boost to get it away from the paddle
            xPos = xPos + Math.abs(xStep/2);
        }
        else if (ballRect.intersects(rightPaddle.getRectangle()))
        {
            //Move the ball in the other direction as if it bounced off the paddle
            xStep = -xStep;
            //Give the ball a boost to get it away from the paddle
            xPos = xPos - Math.abs(xStep/2);
        }
    }

    /**
     * Determines whether the ball has hit any of the walls. If so, the ball will either be reset
     * (a point was scored) or the ball will bounce off the wall.
     */
    private void hasHitWall()
    {
        //Check if the ball has hit the top or bottom of the panel
        if ((yPos <= 0) && (yStep < 0)) //yStep < 0 means the ball is moving upwards
        {
            //Bounce the ball
            yStep = -yStep;
        }
        else if ((yPos + (bRadius * 2) >= PongPanel.PHEIGHT) && (yStep > 0)) //Ball is moving down
        {
            yStep = -yStep;
        }
        else if ((xPos + (bRadius * 2) <= 0) && (xStep < 0)) //The ball has gone off-screen on the left
        {
            //Score for the right paddle
            pPanel.rightScored();
            //Reset the ball
            resetBall();
        }
        else if ((xPos >= PongPanel.PWIDTH) && (xStep > 0)) //The ball has gone off-screen on the right
        {
            //Score for the left paddle
            pPanel.leftScored();
            //Reset the ball
            resetBall();
        }
    }

    /**
     * Reset the position of the ball to the middle of the screen. This is typically done at the
     * start of the game or after a player has scored. The ball's xStep and yStep values are
     * also inverted to change its direction.
     */
    private void resetBall()
    {
        //Set the ball's initial position to the center of the panel
        xPos = PongPanel.PWIDTH/2 - bRadius;
        yPos = PongPanel.PHEIGHT/2 - bRadius;

        //Change the ball's directions
        double sign = Math.random();
        //Invert the xStep
        if (sign >= 0.5)
        {
            xStep = -xStep;
        }
        else //Invert the yStep
        {
            yStep = -yStep;
        }
    }
}
