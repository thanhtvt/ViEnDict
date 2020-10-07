public class Dictionary {
    private int length;
    private final int SIZE_OF_DICT = 60000;
    public Word[] vocab;

    public Dictionary() {
        vocab = new Word[SIZE_OF_DICT];
        for(int i = 0; i < SIZE_OF_DICT; i++) {
            vocab[i] = new Word();
        }
    }

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
