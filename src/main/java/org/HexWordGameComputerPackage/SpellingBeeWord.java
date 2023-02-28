package org.HexWordGameComputerPackage;

public class SpellingBeeWord {

    private final String word;

    private final int score;

    private final boolean isPangram;

    private boolean guessed;

    public SpellingBeeWord(String word, int score, boolean isPangram) {
        this.word = word;
        this.score = score;
        this.isPangram = isPangram;
        this.guessed = false;
    }

    public String getWord() {
        return this.word;
    }

    public int getScore() {
        return this.score;
    }

    public boolean isPangram() {
        return this.isPangram;
    }

    public boolean isGuessed() {
        return this.guessed;
    }

    public void guessed() {
        this.guessed = true;
    }

}
