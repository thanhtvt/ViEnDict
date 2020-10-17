import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JPanel defPanel, lexPanel, taskBarPanel;
    private JButton soundButton, addButton, deleteButton, editButton;

    public DictionaryCommandline dictCom = new DictionaryCommandline();

    public DictionaryApplication() {
        super("ViEn Dictionary");
        setLayout(null);

        setFontForWords();

        textBox.getDocument().addDocumentListener(this);

        setLexiconPanel();
        setDefinitionPanel();
        setTaskBarPanel();
        setSoundButton();

        setSizeOfComponent();

        add(textBox);
        add(lexPanel);
        add(defPanel);
        add(taskBarPanel);
        add(soundButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 650));
        setResizable(false);
        pack();
        setVisible(true);
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

    private void setSoundButton() {
        Icon soundIcon = new ImageIcon("Images/sound.png");
        soundButton = new JButton(soundIcon);
        soundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Set text to speech here
                // DEBUG WHY IT DOESN'T HAVE SOUND
                TextToSpeech audio = new TextToSpeech();
                audio.getSpeech(lexiconList.getSelectedValue());
            }
        });
    }

    private void setTaskBarPanel() {
        taskBarPanel = new JPanel();
        taskBarPanel.setLayout(new GridBagLayout());
        //taskBarPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteButton = new JButton("Delete");
        taskBarPanel.add(deleteButton, gbc);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputString = JOptionPane.showInputDialog(deleteButton, "Type in your word");
                if(inputString != null) {

                }
            }
        });

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        addButton = new JButton("Add");
        taskBarPanel.add(addButton, gbc);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAddWordFrame();
            }
        });

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        editButton = new JButton("Edit");
        taskBarPanel.add(editButton, gbc);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        
            }
        });
    }

    private void setSizeOfComponent() {
        lexPanel.setBounds(10, 90, 150, 480);
        textBox.setBounds(lexPanel.getX(), 50, lexPanel.getWidth(), 25);
        defPanel.setBounds(lexPanel.getWidth() + 20, 50, 400, lexPanel.getHeight() + textBox.getY());
        soundButton.setBounds(lexPanel.getWidth() + defPanel.getWidth() - 20, 30, 20, 20);
        taskBarPanel.setBounds(0, 0, 600, 20);
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

    public int binarySearch(JList<String> list, String pattern, int left, int right) {
        if (right >= left) {
            int mid = left + (right - left) / 2;
            String currentWord = list.getModel().getElementAt(mid);
            int cmpValue = pattern.compareTo(currentWord);
            if (cmpValue > 0) {
                return binarySearch(list, pattern, mid + 1, right);
            } else if (cmpValue < 0) {
                return binarySearch(list, pattern, left, mid - 1);
            } else {
                return mid;
            }
        }
        // If it reaches here, then list doesn't contain pattern
        return -1;
    }

    private void createAddWordFrame() {
        JTextArea lexBox = new JTextArea();
        lexBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        JScrollPane lexiconScroll = new JScrollPane(lexBox);
        lexiconScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        lexiconScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JTextArea defBox = new JTextArea();
        defBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        JScrollPane defScroll = new JScrollPane(defBox);
        defScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        defScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel myPanel = new JPanel(new GridLayout(2, 2));
        myPanel.add(new JLabel("Lexicon: "));
        myPanel.add(lexiconScroll);
        myPanel.add(new JLabel("Definition: "));
        myPanel.add(defScroll);
        myPanel.setPreferredSize(new Dimension(300, 200));

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Add word", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION) {
            String newLexicon = lexBox.getText().toLowerCase();
            String newDefinition = defBox.getText();
            if(dictCom.dictManage.wordPair.containsKey(newLexicon)) {
                JOptionPane.showMessageDialog(null, newLexicon + " is already existed", "Invalid", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                // Rewrite code, change vocab array to ArrayList<Word> and ArrayList<String>
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        showDefinition();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // I will optimize this algorithm later (using ternary search tree)
        String pattern = textBox.getText();

        // Get size of list
        int size = lexiconList.getModel().getSize();

        // Apply binary search algorithm
        int pos = binarySearch(lexiconList, pattern, 0, size - 1);

        if (pos != -1) {
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
        // Will use TST if I have time for that task
        insertUpdate(e);
    }
}
