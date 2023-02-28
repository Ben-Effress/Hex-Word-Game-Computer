package org.HexWordGameComputerPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameGrid extends JPanel implements KeyListener {
    // The game model
    private WordleGame game;

    // The size of each grid box in pixels
    private int boxSize = 50;

    // Keeps track of whether a key is currently being typed
    private boolean keyTyped;

    // Constructor
    public GameGrid(WordleGame game) {
        this.game = game;
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        // Calculate the dimensions of the GameGrid
        int width = boxSize * 5;
        int height = boxSize * 6;

        // Set the preferred size of the GameGrid
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get the grid components for the correctness and the letters
        int[][] correctness = game.getCorrectness();
        char[][] letters = game.getLetters();

        // Set the background color of the GameGrid
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw the boxes in the GameGrid
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                // Calculate the x and y coordinates of the box
                int x = col * boxSize;
                int y = row * boxSize;

                // Set the color of the box based on its correctness value
                switch (correctness[row][col]) {
                    case 0 -> g.setColor(Color.GRAY);
                    case 1 -> g.setColor(Color.YELLOW);
                    case 2 -> g.setColor(Color.GREEN);
                    default -> System.out.println("Correctness of square is invalid");
                }

                // Draw the box with the calculated coordinates and size
                g.fillRect(x, y, boxSize, boxSize);

                // Draw the black border of the box
                g.setColor(Color.BLACK);
                g.drawRect(x, y, boxSize, boxSize);

                // Draw the letter in the box
                g.setColor(Color.MAGENTA);
                g.setFont(new Font("Serif", Font.BOLD, 20));
                g.drawString(
                        Character.toString(letters[row][col]), x + boxSize / 2 - 7,
                        y + boxSize / 2 + 5
                );
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // If a key has not been typed yet, add the letter to the guess field of the
        // WordleGame object
        if (!keyTyped) {
            char letter = e.getKeyChar();
            if (Character.isLetter(letter)) {
                letter = Character.toLowerCase(letter);
                game.addLetter(letter);
            }
            keyTyped = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle the ENTER and BACKSPACE keys
        int keyCode = e.getKeyCode();
        if (!keyTyped) {
            if (keyCode == KeyEvent.VK_ENTER) {
                game.checkGuess();
            } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                game.deleteLetter();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyTyped = false;
        repaint();
    }
}