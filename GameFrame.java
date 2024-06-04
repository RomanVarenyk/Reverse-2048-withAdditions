import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame {
    // Frame
    private JFrame frame;
    private GamePanel panel;
    private JButton resetButton;

    public GameFrame() {
        // Initialize frame and declare GamePanel
        frame = new JFrame();
        panel = new GamePanel();
        frame.setLayout(new BorderLayout());

        // Initialize reset button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.resetGame();
            }
        });

        // Add components to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(resetButton, BorderLayout.SOUTH);

        frame.setTitle("Reverse 2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null); // centers frame in center of screen
        frame.setVisible(true);
    }
}
