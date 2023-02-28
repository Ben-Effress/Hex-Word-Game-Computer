package org.HexWordGameComputerPackage;

import javax.swing.*;
import java.awt.*;

public class RunSpellingBee implements Runnable{

    public static Color alabaster = new Color(242, 240, 230);

    @Override
    public void run() {
        // Create the JFrame and set its properties
        JFrame frame = new JFrame();
        frame.setBackground(alabaster);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel superGuessResultPanel = new JPanel();
        superGuessResultPanel.setLayout(new BorderLayout());
        superGuessResultPanel.setPreferredSize(new Dimension(550, 80));

        JPanel guessResultPanel = new JPanel();
        guessResultPanel.setBackground(alabaster);
        JLabel guessResultLabel = new JLabel();
        guessResultLabel.setBackground(alabaster);
        guessResultPanel.add(guessResultLabel);

        superGuessResultPanel.setBackground(alabaster);
        superGuessResultPanel.add(Box.createRigidArea(new Dimension(0, 25)), BorderLayout.NORTH);
        superGuessResultPanel.add(guessResultPanel);

        SpellingBeeGame game = new SpellingBeeGame(guessResultLabel, guessResultPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(550, 415));
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(alabaster);

        JPanel superProgressPanel = new JPanel();
        superProgressPanel.setBackground(alabaster);
        superProgressPanel.setLayout(new BorderLayout());
        superProgressPanel.setPreferredSize(new Dimension(550, 50));

        int progressBarWidth = 420;
        int progressBarHeight = 20;

        JPanel superProgressBarPanel = new JPanel();
        superProgressBarPanel.setPreferredSize(new Dimension(420, 40));
        superProgressBarPanel.setBackground(alabaster);
        superProgressBarPanel.setLayout(new BoxLayout(superProgressBarPanel, BoxLayout.X_AXIS));

        SpellingBeeProgressBar newProgressBar = new SpellingBeeProgressBar(game, progressBarWidth, progressBarHeight);

        superProgressBarPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        superProgressBarPanel.add(newProgressBar);

        JLabel scoreStatusLabel = new JLabel("Beginner");
        scoreStatusLabel.setPreferredSize(new Dimension(96, 24));
        scoreStatusLabel.setFont(new Font("Serif", Font.BOLD, 20));

        superProgressPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
        superProgressPanel.add(scoreStatusLabel, BorderLayout.WEST);
        superProgressPanel.add(superProgressBarPanel, BorderLayout.CENTER);
        superProgressPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);

        JPanel wordAbbreviationsPanel = new JPanel();
        wordAbbreviationsPanel.setBackground(alabaster);
        wordAbbreviationsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane wordAbbreviationsScroll = new JScrollPane(wordAbbreviationsPanel);
        wordAbbreviationsScroll.setPreferredSize(new Dimension(400, 315));
        wordAbbreviationsScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        rightPanel.add(superProgressPanel, BorderLayout.NORTH);
        rightPanel.add(wordAbbreviationsScroll);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel superGuessPanel = new JPanel();
        superGuessPanel.setBackground(alabaster);
        superGuessPanel.setLayout(new BorderLayout());
        JPanel guessPanel = new JPanel();
        guessPanel.setBackground(alabaster);
        JLabel guessLabel = new JLabel("Type your guess");
        guessLabel.setForeground(Color.GRAY);
        guessLabel.setFont(new Font("Serif", Font.BOLD, 30));
        guessPanel.add(guessLabel, BorderLayout.CENTER);
        superGuessPanel.add(superGuessResultPanel, BorderLayout.NORTH);
        superGuessPanel.add(guessPanel, BorderLayout.CENTER);

        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(alabaster);

        SpellingBeeGrid gameGrid = new SpellingBeeGrid(game, guessLabel, newProgressBar,
                scoreStatusLabel, wordAbbreviationsPanel);
        gameGrid.updateGUI();
        JPanel gameResetterPanel = new JPanel();
        gameResetterPanel.setBackground(alabaster);
        JButton gameResetter = new JButton("New Game!");
        gameResetter.addActionListener(e -> {
            game.startNewGame();
            gameGrid.updateGUI();
            frame.requestFocusInWindow();
        });
        gameResetterPanel.add(gameResetter);
        gamePanel.add(gameGrid);

        leftPanel.add(superGuessPanel);
        leftPanel.add(gamePanel);
        leftPanel.add(gameResetterPanel);

        frame.setLayout(new GridLayout(1, 2));
        frame.addKeyListener(gameGrid);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
