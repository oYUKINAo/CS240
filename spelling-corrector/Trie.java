package spell;

public class Trie implements ITrie{
    private int WordCount = 0;
    private int NodeCount = 1;
    private final Node root = new Node();
    private final String alphabets = "abcdefghijklmnopqrstuvwxyz";

    public void add(String word) {
        Node current_node = root;
        for (int i = 0; i < word.length(); i++) {   // iterate over each letter of the word
            char letter = word.charAt(i);
            int position = letter - 'a';
            if (current_node.nodes[position] == null) { // if the current substring isn't part of a word already in the trie, then we create a new node
                current_node.nodes[position] = new Node();
                NodeCount++;
            }
            current_node = current_node.nodes[position];
        }
        if (current_node.count == 0) {
            WordCount++;
        }
        current_node.count++;
    }

    public Node find(String word) {
        Node current_node = root;
        for (int i = 0; i < word.length(); i++) {   // again, iterate over each letter of the word
            char letter = word.charAt(i);
            int position = letter - 'a';
            if (current_node.nodes[position] == null) { // if the current substring isn't part of a word already in the trie, then the word is not in the trie
                return null;
            }
            current_node = current_node.nodes[position];
        }
        if (current_node.count >= 1) {  // if the word appeared at least once in the trie, then we return the reference to the trie node that represents the word
            return current_node;
        }
        else {
            return null;
        }
    }

    public int getWordCount() {
        return WordCount;
    }

    public int getNodeCount() {
        return NodeCount;
    }

    @Override
    public String toString() {
        StringBuilder words = new StringBuilder();
        StringBuilder word = new StringBuilder();
        toStringHelper(word, words, root);
        if (words.length() >= 1) {
            words.deleteCharAt(words.length() - 1); // get rid of the trailing new line
        }
        return words.toString();
    }
    private void toStringHelper(StringBuilder one_word, StringBuilder all_words, Node trie_node) {
        for (int i = 0; i < alphabets.length(); i++) {  // iterate over all 26 alphabets
            Node letter_node = trie_node.nodes[i];
            if (letter_node != null) {
                one_word.append(alphabets.charAt(i));
                if (letter_node.count >= 1) {
                    all_words.append(one_word.toString());
                    all_words.append("\n");
                }
                toStringHelper(one_word, all_words, letter_node);
                one_word.setLength(0);
            }
        }
    }

    @Override
    public int hashCode() {
        int index = -1;
        for (int i = 0; i < root.nodes.length; i++) {
            if (root.nodes[i] != null) {
                index = i;
                break;
            }
        }
        return index * NodeCount * WordCount;
    }

    @Override
    public boolean equals(Object o) {
        Trie t = (Trie) o;
        if (t == null) {
            return false;
        }
        return equalsHelper(t.root, root);
    }
    private boolean equalsHelper(Node trie1_node, Node trie2_node) {
        if (trie1_node.count != trie2_node.count) {
            return false;
        }
        boolean equal;
        for (int i = 0; i < alphabets.length(); i++) {  // again, iterate over all 26 alphabets
            if (trie1_node.nodes[i] != null && trie2_node.nodes[i] != null) {
                equal = equalsHelper(trie1_node.nodes[i], trie2_node.nodes[i]);
                if (!equal) {
                    return false;
                }
            }
            else if (trie1_node.nodes[i] == null && trie2_node.nodes[i] == null) {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }
}