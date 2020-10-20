import java.util.ArrayList;

public class Dictionary {
    public ArrayList<Word> wordsList;
    public ArrayList<String> vocabList;

    public Dictionary() {
        wordsList = new ArrayList<Word>();
        vocabList = new ArrayList<String>();
    }

    public void addWordBeginning(Word word) {
        wordsList.add(word);
        vocabList.add(word.getWordTarget());
    }

    public void addWord(Word word) {
        if(wordsList.size() == 0) {
            wordsList.add(word);
            vocabList.add(word.getWordTarget());
        }
        else {
            int index = getIndexToAdd(word);
            wordsList.add(index, word);
            vocabList.add(index, word.getWordTarget());
        }
    }

    public int getIndexToAdd(Word word) {
        int i;
        for(i = 0; i < wordsList.size(); i++) {
            if(vocabList.get(i).compareTo(word.getWordTarget()) > 0) {
                break;
            }
        }
        return i;
    }

    public void deleteWord(Word word) {
        if(word == null) {
            return;
        }
        if(vocabList.contains(word.getWordTarget())) {
            wordsList.remove(word);
            vocabList.remove(word.getWordTarget());
        }
    }
}
