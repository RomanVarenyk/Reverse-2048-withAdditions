import java.awt.*;

import javax.swing.*;
/**
 * Sets values for basic tile objects with standard fields and funnctions
 */
public class Tile {
    // Fields
    private int number; // must be a power of 2, unless number is 0 which indicates an empty tile
    private Color color;
    private boolean isLocked;

    public Tile(int n) {
        this.number = n;
        // Set color based on number
        setColor();
        isLocked = false;
    }

    /**
     * Sets the color of the tile based on its number
     */
    private void setColor() {
        if (number == 2048) {
            color = new Color(224, 224, 204);
        } else if (number == 1024) {
            color = new Color(191, 191, 174);
        } else if (number == 512) {
            color = new Color(240, 185, 84);
        } else if (number == 256) {
            color = new Color(196, 129, 6);
        } else if (number == 128) {
            color = new Color(235, 94, 52);
        } else if (number == 64) {
            color = new Color(235, 65, 14);
        } else if (number == 32) {
            color = new Color(242, 231, 19);
        } else if (number == 16) {
            color = new Color(204, 194, 8);
        } else if (number == 8) {
            color = new Color(171, 162, 5);
        } else if (number == 4) {
            color = new Color(115, 109, 2);
        } else if (number == 2) {
            color = new Color(3, 161, 95);
        } else if (number == 1) {
            color = new Color(6, 61, 201);
        } else if (number == 0) {
            color = new Color(161, 161, 148);
        } else {
            System.err.println("Error! Number not valid");
        }
    }

    /**
     * @return tile number
     */
    public int getNumber() {
        return this.number;
    }

    public boolean getLocked() {
        return this.isLocked;
    }

    public void setLocked(boolean state) {
        this.isLocked = state;
    }

    /**
     * Draws the tile on the screen given an x, y, and tileSize
     */
    public void draw(int x, int y, int tileSize, Graphics g) {
        // Draw the square
        g.setColor(color);
        g.fillRect(x, y, tileSize, tileSize);

        // Put the number in the middle of the square
        g.setColor(new Color(41, 41, 37));
        int[] textInfo = centerText(x, y, tileSize);
        g.setFont(new Font("Arial", Font.BOLD, textInfo[2]));
        if (number != 0) {
            g.drawString(Integer.toString(number), textInfo[0], textInfo[1]);
        }
        
    }

    /**
     * Returns an int[] of x and y coordinates, and fontSize for text that is centered in a tile
     */
    public int[] centerText(int x, int y, int tileSize) {
        // Get number of digits
        int numDigits = Integer.toString(number).length();
        // Set font size
        int fontSize = (int) (tileSize / (numDigits * 1.1));
        int centerX = x + tileSize / 2;
        int centerY = y + tileSize / 2;
        int scale = fontSize / 3;

        int newX = centerX - ((scale) * (1 + numDigits / 2));
        int newY = centerY + (fontSize / 2);

        return new int[] {newX, newY, fontSize};
    }

}
