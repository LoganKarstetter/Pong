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
     * Launches a game of Pong. A single integer value can be specified as a command line argument to
     * set the FPS for the game. If no value is provided it will run at the default FPS (20).
     * @param args An integer specifying the requested FPS.
     */
    public static void main(String[] args)
    {

        //Check for command line arguments
        if (args.length > 0)
        {
            //Cast the first argument to an integer
            try
            {
                //Start a game with the requested FPS
                int FPS = Integer.valueOf(args[0]);
                System.out.println("Running Pong with FPS: " + FPS);
                new Pong(FPS);
            }
            catch (Exception e) //Horrible practice, but error catching isn't useful here
            {
                System.out.println("Unable to set requested FPS value: " + args[0] + "\nPlease enter only integers. Exiting...");
                System.exit(0);
            }
        }
        else //Use the default FPS
        {
            System.out.println("Running Pong with default FPS: " + DEFAULT_FPS);
            new Pong(DEFAULT_FPS);
        }
    }

}
