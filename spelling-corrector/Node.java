package spell;

public class Node implements INode {
    public int count = 0;
    public Node[] nodes = new Node[26];

    @Override
    public int getValue() {
        return count;
    }

    public void incrementValue() {
        count++;
    }

    public Node[] getChildren() {
        return nodes;
    }
}
