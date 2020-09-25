import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class DictionaryManagement {
    public Dictionary dict = new Dictionary();

    public void insertFromCommandLine() {
        Scanner sc = new Scanner(System.in);
        int numberOfWords = sc.nextInt();
        sc.nextLine();
        dict.setLength(numberOfWords);
        for (int i = 0; i < numberOfWords; i++) {
            dict.vocab[i].setWordTarget(sc.nextLine());
            dict.vocab[i].setWordExplain(sc.nextLine());
        }
        sc.close();
    }

    public int findLengthInFile() throws FileNotFoundException {
        File file = new File("dictionaries.txt");
        Scanner sc = new Scanner(file);
        int length = 0;
        while(sc.hasNextLine()) {
            sc.nextLine();
            length++;
        }
        sc.close();
        return length;
    }

    public void insertFromFile() throws FileNotFoundException {
        File file = new File("dictionaries.txt");
        Scanner sc = new Scanner(file);

        // Create an object for vocab
        int length = findLengthInFile();
        dict.setLength(length);
        int i = 0;

        // Read from file
        while(sc.hasNextLine()) {
            dict.vocab[i] = new Word();
            String newWord = sc.next();
            dict.vocab[i].setWordTarget(newWord);
            String newMeaning1 = sc.next();
            String newMeaning2;
            if(sc.hasNextLine()) {
                newMeaning2 = sc.nextLine();
            }
            else {
                newMeaning2 = "";
            }
            dict.vocab[i].setWordExplain(newMeaning1 + newMeaning2);
            i++;
        }
        sc.close();
    }
}
