public class Dictionary {
    private int length;
    public Word[] vocab;

    public void setLength(int len) {
        length = len;
        vocab = new Word[length];
        for(int i = 0; i < length; i++) {
            vocab[i] = new Word();
        }
    }
    public int getLength() {
        return length;
    }
}
