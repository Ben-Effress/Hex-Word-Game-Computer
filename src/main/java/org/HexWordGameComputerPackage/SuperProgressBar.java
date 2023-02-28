package org.HexWordGameComputerPackage;

import javax.swing.*;
import java.awt.*;

public class SuperProgressBar extends JProgressBar {


    private SpellingBeeGame game;

    private JProgressBar progressBar;

    private int progressBarWidth;

    private int progressBarHeight;

    private int radius = 15;


    public SuperProgressBar(SpellingBeeGame game, JProgressBar progressBar, int progressBarWidth, int progressBarHeight) {
        this.game = game;
        this.progressBar = progressBar;
        this.progressBarWidth = progressBarWidth;
        this.progressBarHeight = progressBarHeight;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double score = game.getScore();
        double maxScore = game.getMAX_SCORE();
        double decimalDone = score / maxScore;
        System.out.println(score + ", " + maxScore);
        System.out.println(decimalDone);
        int x = (int) (decimalDone * progressBarWidth);
        int y = 3;
        g.setColor(Color.BLACK);
        g.fillOval(x, y, radius, radius);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Serif", Font.BOLD, 100));
        g.drawString(String.valueOf(score), x - 3, y - 5);
    }
}
