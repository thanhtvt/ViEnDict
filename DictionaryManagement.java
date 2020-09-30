import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class DictionaryManagement {
    public Dictionary dict = new Dictionary();
    private Map<String, String> wordPair = new HashMap<String, String>();
    public TernarySearchTree tree = new TernarySearchTree();
    public Scanner sc = new Scanner(System.in);

    public void insertFromCommandLine() {
        int numberOfWords = sc.nextInt();
        sc.nextLine();
        dict.setLength(numberOfWords);
        for (int i = 0; i < dict.getLength(); i++) {
            dict.vocab[i].setWordTarget(sc.nextLine());
            dict.vocab[i].setWordExplain(sc.nextLine());
            wordPair.put(dict.vocab[i].getWordTarget(), dict.vocab[i].getWordExplain());
            tree.insert(dict.vocab[i].getWordTarget());
        }
    }

    /**
     * A function for insertFromFile()
     * 
     * @return number of words in file
     * @throws FileNotFoundException
     */
    private int findLengthInFile() throws FileNotFoundException {
        File file = new File("dictionaries.txt");
        Scanner sc = new Scanner(file);
        int length = 0;
        while (sc.hasNextLine()) {
            sc.nextLine();
            length++;
        }
        sc.close();
        return length;
    }

    public void insertFromFile() throws FileNotFoundException {
        File file = new File("dictionaries.txt");
        Scanner sc = new Scanner(file);

        int length = findLengthInFile();
        dict.setLength(length);
        int i = 0;

        // Read from file
        while (sc.hasNextLine()) {
            dict.vocab[i] = new Word();
            String newWord = sc.next();
            dict.vocab[i].setWordTarget(newWord);
            String newMeaning1 = sc.next();
            String newMeaning2;
            if (sc.hasNextLine()) {
                newMeaning2 = sc.nextLine();
            } else {
                newMeaning2 = "";
            }
            dict.vocab[i].setWordExplain(newMeaning1 + newMeaning2);
            wordPair.put(newWord, newMeaning1 + newMeaning2);
            tree.insert(newWord);
            i++;
        }
        sc.close();
    }

    public void dictionaryLookup() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Your word is: ");
        while (sc.hasNext()) {
            String target = sc.next();
            if (wordPair.get(target) != null) {
                System.out.println("It means: " + wordPair.get(target));
            } else {
                System.out.println("Not found! Please wait for my upcoming update.");
            }
            System.out.print("Your word is: ");
        }
        sc.close();
    }

    private void insertIntoDictionary() {
        int i = 0;
        for (Map.Entry<String, String> mapElement : wordPair.entrySet()) {
            String key = mapElement.getKey();
            dict.vocab[i].setWordTarget(key);
            String value = mapElement.getValue();
            dict.vocab[i].setWordExplain(value);
            i++;
        }
    }

    public void addWord() {
        // Increase number of words by 1.
        dict.setLength(dict.getLength() + 1);

        System.out.print("The word you add is: ");

        String newWord = sc.nextLine();
        System.out.print("It means: ");
        String newMeaning = sc.nextLine();
        wordPair.put(newWord, newMeaning);
        tree.insert(newWord);

        // Recall object
        insertIntoDictionary();
    }

    public void deleteWord(String wordTarget) {
        // REWRITE quicksort and binarySearch algorithm :((
        int index = -1;
        for (int i = 0; i < dict.getLength(); i++) {
            if (dict.vocab[i].getWordTarget().equals(wordTarget)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            System.out.println("NOT FOUND!");
            return;
        } else {
            wordPair.remove(dict.vocab[index].getWordTarget());
            tree.delete(dict.vocab[index].getWordTarget());
            if (index == dict.getLength() - 1) {
                dict.vocab[index].setWordTarget("");
                dict.vocab[index].setWordExplain("");
            } else {
                for (int i = index; i < dict.getLength() - 1; i++) {
                    dict.vocab[i].setWordTarget(dict.vocab[i + 1].getWordTarget());
                    dict.vocab[i].setWordExplain(dict.vocab[i + 1].getWordExplain());
                }
            }
        }
        dict.setLength(dict.getLength() - 1);
        insertIntoDictionary();
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
            for(int i = 0; i < dict.getLength(); i++) {
                fw.write(dict.vocab[i].getWordTarget() + "    " + dict.vocab[i].getWordExplain() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Cannot access file");
            e.printStackTrace();
        }
    }
}
