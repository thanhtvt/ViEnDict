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
    // private Highlighter.HighlightPainter grayPainter; // UI more friendly purpose

    public DictionaryCommandline dictCom = new DictionaryCommandline();

    public void setFontForWords() {
        // Set font for lexicon
        Font lexicalFont = new Font("Fonts/PlayfairDisplay-VariableFont_wght.ttf", Font.PLAIN, 16);
        textBox = new JTextField();
        textBox.setFont(lexicalFont);

        // Set font for definition
        Font defFont = new Font("Fonts/Judson-Regular", Font.PLAIN, 18);
        defDisplay = new JTextArea();
        defDisplay.setFont(defFont);
    }

    private void addWordsTo(DefaultListModel<String> listModel) {
        dictCom.dictManage.insertFromFile();
        for (int i = 0; i < dictCom.dictManage.dict.vocab.length; i++) {
            listModel.addElement(dictCom.dictManage.dict.vocab[i].getWordTarget());
        }
    }

    public void showDefinition() {
        // HOW TO SHOW DEFINITION WHEN SELECT LEXICON ???
        String lexiconSelected = lexiconList.getSelectedValue();
        String definitionValue = dictCom.dictManage.wordPair.get(lexiconSelected);
        defDisplay.setText(definitionValue);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        showDefinition();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    public DictionaryApplication() {
        super("ViEn Dictionary");
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.LIGHT_GRAY);

        setFontForWords();

        textBox.getDocument().addDocumentListener(this);

        DefaultListModel<String> vocabularyList = new DefaultListModel<>();
        addWordsTo(vocabularyList);
        lexiconList = new JList<>(vocabularyList);
        lexiconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lexiconList.setVisibleRowCount(26);
        lexiconList.addListSelectionListener(this);

        JScrollPane lexiconScroll = new JScrollPane(lexiconList);
        lexiconScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(lexiconScroll);

        

        panel.setBounds(10, 80, 150, 480);
        textBox.setBounds(panel.getX(), 40, panel.getWidth(), 25);
        defDisplay.setBounds(panel.getWidth() + 20, 40, 400, panel.getHeight() + textBox.getY());

        defDisplay.setBackground(Color.LIGHT_GRAY);
        defDisplay.setEditable(false);

        add(textBox);
        add(panel);
        add(defDisplay);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 650));
        setResizable(false);
        setVisible(true);
        pack();
    }

    public void runApplication() {
        new DictionaryApplication();
    }
}
