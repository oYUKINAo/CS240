package hangman;

import java.io.*;
import java.util.*;

public class EvilHangman {

    public static void main(String[] args) {
        String path = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        File file = new File(path);

        EvilHangmanGame game = new EvilHangmanGame();

        try {
            game.startGame(file, wordLength);
        }
        catch (IOException | EmptyDictionaryException error) {
            System.out.println("The dictionary is empty.");
        }

        boolean keepGuessing = true;
        while (guesses > 0 && keepGuessing) {
            if (guesses == 1) {
                System.out.println("You have 1 guess left");
            }
            else {
                System.out.println("You have " + guesses + " guesses left");
            }

            System.out.print("Used letters:");
            for (Character letter : game.usedLetters) {
                System.out.print(" " + letter);
            }
            System.out.println();

            System.out.println("Word: " + game.pattern);

            Scanner s = new Scanner(System.in);
            boolean iterate = true;
            char guess = ' ';
            while (iterate) {
                System.out.print("Enter guess: ");
                guess = s.next().charAt(0);
                if (Character.isAlphabetic(guess)) {
                    iterate = false;
                }
                else {
                    System.out.println("Invalid input.");
                }
            }

            try {
                Set<String> candidates = game.makeGuess(guess);
                int count = 0;
                for (String word : candidates) {
                    if (word.contains(Character.toString(guess))) {
                        count = Math.min(game.CountCharInStr(word, guess), wordLength);
                    }
                }
                if (count == 0) {
                    guesses--;
                    System.out.println("Sorry, there are no " + guess + "'s");
                }
                else {
                    if (count == 1) {
                        System.out.println("Yes, there is " + count + " " + guess);
                    }
                    else {
                        System.out.println("Yes, there are " + count + " " + guess);
                    }
                    if (!game.pattern.contains("-")) {
                        System.out.println("You win!");
                        System.out.println("The word was: " + game.pattern);
                        keepGuessing = false;
                    }
                }
            }
            catch (GuessAlreadyMadeException error) {
                System.out.println("You already used that letter.");
            }
        }
        if (keepGuessing) {
            String[] wordArray = game.wordSet.toArray(new String[game.wordSet.size()]);
            Random random = new Random();
            int x = random.nextInt(wordArray.length);
            String word = wordArray[x];
            boolean win = true;
            for (int i = 0; i < word.length(); i++) {
                if (!game.usedLetters.contains(word.charAt(i))) {
                    win = false;
                    System.out.println("You lose!");
                    System.out.println("The word was: " + word);
                    break;
                }
            }
            if (win) {
                System.out.println("You win!");
                System.out.println("The word was: " + word);
            }
        }

    }
}