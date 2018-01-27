import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Logan Karstetter
 * Date: 01/26/2018
 */
public class KeyManager implements KeyListener
{
    /** An array of booleans determining if a key has been pressed */
    private boolean keys[];

    /** Determines if the esc button is pressed */
    private boolean escape;

    /** Determines if the left paddle's up key is pressed */
    public boolean leftUp;
    /** Determines if the left paddle's down key is pressed */
    public boolean leftDown;
    /** Determines if the right paddle's up key is pressed */
    public boolean rightUp;
    /** Determines if the right paddle's down key is pressed */
    public boolean rightDown;

    /** A reference to the PongPanel */
    private PongPanel pPanel;

    /**
     * A KeyManager is used to process concurrent keyboard inputs. The manager maintains
     * an array of boolean values corresponding to each relevant key. When a key is pressed/held
     * the value in the array is set to true. The value is set to false when a key is released.
     * @param pPanel The PongPanel this KeyManager handles KeyEvents for.
     */
    public KeyManager(PongPanel pPanel)
    {
        //Store the PongPanel
        this.pPanel = pPanel;

        //Initialize the keys array
        //Keys are looked up using KeyCodes, KeyEvent.VK_Z has the highest keycode (90) out of all
        //the keys used here, so there's no reason to make a bigger array.
        keys = new boolean[91]; //zero indexed
    }

    /**
     * Update the KeyManager's leftUp, leftDown, rightUp, rightDown, and escape
     * boolean values.
     */
    public void update()
    {
        //Set the booleans according to the array contents
        escape = keys[KeyEvent.VK_ESCAPE];
        leftUp = keys[KeyEvent.VK_A];
        leftDown = keys[KeyEvent.VK_Z];
        rightUp = keys[KeyEvent.VK_K];
        rightDown = keys[KeyEvent.VK_M];

        //Check if escape is set to true, stop the game
        if (escape)
        {
            pPanel.stopGame();
        }
    }

    /**
     * Invoked when a key is pressed. The keyCode of the keyEvent is used to
     * set the corresponding boolean in the keys array to true.
     * @param e A KeyEvent
     */
    public void keyPressed(KeyEvent e)
    {
        //The key is pressed or held down
        //90 is pong specific, Z is has the highest key code of 90 out of a, k ,m and esc
        if (e.getKeyCode() <= 90)
        {
            keys[e.getKeyCode()] = true;
            //System.out.println("Key Pressed");
        }
    }

    /**
     * Invoked when a key is released. The keyCode of the keyEvent is used to
     * set the corresponding boolean in the keys array to false.
     * @param e A KeyEvent
     */
    public void keyReleased(KeyEvent e)
    {
        //The key is not longer pressed
        if (e.getKeyCode() <= 90) //90 is pong specific
        {
            keys[e.getKeyCode()] = false;
            //System.out.println("Key Released");
        }
    }

    /**
     * Invoked when a key is typed (pressed and released). This method does nothing.
     * @param e A KeyEvent
     */
    public void keyTyped(KeyEvent e)
    {
        //Do nothing
    }
}
