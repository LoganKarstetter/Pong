import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Logan Karstetter
 * Date: 01/26/2018
 */
public class Pong extends JFrame implements WindowListener
{
    /** The desired FPS/UPS for Pong */
    private static int DEFAULT_FPS = 20;

    /** The PongPanel used to play Pong */
    private PongPanel pPanel;

    /**
     * A two-player Pong game.
     * @param FPS The desired FPS.
     */
    public Pong(int FPS)
    {
        super("Pong");

        //Create the PongPanel and add it to the contentPane
        pPanel = new PongPanel(FPS);
        getContentPane().add(pPanel);

        //Add a window listener to handle pausing
        addWindowListener(this);
        setResizable(false);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Resumes the game when the window is activated/invoked.
     * @param e A WindowEvent
     */
    public void windowActivated(WindowEvent e)
    {
        pPanel.resumeGame();
    }

    /**
     * Pauses the game when the window is deactivated.
     * @param e A WindowEvent
     */
    public void windowDeactivated(WindowEvent e)
    {
        pPanel.pauseGame();
    }

    /**
     * Resumes the game when the window is deiconified/invoked.
     * @param e A WindowEvent
     */
    public void windowDeiconified(WindowEvent e)
    {
        pPanel.resumeGame();
    }

    /**
     * Pauses the game when the window is iconified.
     * @param e A WindowEvent
     */
    public void windowIconified(WindowEvent e)
    {
        pPanel.pauseGame();
    }

    /**
     * Stops the game when the window is closed.
     * @param e A WindowEvent
     */
    public void windowClosing(WindowEvent e)
    {
        pPanel.stopGame();
    }

    /**
     * This method does nothing.
     * @param e A WindowEvent
     */
    public void windowOpened(WindowEvent e)
    {
        //Do nothing
    }

    /**
     * This method does nothing.
     * @param e A WindowEvent
     */
    public void windowClosed(WindowEvent e)
    {
        //Do nothing
    }

    /**
     * Launches a game of Pong.
     * @param args Command line arguments are not used.
     */
    public static void main(String[] args)
    {
        new Pong(DEFAULT_FPS);
    }

}
