import java.util.Scanner;
public class DictionaryManagement {
    public Dictionary dict = new Dictionary();

    public void insertFromCommandLine() {
        Scanner sc = new Scanner(System.in);
        int numberOfWords = sc.nextInt();
        sc.nextLine();
        dict.setLength(numberOfWords);
        for(int i = 0; i < numberOfWords; i++) {
            dict.vocab[i].setWordTarget(sc.nextLine());
            dict.vocab[i].setWordExplain(sc.nextLine());
        }
        sc.close();
    }
}
