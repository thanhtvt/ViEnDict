import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class DictionaryManagement {
    public Dictionary dict = new Dictionary();
    public Map<String, String> wordPair = new HashMap<String, String>();
    public TernarySearchTree tree = new TernarySearchTree();
    public Scanner sc = new Scanner(System.in);

    /**
     * Insert data for dictionary from file.
     * @return number of words in file.
     * @throws FileNotFoundException.
     */
    public void insertFromFile() {
        File file = new File("data.dict");
        Scanner sc;

        // Read from file
        try {
            sc = new Scanner(file);
            String lexicon, definition;
            while (sc.hasNextLine()) {
                String temp = "10";
                String firstLine = sc.nextLine();
                int pos = firstLine.indexOf('/');
                if(pos > 0) {
                    lexicon = firstLine.substring(1, pos - 1);
                } 
                else {
                    lexicon = firstLine.substring(1);
                }
                definition = firstLine.substring(1);
                while(Character.compare(temp.charAt(0), '@') != 0) {
                    temp = sc.nextLine();
                    if(temp.equals("")) {
                        break;
                    }
                    definition = definition + "\n" + temp;
                    if(!sc.hasNextLine()) {
                        break;
                    }
                }
                Word word = new Word(lexicon, definition);
                dict.addWordBeginning(word);
                wordPair.put(lexicon, definition);
                tree.insert(lexicon);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot read file");
            e.printStackTrace();
        }
    }

    public void dictionaryLookup() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Your word is: ");
        while (sc.hasNext()) {
            String target = sc.next();
            if (wordPair.get(target) != null) {
                System.out.println("It means: " + wordPair.get(target));
            } 
            else {
                System.out.println("Not found! Please wait for my upcoming update.");
            }
            System.out.print("Your word is: ");
        }
        sc.close();
    }

    public void addWord() {        
        System.out.print("The word you add is: ");
        String newWord = sc.nextLine();
        System.out.print("It means: ");
        String newMeaning = sc.nextLine();

        Word word = new Word(newWord, newMeaning);
        dict.addWord(word);
        wordPair.put(newWord, newMeaning);
        tree.insert(newWord);
    }

    public void deleteWord(String wordTarget) {
        Word word = lookup(wordTarget);
        if(word != null) {
            int index = dict.wordsList.indexOf(word);
            dict.deleteWord(word);
            wordPair.remove(dict.vocabList.get(index));
            tree.delete(dict.vocabList.get(index));
        }
    }

    private Word lookup(String target) {
        if(target == null || target.equals("")) {
            return null;
        }
        int index = Arrays.binarySearch(dict.vocabList.toArray(), target);
        if(index < 0) {
            return null;
        }
        else {
            return dict.wordsList.get(index);
        }
    }

    public void editWord(String oldWord) {
        // Lazy ass be like :)
        deleteWord(oldWord);
        addWord();
    }

    public void dictionaryExportToFile()  {
        FileWriter fw;
        try {
            fw = new FileWriter("newDictionary.txt");
            fw.flush();
            for(int i = 0; i < dict.wordsList.size(); i++) {
                fw.write(dict.vocabList.get(i) + "    " + dict.wordsList.get(i).getWordExplain() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Cannot access file");
            e.printStackTrace();
        }
    }
}
