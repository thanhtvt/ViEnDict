public class Node {
    private char data;
    private boolean isEndOfString;
    Node left, middle, right;

    public Node(char data) {
        this.data = data;
        this.isEndOfString = false;
        this.left = null;
        this.middle = null;
        this.right = null;
    }

    public void setData(char data) {
        this.data = data;
    }
    public char getData() {
        return data;
    }
    public void setEnding(boolean end) {
        isEndOfString = end;
    }
    public boolean getEnding() {
        return isEndOfString;
    }
}
