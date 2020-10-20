import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Scanner;

public class FileManager {
    public FileManager() {
    }

    public void deleteManager(String target) throws IOException {
        File oldFile = new File("data.dict");

        File newFile = new File("another.dict");
        if(!newFile.exists()) {
            newFile.createNewFile();
        }

        Scanner sc = new Scanner(oldFile);
        FileWriter fw = new FileWriter("another.dict");
        fw.flush();
        String temp;
        boolean deleted = false;
        
        // Exceptional case
        if(target.equals("a")) {
            temp = sc.nextLine();
            while(!temp.equals("")) {
                temp = sc.nextLine();
            }
            deleted = true;
        }

        while(sc.hasNextLine()) {
            temp = sc.nextLine();
            if(!deleted && temp.length() > target.length() && temp.substring(1, target.length() + 1).equals(target)) {
                temp = sc.nextLine();
                while(sc.hasNextLine() && Character.compare(temp.charAt(0), '@') != 0) {
                    temp = sc.nextLine();
                    if(temp.equals("")) {
                        break;
                    }
                }
            }
            else {
                fw.write(temp + "\n");
            }
        }
        sc.close();
        fw.close();
        oldFile.delete();
        Path source = Paths.get("another.dict");
        Files.move(source, source.resolveSibling("data.dict"));
    }

    public void addManager(String lexicon, String definition, Dictionary dict) throws IOException {
        Word word = new Word(lexicon, definition);

        File oldFile = new File("data.dict");

        File newFile = new File("another.dict");
        if(!newFile.exists()) {
            newFile.createNewFile();
        }

        Scanner sc = new Scanner(oldFile);
        FileWriter fw = new FileWriter("another.dict");
        fw.flush();

        String temp = sc.nextLine();
        int indexToAdd = dict.getIndexToAdd(word);
        System.out.println(indexToAdd);
        boolean added = false;
        int i = 0;

        // Case when add at the beginning
        if(lexicon.compareTo(dict.vocabList.get(0)) < 0) {
            fw.write("@" + lexicon + "\n" + definition + "\n" + "\n");
            added = true;
        }
        fw.write(temp + "\n");

        // Read and write file
        while(sc.hasNextLine()) {
            temp = sc.nextLine();
            if(temp.equals("") && !added && i + 1 == indexToAdd) {
                fw.write("\n" + "@" + lexicon + "\n" + definition + "\n" + "\n");
                added = true;
            }
            else {
                fw.write(temp + "\n");
                if(!added && temp.length() > 0 && Character.compare(temp.charAt(0), '@') == 0) {
                    i++;
                }
            }
        }
        sc.close();
        fw.close();
        oldFile.delete();
        Path source = Paths.get("another.dict");
        Files.move(source, source.resolveSibling("data.dict"));
    }

    public void editDefinitionManager(String lexicon, String newDefinition) throws IOException {
        File oldFile = new File("data.dict");

        File newFile = new File("another.dict");
        if(!newFile.exists()) {
            newFile.createNewFile();
        }

        Scanner sc = new Scanner(oldFile);
        FileWriter fw = new FileWriter("another.dict");
        fw.flush();
        String temp;
        while(sc.hasNextLine()) {
            temp = sc.nextLine();
            if(temp.length() > lexicon.length() && temp.substring(1, lexicon.length() + 1).equals(lexicon)) {
                fw.write(temp + "\n" + newDefinition + "\n" + "\n");
                temp = sc.nextLine();
                while(!temp.equals("") && sc.hasNextLine()) {
                    temp = sc.nextLine();
                }
            }
            else {
                fw.write(temp + "\n");
            }
        }
        sc.close();
        fw.close();
        oldFile.delete();
        Path source = Paths.get("another.dict");
        Files.move(source, source.resolveSibling("data.dict"));
    }

    public void editLexiconManager(String oldLexicon, String newLexicon, String definition, Dictionary dict) throws IOException {
        Word word = new Word(newLexicon, definition);

        File oldFile = new File("data.dict");

        File newFile = new File("another.dict");
        if(!newFile.exists()) {
            newFile.createNewFile();
        }

        Scanner sc = new Scanner(oldFile);
        FileWriter fw = new FileWriter("another.dict");
        fw.flush();

        String temp = sc.nextLine();
        boolean deleted = false;
        boolean added = false;
        int indexToAdd = dict.getIndexToAdd(word);
        int i = 0;

        // Case 0: when add at the beginning
        if(newLexicon.compareTo(dict.vocabList.get(0)) < 0) {
            fw.write("@" + newLexicon + "\n" + definition + "\n" + "\n");
            added = true;
        }
        fw.write(temp + "\n");

        // Read and write file
        while(sc.hasNextLine()) {
            temp = sc.nextLine();
            if(temp.equals("") && !added && indexToAdd == i) {
                fw.write("\n" + "@" + newLexicon + "\n" + definition + "\n" + "\n");
                added = true;
            }
            else if(!deleted && temp.length() > oldLexicon.length() && temp.substring(1, oldLexicon.length() + 1).equals(oldLexicon)) {
                temp = sc.nextLine();
                while(sc.hasNextLine() && Character.compare(temp.charAt(0), '@') != 0) {
                    temp = sc.nextLine();
                    if(temp.equals("")) {
                        break;
                    }
                }
            }
            else {
                fw.write(temp + "\n");
                if(!added && temp.length() > 0 && Character.compare(temp.charAt(0), '@') == 0) {
                    i++;
                }
            }
        }
        sc.close();
        fw.close();
        oldFile.delete();
        Path source = Paths.get("another.dict");
        Files.move(source, source.resolveSibling("data.dict"));
    }
}
