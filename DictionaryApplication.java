import java.awt.*;

import javax.swing.*;

public class DictionaryApplication extends JFrame {
	private static final long serialVersionUID = 1L;
	private JList<String> wordList;
    private JTextField textBox;
    private JTextArea defDisplay;
    // private Highlighter.HighlightPainter grayPainter;

    public void setFontForWords() {
        // Set font for new words
        Font lexicalFont = new Font("Fonts/PlayfairDisplay-VariableFont_wght.ttf", Font.PLAIN, 16);
        textBox = new JTextField();
        textBox.setFont(lexicalFont);

        // Set font for definition
        Font defFont = new Font("Fonts/Judson-Regular", Font.PLAIN, 20);
        defDisplay = new JTextArea();
        defDisplay.setFont(defFont);
    }
    
    private void addWordsTo(DefaultListModel<String> listModel) {
        DictionaryCommandline dictCom = new DictionaryCommandline();
        dictCom.dictManage.insertFromFile();
        for(int i = 0; i < dictCom.dictManage.dict.vocab.length; i++) {
            listModel.addElement(dictCom.dictManage.dict.vocab[i].getWordTarget());
        }
    }

    public DictionaryApplication() {
        super("ViEn Dictionary");
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.LIGHT_GRAY);

        setFontForWords();

        DefaultListModel<String> vocabularyList = new DefaultListModel<>();
        addWordsTo(vocabularyList);
        wordList = new JList<>(vocabularyList);
        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wordList.setVisibleRowCount(26);

        JScrollPane scroll = new JScrollPane(wordList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll);
        //panel.add(wordList);
        getContentPane().add(panel);

        panel.setBounds(10, 80, 150, 480);
        textBox.setBounds(10, 40, panel.getWidth(), 25);
        defDisplay.setBounds(panel.getWidth() + 20, 40, 300, panel.getHeight() + textBox.getY());

        defDisplay.setBackground(Color.LIGHT_GRAY);

        add(textBox);
        add(panel);
        add(defDisplay);

        defDisplay.setEditable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 650));
        setResizable(false);
        pack();
        setVisible(true);
    }

    public void runApplication() {
        new DictionaryApplication();
    }
}
