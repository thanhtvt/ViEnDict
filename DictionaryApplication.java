import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DictionaryApplication extends JFrame implements ListSelectionListener, DocumentListener {
    private static final long serialVersionUID = 1L;
    private JList<String> lexiconList;
    private JTextField textBox;
    private JTextArea defDisplay;
    private JPanel defPanel, lexPanel;
    // private Highlighter.HighlightPainter grayPainter; // UI more friendly purpose

    public DictionaryCommandline dictCom = new DictionaryCommandline();

    public DictionaryApplication() {
        super("ViEn Dictionary");
        setLayout(null);

        setFontForWords();

        textBox.getDocument().addDocumentListener(this);

        setLexiconPanel();
        setDefinitionPanel();

        setSizeOfComponent();

        add(textBox);
        add(lexPanel);
        add(defPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 650));
        setResizable(false);
        setVisible(true);
        pack();
    }

    public void runApplication() {
        new DictionaryApplication();
    }

    /**
     * Create panel with scroll bar to show vocabulary.
     */
    private void setLexiconPanel() {
        lexPanel = new JPanel();
        lexPanel.setLayout(new BorderLayout());
        lexPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Create list holding data from file
        DefaultListModel<String> vocabularyList = new DefaultListModel<>();
        addWordsTo(vocabularyList);

        lexiconList = new JList<>(vocabularyList);
        lexiconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lexiconList.setVisibleRowCount(26);
        lexiconList.addListSelectionListener(this);

        JScrollPane lexiconScroll = new JScrollPane(lexiconList);
        lexiconScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        lexiconScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        lexPanel.add(lexiconScroll, BorderLayout.CENTER);
    }

    /**
     * Create panel with scroll bar to show definition. 
     */
    private void setDefinitionPanel() {
        defPanel = new JPanel();
        defPanel.setLayout(new BorderLayout());
        defPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        defDisplay.setBackground(Color.LIGHT_GRAY);
        defDisplay.setEditable(false);

        // Set scroll bar for panel
        JScrollPane defScroll = new JScrollPane(defDisplay);
        defScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        defScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        defPanel.add(defScroll, BorderLayout.CENTER);
    }

    private void setSizeOfComponent() {
        lexPanel.setBounds(10, 80, 150, 480);
        textBox.setBounds(lexPanel.getX(), 40, lexPanel.getWidth(), 25);
        defPanel.setBounds(lexPanel.getWidth() + 20, 40, 400, lexPanel.getHeight() + textBox.getY());
    }

    private void setFontForWords() {
        // Set font for lexicon
        Font lexiconFont = new Font("Fonts/PlayfairDisplay-VariableFont_wght.ttf", Font.PLAIN, 16);
        textBox = new JTextField();
        textBox.setFont(lexiconFont);

        // Set font for definition
        Font defFont = new Font("Fonts/Judson-Regular", Font.PLAIN, 17);
        defDisplay = new JTextArea();
        defDisplay.setFont(defFont);
    }

    private void addWordsTo(DefaultListModel<String> listModel) {
        dictCom.dictManage.insertFromFile();
        for (int i = 0; i < dictCom.dictManage.dict.vocab.length; i++) {
            listModel.addElement(dictCom.dictManage.dict.vocab[i].getWordTarget());
        }
    }

    private void showDefinition() {
        // HOW TO SHOW DEFINITION WHEN SELECTING LEXICON ???
        String lexiconSelected = lexiconList.getSelectedValue();
        String definitionValue = dictCom.dictManage.wordPair.get(lexiconSelected);
        defDisplay.setText(definitionValue);
    }

    private int binarySearch(JList<String> list, String pattern, int left, int right) {
        if(right >= left) {
            int mid = left + (right - left) / 2;
            String currentWord = list.getModel().getElementAt(mid);
            int cmpValue = pattern.compareTo(currentWord);
            if(cmpValue > 0) {
                return binarySearch(list, pattern, mid + 1, right);
            }
            else if(cmpValue < 0) {
                return binarySearch(list, pattern, left, mid - 1);
            }
            else {
                return mid;
            }
        }
        // If it reaches here, then list doesn't contain pattern
        return -1;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        showDefinition();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        String pattern = textBox.getText();

        // Get size of list
        int size = lexiconList.getModel().getSize();

        // Apply binary search algorithm
        int pos = binarySearch(lexiconList, pattern, 0, size - 1);

        if(pos != -1) {
            lexiconList.setSelectedIndex(pos);
            lexiconList.ensureIndexIsVisible(pos);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Sadly, my data isn't 100% sorted in alphabet order
        // so binarySearch is not that efficient :'(
        insertUpdate(e);
    }
}
