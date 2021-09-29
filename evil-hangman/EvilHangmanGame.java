package hangman;

import java.io.*;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private int length = 0;

    private String GetWordRepresentation(String word, char letter) {
        StringBuilder representation = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c == letter) {
                representation.append(c);
            }
            else {
                representation.append('-');
            }
        }
        return representation.toString();
    }

    private int GetPositionScore(String s, char c) {
        int score = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                score += i - s.length();
            }
        }
        return score;
    }

    private String CombinePatterns(String p1, String p2) {
        if (p1.length() != p2.length()) {
            return pattern;
        }
        StringBuilder p3 = new StringBuilder();
        for (int i = 0; i < p1.length(); i++) {
            char c1 = p1.charAt(i);
            char c2 = p2.charAt(i);
            if (Character.isAlphabetic(c1)) {
                p3.append(c1);
            }
            else if (Character.isAlphabetic(c2)) {
                p3.append(c2);
            }
            else {
                p3.append('-');
            }
        }
        return p3.toString();
    }

    public Set<String> wordSet; // set of words of length <wordLength>
    public SortedSet<Character> usedLetters;    // set of letters already guessed
    public String pattern;   // e.g. "e--e" for "else", "elle", etc.

    public EvilHangmanGame() {
        wordSet = new HashSet<>();
        usedLetters = new TreeSet<>();
    }

    public int CountCharInStr(String s, char c) {
        return s.length() - s.replace(Character.toString(c), "").length();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        /* Step 1: make sure <wordLength> is valid (>= 2) */
        length = wordLength;
        if (length < 2) {
            throw new EmptyDictionaryException();
        }

        /* Step 2: clear/reinitialize <wordSet> */
        wordSet = new HashSet<>();

        /* Step 3: make sure <dictionary> is not empty */
        BufferedReader br = new BufferedReader(new FileReader(dictionary));
        String word = br.readLine();
        if (word == null) {
            throw new EmptyDictionaryException();
        }

        /* Step 4: add words in dictionary of length <length> to <wordSet> */
        while (word != null) {
            if (word.length() == length) {
                wordSet.add(word);
            }
            word = br.readLine();
        }

        /* Step 5: make sure <wordSet> is not empty after adding */
        if (wordSet.isEmpty()) {
            throw new EmptyDictionaryException();
        }

        /* Step 6: initialize <pattern> */
        String hyphen = "-";
        pattern = hyphen.repeat(wordLength);
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        /* Step 1: convert the guessed letter to lowercase */
        final char guess1 = Character.toLowerCase(guess);

        /* Step 2: make sure we haven't guessed this letter yet */
        if (usedLetters.contains(guess1)) {
            throw new GuessAlreadyMadeException();
        }
        else {
            usedLetters.add(guess1);
        }

        /* Step 3: create a hash table that maps word patterns to words */
        HashMap<String, ArrayList<String>> wordMap = new HashMap<>();   // partition of words in <wordSet> based on position of <guess>
        String key;
        ArrayList<String> value;
        for (String word : wordSet) {
            key = GetWordRepresentation(word, guess1);
            if (wordMap.containsKey(key)) {
                value = wordMap.get(key);
            }
            else {
                value = new ArrayList<>();
            }
            value.add(word);
            wordMap.put(key, value);
        }

        /* Step 4: find the "best" word pattern of words */
        int size = 0;
        for (ArrayList<String> wordList : wordMap.values()) {
            size = Math.max(wordList.size(), size);
        }
        final int max_size = size;
        wordMap.values().removeIf(v -> v.size() != max_size);

        int count = length;
        int score = -length * length;
        for (String family : wordMap.keySet()) {
            if (family.indexOf(guess1) < 0) {
                wordSet = new HashSet<>(wordMap.get(family));
                pattern = CombinePatterns(family, pattern);
                return wordSet;
            }
            count = Math.min(CountCharInStr(family, guess1), count);
            score = Math.max(GetPositionScore(family, guess1), score);
        }
        final int min_count = count;
        wordMap.keySet().removeIf(k -> CountCharInStr(k, guess1) != min_count);
        final int max_score = score;
        wordMap.keySet().removeIf(k -> GetPositionScore(k, guess1) != max_score);

        /* Step 5: update the pattern & output the set of words */
        wordSet = new HashSet<>(wordMap.values().iterator().next());
        pattern = CombinePatterns(wordMap.keySet().toArray()[0].toString(), pattern);
        return wordSet;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return usedLetters;
    }
}

