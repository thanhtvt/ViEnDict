public class Word {
    private String word_target;
    private String word_explain;

    public Word() {}

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public void setWordTarget(String target) {
        word_target = target;
    }
    public String getWordTarget() {
        return word_target;
    }
    public void setWordExplain(String explain) {
        word_explain = explain;
    }
    public String getWordExplain() {
        return word_explain;
    }
}
