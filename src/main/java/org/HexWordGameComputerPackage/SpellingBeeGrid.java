package org.HexWordGameComputerPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_ENTER;

public class SpellingBeeGrid extends JPanel implements KeyListener {

    private SpellingBeeGame game;

    private int width = 300;
    private int height = 300;

    private final int xCenter = width/2;
    private final int yCenter = height/2;

    private final int strokeWidth = 5;

    private final int radius = (int) (.93 * 2 * SpellingBeeHexagon.radius);

    private final int xOffset = (int) ((.875) * radius);


    private List<SpellingBeeHexagon> hexagons;
    private List<String> letters;

    private JLabel guessLabel, scoreStatusLabel;

    private SpellingBeeProgressBar progressBar;

    private JPanel wordAbbreviationsPanel;

    public SpellingBeeGrid(
            SpellingBeeGame game, JLabel guessLabel, SpellingBeeProgressBar progressBar,
            JLabel scoreStatusLabel, JPanel wordAbbreviationsPanel
    ) {
        this.game = game;
        this.guessLabel = guessLabel;
        this.progressBar = progressBar;
        this.scoreStatusLabel = scoreStatusLabel;
        this.wordAbbreviationsPanel = wordAbbreviationsPanel;
        this.letters = game.getLetters();
        this.setBackground(RunSpellingBee.alabaster);
        this.setPreferredSize(new Dimension(width, height));
        hexagons = new ArrayList<>();
        hexagons.add(new SpellingBeeHexagon(letters.get(0), xCenter, yCenter, true));
        hexagons.add(new SpellingBeeHexagon(letters.get(1), xCenter - xOffset, yCenter - radius/2,
                false));
        hexagons.add(new SpellingBeeHexagon(letters.get(2), xCenter, yCenter - radius, false));
        hexagons.add(new SpellingBeeHexagon(letters.get(3), xCenter + xOffset, yCenter - radius/2,
                false));
        hexagons.add(new SpellingBeeHexagon(letters.get(4), xCenter + xOffset, yCenter + radius/2,
                false));
        hexagons.add(new SpellingBeeHexagon(letters.get(5), xCenter, yCenter + radius, false));
        hexagons.add(new SpellingBeeHexagon(letters.get(6), xCenter - xOffset, yCenter + radius/2,
                false));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        hexagons.clear();
        hexagons.add(new SpellingBeeHexagon(letters.get(0), xCenter, yCenter, true));
        hexagons.add(new SpellingBeeHexagon(letters.get(1), xCenter - xOffset, yCenter - radius/2,
                false));
        hexagons.add(new SpellingBeeHexagon(letters.get(2), xCenter, yCenter - radius, false));
        hexagons.add(new SpellingBeeHexagon(letters.get(3), xCenter + xOffset, yCenter - radius/2,
                false));
        hexagons.add(new SpellingBeeHexagon(letters.get(4), xCenter + xOffset, yCenter + radius/2,
                false));
        hexagons.add(new SpellingBeeHexagon(letters.get(5), xCenter, yCenter + radius, false));
        hexagons.add(new SpellingBeeHexagon(letters.get(6), xCenter - xOffset, yCenter + radius/2,
                false));
        for (SpellingBeeHexagon hex : hexagons) {
            // Set colors
            Color fill, borderAndLetter;
            if (hex.isCenter()) {
                fill = Color.BLACK;
                //borderAndLetter = Color.GREEN;
                borderAndLetter = new Color(0, 100, 0);
            } else {
                fill = new Color(0, 100, 0);
                borderAndLetter = Color.BLACK;
            }

            // Fill hexagon
            g.setColor(fill);
            g.fillPolygon(hex);

            // Draw border of hexagon
            int[] xPoints = hex.xpoints;
            int[] yPoints = hex.ypoints;
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(strokeWidth));
            g2.setColor(borderAndLetter);
            GeneralPath hexBorder = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
            hexBorder.moveTo(xPoints[0], yPoints[0]);
            for (int i = 1; i < hex.npoints; i ++) {
                hexBorder.lineTo(xPoints[i], yPoints[i]);
            }
            hexBorder.closePath();
            g2.draw(hexBorder);

            // Draw String
            g.setColor(borderAndLetter);
            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString(hex.getLetter(), hex.getXCenter() - 5, hex.getYCenter() + 7);
        }
    }

    public void shuffleLetters() {
        String middleLetter = letters.remove(0);
        Collections.shuffle(letters);
        letters.add(0, middleLetter);
        updateGUI();
    }

    public void updateGUI() {
        if (game.getGuess().length() > 0) {
            guessLabel.setForeground(Color.BLACK);
            guessLabel.setText(game.getGuess());
        } else {
            guessLabel.setForeground(Color.GRAY);
            guessLabel.setText("Type your guess");
        }
        guessLabel.repaint();

        progressBar.setMax(game.getMAX_SCORE());
        progressBar.setValue(game.getScore());

        String scoreStatusString;
        double percentage = progressBar.getPercentComplete();
        if (percentage < 2) {
            scoreStatusString = "Beginner";
        } else if (percentage < 5) {
            scoreStatusString = "Good Start";
        } else if (percentage < 8) {
            scoreStatusString = "Moving Up";
        } else if (percentage < 15) {
            scoreStatusString = "Good";
        } else if (percentage < 25) {
            scoreStatusString = "Solid";
        } else if (percentage < 40) {
            scoreStatusString = "Nice";
        } else if (percentage < 50) {
            scoreStatusString = "Great";
        } else if (percentage < 70) {
            scoreStatusString = "Amazing";
        } else if (percentage < 100) {
            scoreStatusString = "Genius";
        } else {
            scoreStatusString = "Queen Bee";
        }
        scoreStatusLabel.setText(scoreStatusString);
        scoreStatusLabel.repaint();

        progressBar.repaint();

        wordAbbreviationsPanel.revalidate();
        wordAbbreviationsPanel.removeAll();
        List<SpellingBeeWord> words = game.getWords();
        int numRows = 19;
        int numPanels = 1 + words.size() / numRows;
        JPanel[] panels = new JPanel[numPanels];
        for (int i = 0; i < numPanels; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.Y_AXIS));
            panels[i].setPreferredSize(new Dimension(50, 400));
            panels[i].setBackground(RunSpellingBee.alabaster);
            //panels[i].setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        int row = 0;
        int panelIndex = 0;

        for (SpellingBeeWord word : words) {
            JLabel abbreviationLabel = new JLabel();
            if (word.isGuessed()) {
                abbreviationLabel.setFont(new Font("Serif", Font.BOLD, 18));
                abbreviationLabel.setForeground(new Color(0, 100, 0));
                abbreviationLabel.setText(" " + word.getWord());
                if (word.isPangram()) {
                    abbreviationLabel.setForeground(new Color(212, 175, 55));
                }
            } else {
                String firstTwo = word.getWord().substring(0, 2);
                abbreviationLabel.setFont(new Font("Serif", Font.PLAIN, 18));
                abbreviationLabel.setForeground(Color.BLACK);
                abbreviationLabel.setText(" " + word.getWord().length() + " " + firstTwo);
            }
            abbreviationLabel.setPreferredSize(new Dimension(120, 20));
            if (row > numRows) {
                panelIndex++;
                row = 1;
            } else {
                row++;
            }
            panels[panelIndex].add(abbreviationLabel);
        }
        wordAbbreviationsPanel.setLayout(new GridLayout(1, 3));
        for (JPanel panel : panels) {
            wordAbbreviationsPanel.add(panel);
        }
        JPanel spacerPanel = new JPanel();
        spacerPanel.setBackground(RunSpellingBee.alabaster);
        //spacerPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        JPanel spacerPanel2 = new JPanel();
        spacerPanel2.setBackground(RunSpellingBee.alabaster);
        //spacerPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        for (int i = panels.length; i < 3; i++) {
            if (i % 2 == 0) {
                wordAbbreviationsPanel.add(spacerPanel);
            } else {
                wordAbbreviationsPanel.add(spacerPanel2);
            }
        }
        wordAbbreviationsPanel.repaint();

        repaint();
    }

    private boolean keyTyped = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char letter = e.getKeyChar();
        if (Character.isLetter(letter)) {
            game.addLetter(letter);
        }
        int keyCode = e.getKeyCode();
        if (!keyTyped && keyCode == VK_ENTER) {
            game.checkGuess();
            keyTyped = true;
        }
        int guessLength = 69;
        if (keyCode == VK_BACK_SPACE) {
            guessLength = game.getGuess().length();
            game.deleteLetter();
        }
        if (!(guessLength == 0)) {
            updateGUI();
        }
        if (keyCode == 51) {
            shuffleLetters();
            updateGUI();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyTyped = false;
    }
}
