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
            int i = 0;
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
                dict.vocab[i].setWordTarget(lexicon);
                dict.vocab[i].setWordExplain(definition);
                wordPair.put(lexicon, definition);
                tree.insert(lexicon);
                i++;
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
