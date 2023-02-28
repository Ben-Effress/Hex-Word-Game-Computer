package org.HexWordGameComputerPackage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SpellingBeeGame {

    // Letters of the current round
    private List<String> letters = new ArrayList<>();

    private String middleLetter;

    // Correct words for the round
    private List<SpellingBeeWord> words = new ArrayList<>();

    private List<SpellingBeeWord> pangrams = new ArrayList<>();

    // The current guess
    private String guess;

    // A random number generator
    private final Random rand = new Random();

    // Name of current JSON round file
    private String JSONName;

    private int score = 0;

    private int MAX_SCORE;

    private JLabel guessResultLabel;

    private JPanel guessResultPanel;

    private int numWipers = 0;

    // Going to have to take in the labels!
    public SpellingBeeGame(JLabel guessResultLabel, JPanel guessResultPanel) {
        this.guessResultLabel = guessResultLabel;
        this.guessResultPanel = guessResultPanel;

        startNewGame();

    }

    public void startNewGame() {
        score =  0;

        //Initializes JSONName
        chooseRandomRound();

        // Get the letters for the round
        letters.clear();
        char[] chars = this.JSONName.substring(0, 7).toCharArray();
        for (char c : chars) {
            letters.add(Character.toString(c));
            System.out.println(c);
        }

        this.middleLetter = String.valueOf(chars[0]);

        this.guess = "";

        // Initializes words
        getRoundData();
    }

    private void chooseRandomRound() {
        try {

            // Read the words.txt file and store the words in a list
            List<String> names = new ArrayList<>();;
            BufferedReader reader = new BufferedReader(new FileReader("JSONNames"));
            String line = reader.readLine();
            while (line != null) {
                names.add(line);
                line = reader.readLine();
            }
            reader.close();

            // Choose a random word from the list
            int index = rand.nextInt(names.size());
            this.JSONName = names.get(index);
            index = rand.nextInt(names.size());

        } catch (IOException e) {
            System.out.println("Error reading JSONNames.txt: " + e.getMessage());
        }
    }

    public void getRoundData() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("data/" + JSONName)) {
        //try (FileReader reader = new FileReader((FileReader) getClass().getResourceAsStream("resources/filename.txt"))) {
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            JSONArray wordsArray = (JSONArray) obj.get("word_list");
            // Initializes words
            words.clear();
            wordsArray.forEach(this::parseWord);

            // Initializes MAX_SCORE
            this.MAX_SCORE = words.stream().map(SpellingBeeWord::getScore).reduce(0, Integer::sum);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseWord(Object wordsArrayObject) {
        JSONObject wordsArrayJSONObject = (JSONObject) wordsArrayObject;
        SpellingBeeWord wurd;
        String newWord = (String) wordsArrayJSONObject.get("word");
        System.out.println(newWord);
        boolean newIsPangram = (boolean) wordsArrayJSONObject.get("pangram");
        int newScore;
        if (newWord.length() == 4) {
            newScore = 1;
        } else {
            newScore = newWord.length();
            if (newIsPangram) {
                newScore *= 2;
            }
        }
        System.out.println(newScore);
        wurd = new SpellingBeeWord(newWord, newScore, newIsPangram);
        words.add(wurd);
        if (newIsPangram) {
            pangrams.add(wurd);
        }
    }

    public List<String> getLetters() {
        return this.letters;
    }

    public List<SpellingBeeWord> getWords() {
        return this.words;
    }

    public void addLetter(char c) {
        guess += (String.valueOf(Character.toUpperCase(c)));
    }

    public void deleteLetter() {
        if (guess.length() > 0) {
            guess = guess.substring(0, guess.length() - 1);
        }
    }

    public String getGuess() {
        return this.guess;
    }

    public void checkGuess() {
        if (guess.contains(middleLetter)) {
            if (guess.length() > 3) {
                for (SpellingBeeWord wurd : words) {
                    if (guess.equals(wurd.getWord())) {
                        guessResultLabel.setFont(new Font("Serif", Font.PLAIN, 20));
                        if (wurd.isGuessed()) {
                            // CHANGE STATUS LABEL - Already Guessed
                            guessResultLabel.setForeground(Color.RED);
                            guessResultLabel.setText("Already Guessed!");
                            break;
                        }
                        if(wurd.isPangram()) {
                            // CHANGE STATUS LABEL - (Bold)Pangram! +<score>(Bold)
                            guessResultLabel.setForeground(new Color(212, 175, 55));
                            guessResultLabel.setFont(new Font("Serif", Font.BOLD, 20));
                            guessResultLabel.setText("PANGRAM +" + wurd.getScore());

                        } else {
                            // CHANGE STATUS LABEL - Nice! +<score>
                            guessResultLabel.setFont(new Font("Serif", Font.BOLD, 20));
                            guessResultLabel.setForeground(new Color(0, 100, 0));
                            guessResultLabel.setText("Nice! +" + wurd.getScore());

                        }
                        score += wurd.getScore();
                        wurd.guessed();
                        break; // When correct word
                    } else {
                        // CHANGE STATUS LABEL - Not in word list
                        guessResultLabel.setForeground(Color.RED);
                        guessResultLabel.setText("Not in word list");
                    }
                }
            } else {
                // CHANGE STATUS LABEL - Too short
                guessResultLabel.setForeground(Color.RED);
                guessResultLabel.setText("Too short");
            }
        } else {
            // CHANGE STATUS LABEL - Word must contain middle letter
            guessResultLabel.setForeground(Color.RED);
            guessResultLabel.setText("Word must contain middle letter");
        }
        numWipers++;
        guess = "";
        TimerTask task = new TimerTask() {
            public void run() {
                if (numWipers == 1){
                    guessResultLabel.setText("");
                }
                numWipers--;
            }
        };
        Timer timer = new Timer();

        long delay = 1500;
        timer.schedule(task, delay);
    }

    public int getScore() {
        return this.score;
    }

    public int getMAX_SCORE() {
        return this.MAX_SCORE;
    }

    public static void main(String[] args) {
        SpellingBeeGame test = new SpellingBeeGame(new JLabel(), new JPanel());
        test.addLetter('a');
        test.addLetter('b');
        test.addLetter('A');
        test.addLetter('c');
        test.addLetter('k');
        test.addLetter('E');
        test.deleteLetter();
        test.checkGuess();
    }

}
