public class DictionaryCommandline {
    DictionaryManagement dictManage = new DictionaryManagement();

    final int spaceNoEng = 9;
    final int spaceEngViet = 28;

    public void createOutput(int no, String target, String explain) {
        System.out.print(no);
        for(int i = 0; i < spaceNoEng - String.valueOf(no).length(); i++) {
            System.out.print(" ");
        }
        System.out.print("| ");
        System.out.print(target);
        for(int i = 0; i < spaceEngViet - target.length(); i++) {
            System.out.print(" ");
        }
        System.out.print("| ");
        System.out.println(explain);
    }

    public void showAllWords() {
        for(int i = -1; i < dictManage.dict.getLength(); i++) {
            if(i == -1) {
                System.out.println("No       | English                     | Vietnamese");
                continue;
            }
            String newWord = dictManage.dict.vocab[i].getWordTarget();
            String newMeaning = dictManage.dict.vocab[i].getWordExplain();
            createOutput(i + 1, newWord, newMeaning);
        }
    }

    public void dictionaryBasic() {
        dictManage.insertFromCommandLine();
        showAllWords();
    }
}
