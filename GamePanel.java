import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    // Screen size settings
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;

    // set square size
    private static final int TILE_SIZE = SCREEN_WIDTH / 7;

    // Create board 2D-list
    private Tile[][] board = new Tile[4][4];

    // Board variables
    int horPad = SCREEN_WIDTH / 6; // horizontal padding
    int verPad = SCREEN_HEIGHT / 6; // vertical padding
    int boardSize = SCREEN_WIDTH - (horPad * 2);
    int tilePad = TILE_SIZE / 7; // padding between tiles 

    private boolean running;

    // Notes: 2048 should spawn 90% of the time, and 1024 should spawn 10% of the time. Initially, two 2048's should spawn in random places

    
    public GamePanel() {

        // Set size
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true); // allows keyboard inputs to go directly to GamePanel

        // Key Listener
        this.addKeyListener(new KeyAdapter() {
            /**
             * Takes input and changes board if user presses arrow keys
             */
            public void keyPressed(KeyEvent e) {
                if (running) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        moveLeft();
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        moveRight();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        moveUp();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        moveDown();
                    }
                    if (checkLost()) {
                        running = false;
                    }
                }
                
            }
        });

        // Fill board with tiles
        populateBoard();
        running = true;
    }

    /**
     * Swing function that is automatically called upon changes in state, or my calls to repaint()
     * Calls my draw method
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Draws the game board, as well as other elements on screen
     */
    public void draw(Graphics g) {
        // Draw board
        g.setColor(new Color(79, 79, 70)); // dark grey
        g.fillRect(horPad, verPad, boardSize, boardSize);

        // Draw tiles
        for (int r = 0; r < 4; r++) {
            int tileY = verPad + ((r+1) * tilePad) + (r * TILE_SIZE);
            for (int c = 0; c < 4; c++) {
                int tileX = horPad + ((c+1) * tilePad) + (c * TILE_SIZE);
                board[r][c].draw(tileX, tileY, TILE_SIZE, g);
            }
        }

        // Game over text
        if (!running) {
            g.setColor(new Color(255, 10, 10));
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over!", horPad + 50, verPad - 5);
        }
    }

    /**
     * Fills the board with all empty tiles, except two random spots on board are given a 2048 or 1024
     */
    public void populateBoard() {
        // Fill board with all empty tiles
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                board[r][c] = new Tile(0);
            }
        }
        // Set 2 random squares to 2048
        int count = 0;
        while (count < 2) {
            int r = (int) (Math.random() * 4);
            int c = (int) (Math.random() * 4);
            // ensure not a repeat square
            if (board[r][c].getNumber() == 0) {
                board[r][c] = new Tile(2048);
                count++;
            }
        }
    }

    public boolean isFull() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c].getNumber() == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * If board is all filled after a movement has been made, see if there are any possible combinations left
     * @return true if game is over
     */
    public boolean checkLost() {
        if (!isFull()) { return false; } // check if board is fully occupied
        // Horizontal check
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getNumber() == board[r][c + 1].getNumber()) {
                    return false;
                }
            }
        }
        // Vertical check
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 3; r++) {
                if (board[r][c].getNumber() == board[r + 1][c].getNumber()) {
                    return false;
                }
            }
        }
        // If no combinations were found, return true
        return true;
    }
    /**
     * Unlocks a row or column of Tiles after movement has finished
     */
    public void unlock(Tile[] seq) {
        for (Tile t : seq) {
            t.setLocked(false);
        }
    }

    public void spawn() {
        // determine whether spawning 2048 or 1024 (90 % - 10 %)
        int p = (int) (Math.random() * 10);
        int newN;
        if (p == 0) {
            newN = 1024;
        } else {
            newN = 2048;
        }
        boolean spawned = false;
        while (!spawned) {
            int randR = (int) (Math.random() * 4);
            int randC = (int) (Math.random() * 4);
            if (board[randR][randC].getNumber() == 0) {
                board[randR][randC] = new Tile(newN);
                spawned = true;
            }
        }
        
    }

    /**
     * Returns an array of all elements in the column
     */
    public Tile[] makeColumn(int c) {
        Tile[] col = new Tile[4];
        for (int r = 0; r < 4; r++) {
            col[r] = board[r][c];
        }
        return col;
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {

        }
    }

    /**
     * Called when user inputs a left movement
     */
    public void moveLeft() {
        boolean hasMoved = false;
        for (int r = 0; r < 4; r++) {
            for (int c = 1; c < 4; c++) {
                if (board[r][c].getNumber() != 0) {
                    if (board[r][c - 1].getNumber() == 0) {
                        board[r][c - 1] = board[r][c];
                        board[r][c] = new Tile(0);
                        repaint();
                        pause(5);
                        c = 0; // reset after change
                        hasMoved = true;
                    } else if (!board[r][c].getLocked() && !board[r][c - 1].getLocked() && board[r][c - 1].getNumber() == board[r][c].getNumber()) {
                        board[r][c - 1] = new Tile(board[r][c].getNumber() / 2);
                        board[r][c - 1].setLocked(true);
                        board[r][c] = new Tile(0);
                        repaint();
                        pause(5);
                        c = 0; // reset after change 
                        hasMoved = true;
                    }
                }  
            }
            // Unlock row
            unlock(board[r]);
            repaint();
        }       
        if (hasMoved) {
            spawn();
        }
        
    }

    public void moveRight() {
        boolean hasMoved = false;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getNumber() != 0) {
                    if (board[r][c + 1].getNumber() == 0) {
                        board[r][c + 1] = board[r][c];
                        board[r][c] = new Tile(0);
                        repaint();
                        c = -1; // reset after change
                        hasMoved = true;
                    } else if (!board[r][c].getLocked() && !board[r][c + 1].getLocked() && board[r][c + 1].getNumber() == board[r][c].getNumber()) {
                        board[r][c + 1] = new Tile(board[r][c].getNumber() / 2);
                        board[r][c + 1].setLocked(true);
                        board[r][c] = new Tile(0);
                        repaint();
                        c = -1; // reset after collision
                        hasMoved = true;
                    }
                }
            }
            unlock(board[r]);
            repaint();
        }       
        if (hasMoved) {
            spawn();
        }   
    }

    public void moveDown() {
        boolean hasMoved = false;
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 3; r++) {
                if (board[r][c].getNumber() != 0) {
                    if (board[r + 1][c].getNumber() == 0) {
                        board[r + 1][c] = board[r][c];
                        board[r][c] = new Tile(0);
                        repaint();
                        r = -1; // reset after change
                        hasMoved = true;
                    } else if (!board[r][c].getLocked() && !board[r + 1][c].getLocked() && board[r + 1][c].getNumber() == board[r][c].getNumber()) {
                        board[r + 1][c] = new Tile(board[r][c].getNumber() / 2);
                        board[r + 1][c].setLocked(true);
                        board[r][c] = new Tile(0);
                        repaint();
                        r = -1; // reset after collision
                        hasMoved = true;
                    }
                }
            }
            unlock(makeColumn(c));
            repaint();
        }  
        if (hasMoved) {
            spawn();
        }   
    }

    public void moveUp() {
        boolean hasMoved = false;
        for (int c = 0; c < 4; c++) {
            for (int r = 1; r < 4; r++) {
                if (board[r][c].getNumber() != 0) {
                    if (board[r - 1][c].getNumber() == 0) {
                        board[r - 1][c] = board[r][c];
                        board[r][c] = new Tile(0);
                        repaint();
                        r = 0; // reset after change
                        hasMoved = true;
                    } else if (!board[r][c].getLocked() && !board[r - 1][c].getLocked() && board[r - 1][c].getNumber() == board[r][c].getNumber()) {
                        board[r - 1][c] = new Tile(board[r][c].getNumber() / 2);
                        board[r - 1][c].setLocked(true);
                        board[r][c] = new Tile(0);
                        repaint();
                        r = 0; // reset after change 
                        hasMoved = true;
                    }
                }  
            }
            // Unlock row
            unlock(makeColumn(c));
            repaint();
        }       
        if (hasMoved) {
            spawn();
        }
    }


}
