import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * @author Logan Karstetter
 * Date: 02/11/2018
 */
public class KeyManager implements KeyListener
{
    /** A HashMap of booleans determining if a key has been pressed.
     * The key values for the map are the unique KeyCodes for each key. */
    private HashMap<Integer, Boolean> keys;

    /** Determines if the esc button is pressed */
    private boolean escape;
    /** Determines if the left paddle's up key was pressed */
    public boolean leftUp;
    /** Determines if the left paddle's down key was pressed */
    public boolean leftDown;
    /** Determines if the right paddle's up key was pressed */
    public boolean rightUp;
    /** Determines if the right paddle's down key was pressed */
    public boolean rightDown;

    /** A reference to the PongPanel this KeyManager listens for */
    private PongPanel pPanel;

    /**
     * A KeyManager is used to process concurrent keyboard inputs. The manager maintains
     * a HashMap of boolean values corresponding to each relevant key. When a key is pressed/held
     * the value in the HashMap is set to true using the keyCode as the key. The value is set to
     * false when a key is released.
     * @param pPanel The PongPanel this KeyManager handles KeyEvents for.
     */
    public KeyManager(PongPanel pPanel)
    {
        //Store the reference to the PongPanel
        this.pPanel = pPanel;

        //Create the keys map
        keys = new HashMap<>();
        keys.put(KeyEvent.VK_ESCAPE, false);
        keys.put(KeyEvent.VK_A, false);
        keys.put(KeyEvent.VK_Z, false);
        keys.put(KeyEvent.VK_K, false);
        keys.put(KeyEvent.VK_M, false);
    }

    /**
     * Update the KeyManager's escape, leftUp, leftDown, rightUp, and rightDown key boolean values.
     */
    public void update()
    {
        //Set the booleans according to the map contents
        escape = keys.get(KeyEvent.VK_ESCAPE);
        leftUp = keys.get(KeyEvent.VK_A);
        leftDown = keys.get(KeyEvent.VK_Z);
        rightUp = keys.get(KeyEvent.VK_K);
        rightDown = keys.get(KeyEvent.VK_M);

        //Check if escape is set to true, stop the game
        if (escape)
        {
            pPanel.stopGame();
        }
    }

    /**
     * Invoked when a key is pressed. The keyCode of the keyEvent is used to
     * set the corresponding boolean in the keys HashMap.
     * @param e A KeyEvent
     */
    public void keyPressed(KeyEvent e)
    {
        keys.replace(e.getKeyCode(), true);
    }

    /**
     * Invoked when a key is released. The keyCode of the keyEvent is used to
     * set the corresponding boolean in the keys HashMap.
     * @param e A KeyEvent
     */
    public void keyReleased(KeyEvent e)
    {
        keys.replace(e.getKeyCode(), false);
    }

    /**
     * Invoked when a key is pressed and then released. This method does nothing.
     * @param e A KeyEvent
     */
    public void keyTyped(KeyEvent e)
    {
        //Do nothing
    }
}
