import javax.swing.*;
import java.awt.*;

public class GameFrame {
    // Frame
    private JFrame frame;

    public GameFrame() {

        // Initialize frame and declare GamePanel
        frame = new JFrame();
        GamePanel panel = new GamePanel();
        frame.add(panel); // adds panel to the GameFrame

        // Give frame default settings
        frame.setTitle("Reverse 2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null); // centers frame in center of screen
        frame.setVisible(true);
    }
}
