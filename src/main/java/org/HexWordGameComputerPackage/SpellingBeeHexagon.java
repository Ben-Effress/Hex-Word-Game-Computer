package org.HexWordGameComputerPackage;

import java.awt.*;

public class SpellingBeeHexagon extends Polygon {

    // Letter for that hexagon
    private String letter;

    public static final int radius = 50;
    public static final int yOffset = (int) ((.875) * radius);

    private final int xCenter;
    private final int yCenter;

    private final boolean center;

    public SpellingBeeHexagon(String letter, int xCenter, int yCenter, boolean center) {
        this.letter = letter;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.center = center;
        this.addPoint(xCenter - radius, yCenter);
        this.addPoint(xCenter - radius/2, yCenter - yOffset);
        this.addPoint(xCenter + radius/2, yCenter - yOffset);
        this.addPoint(xCenter + radius, yCenter);
        this.addPoint(xCenter + radius/2, yCenter + yOffset);
        this.addPoint(xCenter - radius/2, yCenter + yOffset);
    }

    public String getLetter() {
        return this.letter;
    }

    public void setLetter(String s) {
        this.letter = s;
    }

    public static int getRadius() {
        return radius;
    }

    public int getXCenter() {
        return this.xCenter;
    }

    public int getYCenter() {
        return this.yCenter;
    }

    public boolean isCenter() {
        return this.center;
    }
}
