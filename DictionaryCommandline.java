import java.io.FileNotFoundException;
import java.util.Scanner;

public class DictionaryCommandline {
    DictionaryManagement dictManage = new DictionaryManagement();

    private final int spaceNoEng = 9;
    private final int spaceEngViet = 28;
    public Scanner sc = new Scanner(System.in);

    private void createOutput(int no, String target, String explain) {
        if (no == 0) {
            System.out.println("No       | English                     | Vietnamese");
            return;
        }
        System.out.print(no);
        for (int i = 0; i < spaceNoEng - String.valueOf(no).length(); i++) {
            System.out.print(" ");
        }
        System.out.print("| ");
        System.out.print(target);
        for (int i = 0; i < spaceEngViet - target.length(); i++) {
            System.out.print(" ");
        }
        System.out.print("| ");
        System.out.println(explain);
    }

    public void showAllWords() {
        System.out.println("Welcome to ViEnDictionary!!\n");
        for (int i = -1; i < dictManage.dict.getLength(); i++) {
            if (i != -1) {
                String newWord = dictManage.dict.vocab[i].getWordTarget();
                String newMeaning = dictManage.dict.vocab[i].getWordExplain();
                createOutput(i + 1, newWord, newMeaning);
            } 
            else {
                createOutput(i + 1, "", "");
            }
        }
    }

    public void dictionaryBasic() {
        dictManage.insertFromCommandLine();
        showAllWords();
    }

    public void dictionaryAdvanced() {
        try {
            dictManage.insertFromFile();
            showAllWords();
            dictManage.dictionaryLookup();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot read file!");
            e.printStackTrace();
        }
    }

    public void dictionarySearcher() {
        String pattern = sc.nextLine();
        dictManage.tree.autoComplete(pattern);
        //sc.close();
    }
}
