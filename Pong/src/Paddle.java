import java.awt.*;

/**
 * @author Logan Karstetter
 * Date: 01/26/2018
 */
public class Paddle
{
    /** The width of the paddle */
    private int pWidth = 25;
    /** The height of the paddle */
    private int pHeight = 100;

    /** The x-coordinate of the paddle within the PongPanel */
    private int xPos;
    /** The y-coordinate of the paddle within the PongPanel */
    private int yPos;

    /** The number of pixels the paddle will move (up or down) per key press */
    private int yStep = 15;

    /** The color of the paddle */
    private Color pColor = Color.WHITE;

    /** A reference to the PongPanel */
    private PongPanel pPanel;
    /** A reference to the KeyManager */
    private KeyManager keyManager;
    /** Determines whether this is the left or right paddle */
    private boolean isLeftSide;

    /**
     * Creates a paddle to be used in a game of Pong.
     * @param pPanel The PongPanel this paddle resides within.
     * @param keyManager The KeyManager that feeds key input to this paddle.
     * @param isLeftSide Determines whether the paddle is the left or right paddle.
     */
    public Paddle(PongPanel pPanel, KeyManager keyManager, boolean isLeftSide)
    {
        //Store the PongPanel and KeyManager
        this.pPanel = pPanel;
        this.keyManager = keyManager;
        this.isLeftSide = isLeftSide;

        //Set the x-coordinate according to the side the paddle is on
        if (isLeftSide)
        {
            xPos = 0;
        }
        else
        {
            xPos = PongPanel.PWIDTH - pWidth;
        }
        //Set the y-coordinate to the middle of the PongPanel
        yPos = PongPanel.PHEIGHT/2 - pHeight/2;
    }

    /**
     * Move the paddle upwards in the y-direction.
     */
    private void moveUp()
    {
        //Make sure the paddle doesn't move off the top of the screen
        if (!(yPos - yStep <= 0))
        {
            yPos = yPos - yStep;
        }
        else //The yPos - yStep is always < 0, so just move the yPos to zero
        {
            yPos = 0;
        }
    }

    /**
     * Move the paddle downwards in the y-direction.
     */
    private void moveDown()
    {
        //Make sure the paddle doesn't move off the bottom of the screen
        if (!(yPos + yStep + pHeight > PongPanel.PHEIGHT))
        {
            yPos = yPos + yStep;
        }
    }

    /**
     * Update the position of the paddle using the KeyManager.
     */
    public void update()
    {
        //Determine which direction to move, if not both
        //Move the left keys
        if (isLeftSide && keyManager.leftUp)
        {
            moveUp();
        }

        if (isLeftSide && keyManager.leftDown)
        {
            moveDown();
        }

        //Move the right keys
        if (!isLeftSide && keyManager.rightUp)
        {
            moveUp();
        }
        if (!isLeftSide && keyManager.rightDown)
        {
            moveDown();
        }
    }

    /**
     * Draw the paddle on the screen.
     * @param dbGraphics The dbGraphics object from the PongPanel.
     */
    public void draw(Graphics dbGraphics)
    {
        //Draw the paddle
        dbGraphics.setColor(pColor);
        dbGraphics.fill3DRect(xPos, yPos, pWidth, pHeight, true);
    }

    /**
     * Get a rectangle representing the paddle's position and dimensions.
     * @return A rectangle representing the paddle.
     */
    public Rectangle getRectangle()
    {
        return new Rectangle(xPos, yPos, pWidth, pHeight);
    }
}
