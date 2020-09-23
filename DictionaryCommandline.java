// A BIG MESS
// NEED TO BE FIXED

public class DictionaryCommandline {
    public void showAllWords() {
        System.out.println("No       | English                     | Vietnamese");
        Dictionary dict = new Dictionary();
        for(int i = 0; i < dict.getLength(); i++) {
            String newWord = dict.vocab[i].getWordTarget();
            String newMeaning = dict.vocab[i].getWordExplain();
            System.out.println((i + 1) + "       | " + newWord + "                     | " + newMeaning);
        }
    }
    public void dictionaryBasic() {
        DictionaryManagement dictManage = new DictionaryManagement();
        dictManage.insertFromCommandLine();
        showAllWords();
    }
}
