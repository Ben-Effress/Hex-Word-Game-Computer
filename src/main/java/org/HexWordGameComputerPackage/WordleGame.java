package org.HexWordGameComputerPackage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WordleGame {
    // The correct word for the current round
    private String correctWord;
    // The current guess
    private List<Character> guess = new LinkedList<>();
    // The 6x5 array of integers representing how correct each element in the grid
    // is
    private int[][] correctness = new int[6][5];
    // The 6x5 array of characters representing the letters in each box of the grid
    private char[][] letters = new char[6][5];
    // A random number generator
    private Random rand = new Random();
    // The current row and column in the letters array
    private int currentRow = 0;
    private int currentColumn = 0;

    // The history JTextArea to add the current round results to and repaint
    private JTextArea history;

    // Constructor
    public WordleGame(JTextArea history) {
        this.history = history;

        // Check if there is a saved game state
        try (BufferedReader reader = new BufferedReader(new FileReader("files/current.txt"))) {
            String line;
            String[] lettersStrings;
            String[] correctnessStrings;
            if ((line = reader.readLine()) != null) {
                correctWord = line;
                System.out.println(correctWord);
                lettersStrings = reader.readLine().split(",");
                correctnessStrings = reader.readLine().split(",");
                currentRow = Integer.parseInt(reader.readLine());
                int index = 0;
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        letters[i][j] = lettersStrings[index].charAt(0);
                        correctness[i][j] = Integer.parseInt(correctnessStrings[index]);
                        index++;
                    }
                }
            } else {
                // No saved state
                // Choose a new random word
                chooseRandomWord();

                // Write the initial state to the current.txt file
                writeCurrentState();
                // Initialize the arrays to their default values
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        correctness[i][j] = 0;
                        letters[i][j] = ' ';
                    }
                }
            }
        } catch (IOException e) {
            // Couldn't find current.txt
            // Choose a new random word
            chooseRandomWord();

            // Write the initial state to the current.txt file
            writeCurrentState();
            // Initialize the arrays to their default values
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    correctness[i][j] = 0;
                    letters[i][j] = ' ';
                }
            }
        }
    }

    // Chooses a random word from the words.txt file and sets it as the correct word
    private void chooseRandomWord() {
        try {
            // Read the words.txt file and store the words in a list
            List<String> words = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("files/words.txt"));
            String line = reader.readLine();
            while (line != null) {
                words.add(line);
                line = reader.readLine();
            }
            reader.close();

            // Choose a random word from the list
            int index = rand.nextInt(words.size());
            correctWord = words.get(index);
            System.out.println(correctWord);
        } catch (IOException e) {
            System.out.println("Error reading words.txt: " + e.getMessage());
        }
    }

    // Adds the given letter to the guess linked list and to the letters array
    public void addLetter(char letter) {
        if (guess.size() < 5 && Character.isLetter(letter)) {
            guess.add(Character.toLowerCase(letter));
            letters[currentRow][currentColumn] = letter;
            currentColumn++;
        }
    }

    // Clears the guess linked list and the letters array
    public void clearGuess() {
        guess.clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                letters[i][j] = ' ';
                correctness[i][j] = 0;
            }
        }
        currentRow = 0;
        currentColumn = 0;
    }

    // Checks the current guess against the correct word and updates the correctness
    // and letters arrays
    public void checkGuess() {
        if (guess.size() == 5) {
            // Check each letter of the guess against the correct word
            for (int i = 0; i < 5; i++) {
                char g = guess.get(i);
                if (g == correctWord.charAt(i)) {
                    // The letter is in the correct position
                    correctness[currentRow][i] = 2;
                } else if (correctWord.indexOf(g) >= 0) {
                    // The letter is in the word, but in the wrong position
                    correctness[currentRow][i] = 1;
                }
            }
            // Check if the guess was correct
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                sb.append(guess.get(i));
            }
            String guessWord = sb.toString();
            if (guessWord.equals(correctWord)) {
                // The guess was correct, update the status and prepare for the next round
                appendToHistory(correctWord, currentRow + 1);
                history.append(correctWord + "," + (currentRow + 1) + "\n");
                history.repaint();
                currentRow = 0;
                currentColumn = 0;
                chooseRandomWord();
                clearGuess();
                writeCurrentState();
            } else {
                // The guess was incorrect, move to the next row
                currentRow++;
                currentColumn = 0;
                guess.clear();
                writeCurrentState();
                if (currentRow == 6) {
                    // The grid is full, update the status and prepare for the next round
                    appendToHistory(correctWord, 7);
                    history.append(correctWord + "," + "X" + "\n");
                    history.repaint();
                    currentRow = 0;
                    currentColumn = 0;
                    chooseRandomWord();
                    clearGuess();
                    writeCurrentState();
                }
            }
        }
    }

    // Appends the given word and the number of guesses it took to solve it to the
    // history.txt file
    private void appendToHistory(String word, int numGuesses) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("files/history.txt", true));
            if (numGuesses < 7) {
                writer.write(word + "," + numGuesses);
            } else {
                writer.write(word + "," + "X");
            }
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to history.txt: " + e.getMessage());
        }
    }

    // Writes the current game state to the current.txt file
    private void writeCurrentState() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("files/current.txt"));
            writer.write(correctWord);
            writer.newLine();
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    writer.write(letters[i][j]);
                    if (i != 5 || j != 4) {
                        writer.write(",");
                    }
                }
            }
            writer.newLine();
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    writer.write("" + correctness[i][j]);
                    if (i != 5 || j != 4) {
                        writer.write(",");
                    }
                }
            }
            writer.newLine();
            writer.write("" + currentRow);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to current.txt: " + e.getMessage());
        }
    }

    // Returns the correctness array
    public int[][] getCorrectness() {
        return correctness.clone();
    }

    // Returns the letters array
    public char[][] getLetters() {
        return letters.clone();
    }

    // Deletes the most recently added letter from the guess
    public void deleteLetter() {
        if (guess.size() > 0) {
            guess.remove(guess.size() - 1);
            currentColumn--;
            letters[currentRow][currentColumn] = ' ';
        }
    }

    // (For testing) Changes the correct word to the given word
    public void setCorrectWord(String newCorrect) {
        correctWord = newCorrect;
    }
}
