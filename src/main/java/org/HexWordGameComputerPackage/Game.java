package org.HexWordGameComputerPackage;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {

        Runnable game = new RunSpellingBee();

        SwingUtilities.invokeLater(game);
    }
}
