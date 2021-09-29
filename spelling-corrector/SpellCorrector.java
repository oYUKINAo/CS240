package spell;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.*;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
    private final Trie dictionary = new Trie();
    private final String alphabets = "abcdefghijklmnopqrstuvwxyz";

    private void getDeletionWords(String inputWord, TreeSet<String> candidateSet) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < inputWord.length(); j++) {
                if (j != i) {
                    word.append(inputWord.charAt(j));
                }
            }
            candidateSet.add(word.toString());
            word.setLength(0);
        }
    }

    private void getTranspositionWords(String inputWord, TreeSet<String> candidateSet) {
        char[] characters;
        char temp;
        String word;
        for (int i = 0; i < inputWord.length() - 1; i++) {
            characters = inputWord.toCharArray();
            temp = characters[i];
            characters[i] = characters[i+1];
            characters[i+1] = temp;
            word = new String(characters);
            candidateSet.add(word);
        }
    }

    private void getAlterationWords(String inputWord, TreeSet<String> candidateSet) {
        char[] characters;
        String word;
        for (int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < alphabets.length(); j++) {
                characters = inputWord.toCharArray();
                characters[i] = alphabets.charAt(j);
                word = new String(characters);
                candidateSet.add(word);
            }
        }
        candidateSet.remove(inputWord); // get rid of the original word from the candidate words
    }

    private void getInsertionWords(String inputWord, TreeSet<String> candidateSet) {
        String word;
        for (int i = 0; i <= inputWord.length(); i++) {
            for (int j = 0; j < alphabets.length(); j++) {
                word = new StringBuilder(inputWord).insert(i, alphabets.charAt(j)).toString();
                candidateSet.add(word);
            }
        }
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File f = new File(dictionaryFileName);
        FileReader fr = new FileReader(f);
        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            String[] words;
            while ((line = br.readLine()) != null) {
                words = line.split(" ", 0);
                for (int i = 0; i < words.length; i++) {
                    dictionary.add(words[i]);
                }
            }
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if (dictionary.find(inputWord.toLowerCase()) != null) { // if the dictionary contains the lowercase version of inputWord, then we return it immediately
            return inputWord.toLowerCase();
        }

        String inputWordLower = inputWord.toLowerCase();

        TreeSet<String> primary_candidates = new TreeSet<>(); // 0: the set of all strings that are edit distance 1 from inputWord

        getDeletionWords(inputWordLower, primary_candidates);
        getTranspositionWords(inputWordLower, primary_candidates);
        getAlterationWords(inputWordLower, primary_candidates);
        getInsertionWords(inputWordLower, primary_candidates);

        TreeSet<String> semifinalists = new TreeSet<>();    // 1: the set of all strings (words) in dictionary that are in 0
        for (String string : primary_candidates) {
            if (dictionary.find(string) != null) {
                semifinalists.add(string);
            }
        }

        if (semifinalists.isEmpty()) {
            TreeSet<String> secondary_candidates = new TreeSet<>();   // 0.5: the set of all strings that are edit distance 2 from inputWord
            for (String string : primary_candidates) {
                getDeletionWords(string, secondary_candidates);
                getTranspositionWords(string, secondary_candidates);
                getAlterationWords(string, secondary_candidates);
                getInsertionWords(string, secondary_candidates);
            }
            for (String string : secondary_candidates) {
                if (dictionary.find(string) != null) {
                    semifinalists.add(string);
                }
            }
            if (semifinalists.isEmpty()) {
                return null;
            }
        }

        int max_appearance = 0;
        for (String word : semifinalists) {
            max_appearance = Math.max(dictionary.find(word).count, max_appearance);
        }

        TreeSet<String> finalists = new TreeSet<>();    // 2: a set of words in 1 that appears the greatest number of times in the dictionary
        for (String word : semifinalists) {
            if (dictionary.find(word).count == max_appearance) {
                finalists.add(word);
            }
        }

        String winner = finalists.first();    // 3: the word that comes first alphabetically in 2
        if (winner != null) {
            return winner;
        }
        return null;
    }
}