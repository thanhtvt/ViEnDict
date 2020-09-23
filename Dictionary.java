public class Dictionary {
    private int length;
    public Word[] vocab;

    public void setLength(int len) {
        length = len;
    }
    public int getLength() {
        return length;
    }

    Dictionary(int len) {
        length = len;
        vocab = new Word[length];
    }
    Dictionary() {}
}
