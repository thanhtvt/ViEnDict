import java.util.Scanner;
// MESS HERE TOO...MAYBE??
public class DictionaryManagement {
    public Dictionary dict;

    public void insertFromCommandLine() {
        Scanner sc = new Scanner(System.in);
        int numberOfWords = sc.nextInt();
        dict = new Dictionary(numberOfWords);
        for(int i = 0; i < numberOfWords; i++) {
            dict.vocab[i].setWordTarget(sc.nextLine());
            dict.vocab[i].setWordExplain(sc.nextLine());
        }
        sc.close();
    }
}
