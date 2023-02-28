package org.HexWordGameComputerPackage;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunWordleGame implements Runnable {
    // The game model
    private WordleGame game;
    // The game grid graphics
    private GameGrid grid;
    // The frame containing the game
    private JFrame frame;
    // The label displaying the game title
    private JLabel titleLabel;

    // The panel that contains the title label
    private JPanel titlePanel;

    // The text area displaying the previous games
    private JTextArea historyArea;

    // The scroll pane that encloses the historyArea
    private JScrollPane historyPane;

    // Button to see instructions or not
    private JButton instructionsButton;

    // Instructions text area
    private JTextArea instructions;

    // Constructor
    public RunWordleGame() {
        frame = new JFrame("Wordle");
        frame.setBackground(Color.BLACK);
        frame.setLocation(500, 200);
        frame.setLayout(new BorderLayout());

        titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);

        titleLabel = new JLabel("WORDLE");
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        historyArea = new JTextArea();
        historyArea.setBackground(Color.BLACK);
        historyArea.setForeground(Color.WHITE);
        historyArea.setEditable(false);

        historyPane = new JScrollPane(historyArea);
        historyPane.setPreferredSize(new Dimension(150, 100));

        game = new WordleGame(historyArea);
        grid = new GameGrid(game);

        instructionsButton = new JButton("Press to Toggle Instructions");
        instructionsButton.addActionListener(e -> {
            boolean visibility = !instructions.isVisible();
            instructions.setVisible(visibility);
            frame.pack();
            frame.requestFocusInWindow();
        });

        instructions = new JTextArea();
        instructions.setLineWrap(true);
        instructions.append("Welcome to my edition of the popular game Wordle! In " +
                "this game, you will aim " +
                "to guess\n" +
                "the correct word by inputting guess of 5 letters (they do " +
                "not need to be actual " +
                "words.\nThis allows you to get create and strategic in your " +
                "guesses). When you have " +
                "inputted a\nfull row of letters, press ENTER to submit your " +
                "guess. " +
                "If a letter is in the correct spot, the\ngrid box will turn" +
                " green. If the letter is " +
                "somewhere else in the word, the grid box will\nturn yellow. " +
                "If " +
                "the letter is not in the word, it will remain gray. You have 6 " +
                "chances to\nguess the correct word. The correct word is a" +
                " random word " +
                "from a select list of words so\nmany of the options will be fairly hard. " +
                "While guessing, you may use backspace key " +
                "to\ndelete" +
                "letters from your guess. If you guess the " +
                "word correctly, the word will be " +
                "added to\nyour history of words on the right " +
                "along with the number of " +
                "guesses it took you to\nguess the correct " +
                "word. " +
                "If you take six guesses and you have not " +
                "guessed " +
                "the word \ncorrectly, an X will be next to " +
                "the correct " +
                "word. I hope you enjoy!");
        instructions.setPreferredSize(new Dimension(100, 200));
        instructions.setEditable(false);
        instructions.setVisible(false);
    }

    @Override
    public void run() {

        // Load the previous games history
        try {
            BufferedReader reader = new BufferedReader(new FileReader("files/history.txt"));
            String line = reader.readLine();
            while (line != null) {
                historyArea.append(line + "\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the elements to the frame
        frame.add(instructionsButton, BorderLayout.WEST);
        frame.add(grid, BorderLayout.CENTER);
        frame.addKeyListener(grid);
        frame.add(historyPane, BorderLayout.EAST);
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(instructions, BorderLayout.SOUTH);


        // Set the frame size and make it visible
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.requestFocusInWindow();
    }

}