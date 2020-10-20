import java.util.Vector;

public class TernarySearchTree {
    private Node root;
    private Vector<String> traversedArr = new Vector<>();

    public Vector<String> getTraversedArr() {
        return traversedArr;
    }

    private Node insertUtil(Node r, char[] word, int pos) {
        if (r == null) {
            r = new Node(word[pos]);
        }
 
        if (word[pos] < r.getData()) {
            r.left = insertUtil(r.left, word, pos);
        } 
        else if (word[pos] > r.getData()) {
            r.right = insertUtil(r.right, word, pos);
        } 
        else {
            if (pos + 1 < word.length) {
                r.middle = insertUtil(r.middle, word, pos + 1);
            } 
            else r.setEnding(true);
        }
        return r;
    }
    public void insert(String word) {
        root = insertUtil(root, word.toCharArray(), 0);
    }

    private void deleteUtil(Node r, char[] input, int pos) {
        // Base case
        if(r == null) return;
        
        if(input[pos] < r.getData()) {
            deleteUtil(r.left, input, pos);
        }
        else if(input[pos] > r.getData()) {
            deleteUtil(r.right, input, pos);
        }
        else {
            if(pos == input.length - 1 && r.getEnding()) {
                // Remove isEndOfString flag
                r.setEnding(false);
                return;
            }
            else {
                deleteUtil(r.middle, input, pos + 1);
            }
        }
    }

    public void delete(String word) {
        deleteUtil(root, word.toCharArray(), 0);
    }

    private void traverse(Node r, String pattern, char[] word, int depth) {
        if (r != null) 
        { 
            // First traverse the left subtree 
            traverse(r.left, pattern, word, depth); 
    
            // Store the character of this node 
            word[depth] = r.getData(); 
            if (r.getEnding()) 
            { 
                word[depth+1] = '\0'; 
                traversedArr.add(pattern + String.valueOf(word)); 
            } 
    
            // Traverse the subtree using middle pointer 
            traverse(r.middle, pattern, word, depth + 1); 
    
            // Finally Traverse the right subtree 
            traverse(r.right, pattern, word, depth); 
        } 
    }

    private void autoCompleteArr(Node r, String pattern) {
        char[] word = new char[100];
        if(r.getEnding()) {
            traversedArr.add(pattern);
        }
        traverse(r, pattern, word, 0);
    }

    private void nodeIdentify(Node r, String pattern, int pos) {
        char[] input = pattern.toCharArray();
        while(r != null && pos < pattern.length()) {
            int compareChars = Character.compare(r.getData(), input[pos]);
            if(compareChars > 0) {
                r = r.left;
            }
            else if(compareChars < 0) {
                r = r.right;
            }
            else if(compareChars == 0) {
                r = r.middle;
                pos++;
            }
        }
        if(r == null) {
            System.out.println("NOT FOUND!");
            return;
        }
        autoCompleteArr(r, pattern);
    }

    public void autoComplete(String pattern) {
        if(pattern.isEmpty()) {
            return; 
        }
        // Identify starting node in TST
        nodeIdentify(root, pattern, 0);
        // printArray();
    }
    
    // private void printArray() {
    //     Iterator<String> value = traversedArr.iterator();
    //     while (value.hasNext()) { 
    //         System.out.println(value.next()); 
    //     }
    // }
}
