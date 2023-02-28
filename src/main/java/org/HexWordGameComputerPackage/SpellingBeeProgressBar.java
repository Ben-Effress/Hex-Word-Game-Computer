package org.HexWordGameComputerPackage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SpellingBeeProgressBar extends JPanel {

    private double score = 0;

    private double max;

    private int width;

    private int height;

    private SpellingBeeGame game;

    public SpellingBeeProgressBar(SpellingBeeGame game, int width, int height) {
        this.game = game;
        this.max = game.getMAX_SCORE();
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int offset = 14;
        this.setBackground(RunSpellingBee.alabaster);
        this.score = game.getScore();
        this.max = game.getMAX_SCORE();
        double drawHeight = 1;
        int y = (int) (height / 2 + drawHeight + 3);
        int rectWidth = width - 10;

        g.setColor(Color.GRAY);
        g.fillRect(offset, y, rectWidth, (int) drawHeight);

        int segmentWidth = (int) (rectWidth / 9.0);

        ArrayList<Double> xValues = new ArrayList<>();
        double beginnerValue = 0;
        double goodStartValue = (max * .02) - beginnerValue;
        double movingUpValue = (max * .05) - goodStartValue;
        double goodValue = (max * .08) - movingUpValue;
        double solidValue = (max * .15) - goodValue;
        double niceValue = (max * .25) - solidValue;
        double greatValue = (max * .40) - niceValue;
        double amazingValue = (max * .50) - greatValue;
        double geniusValue = (max * .70) - amazingValue;
        double queenBeeValue = max - geniusValue;
        //xValues.add(beginnerValue);
        xValues.add(goodStartValue);
        xValues.add(movingUpValue);
        xValues.add(goodValue);
        xValues.add(solidValue);
        xValues.add(niceValue);
        xValues.add(greatValue);
        xValues.add(amazingValue);
        xValues.add(geniusValue);
        xValues.add(queenBeeValue);

        double drawWidthMultiplier = 0;
        double tempScore = score;

        for (double value : xValues) {
            double progressInSegment = tempScore / value;
            if (progressInSegment >= 1) {
                drawWidthMultiplier++;
                tempScore -= value;
            } else if (progressInSegment >= 0) {
                drawWidthMultiplier += progressInSegment;
                tempScore -= value;
            }
        }

        int drawWidth = (int) (segmentWidth * drawWidthMultiplier);

        int radius = 10;
        for (int i = 0; i < 10; i++) {
            int valueOvalX = offset + i * segmentWidth;
            if (drawWidth + offset > valueOvalX) {
                g.setColor(new Color(212, 175, 55));
            } else {
                g.setColor(Color.GRAY);
            }
            if (i == 9) {
                int rectRadius = radius + 1;
                int rectX = offset + 9 * segmentWidth;
                g.fillRect(rectX - rectRadius, height / 2 - rectRadius / 2, rectRadius * 2, rectRadius * 2);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Serif", Font.BOLD, 12));
                int maxScoreDivide = (int) (max / 10);
                if (maxScoreDivide < 1) {
                    g.drawString(String.valueOf((int) max), rectX + radius - 13, (int) (y + drawHeight + 5));
                } else if (maxScoreDivide < 10) {
                    g.drawString(String.valueOf((int) max), rectX + radius - 16, (int) (y + drawHeight + 5));
                } else {
                    g.drawString(String.valueOf((int) max), rectX + radius - 19, (int) (y + drawHeight + 5));
                }
            } else {
                g.fillOval(valueOvalX, height / 2, radius, radius);
            }
        }



        int ovalRadius = radius + 3;
        int ovalY = height / 2;
        g.setColor(new Color(212, 175, 55));
        g.fillOval(offset + drawWidth - ovalRadius, ovalY - radius, ovalRadius * 2, ovalRadius * 2);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, 12));
        int scoreDivide = (int) (score / 10);
        if (scoreDivide < 1) {
            g.drawString(String.valueOf((int) score), drawWidth + ovalRadius - 2, (int) (y + drawHeight + 2));
        } else if (scoreDivide < 10) {
            g.drawString(String.valueOf((int) score), drawWidth + ovalRadius - 5, (int) (y + drawHeight + 2));
        } else {
            g.drawString(String.valueOf((int) score), drawWidth + ovalRadius - 8, (int) (y + drawHeight + 2));
        }



    }

    public void setValue(int value) {
        this.score = value;
    }

    public int getValue() {
        return (int) this.score;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return (int) this.max;
    }

    public double getPercentComplete() {
        return 100 * (score / max);
    }
}
