import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DictionaryApplication extends JFrame implements DocumentListener {
    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> vocabularyList;
    private JList<String> lexiconList;
    private JTextField textBox;
    private JTextArea defDisplay;
    private JPanel defPanel, lexPanel, taskBarPanel;
    private JButton soundButton, addButton, deleteButton, editButton, restartButton, googleTranslateButton;

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
        vocabularyList = new DefaultListModel<>();
        addWordsTo(vocabularyList);

        lexiconList = new JList<>(vocabularyList);
        lexiconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lexiconList.setVisibleRowCount(26);
        lexiconList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                showDefinition();
            }
        });

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
                TextToSpeech audio = new TextToSpeech();
                
                // Setting the voice
				audio.setVoice("cmu-slt-hsmm");

				// TTS say something
				audio.speak(lexiconList.getSelectedValue(), 2.0f, false, false);
            }
        });
    }

    private void setTaskBarPanel() {
        taskBarPanel = new JPanel();
        taskBarPanel.setLayout(new GridBagLayout());
        // taskBarPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();

        // Delete function
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteButton = new JButton("Delete");
        taskBarPanel.add(deleteButton, gbc);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputString = JOptionPane.showInputDialog(deleteButton, "Type in your word");
                if (inputString != null && !inputString.equals("")) {
                    if (!dictCom.dictManage.wordPair.containsKey(inputString)) {
                        JOptionPane.showMessageDialog(null, inputString + " not found");
                    } else {
                        dictCom.dictManage.deleteWord(inputString);
                        dictCom.dictManage.wordPair.remove(inputString);
                        dictCom.dictManage.tree.delete(inputString);
                        FileManager fileManager = new FileManager();
                        try {
                            fileManager.deleteManager(inputString);
                            JOptionPane.showMessageDialog(null, "Please restart to save your changes");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        // Add function
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

        // Edit function
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        editButton = new JButton("Edit");
        taskBarPanel.add(editButton, gbc);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createEditWordFrame();
            }
        });

        // call Google Translate function
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 3;
        gbc.gridy = 0;
        googleTranslateButton = new JButton("Google Translate");
        taskBarPanel.add(googleTranslateButton, gbc);
        googleTranslateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createGoogleTranslateFrame();
            }
        });

        // Restart function
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 4;
        gbc.gridy = 0;
        restartButton = new JButton("Restart");
        taskBarPanel.add(restartButton, gbc);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new DictionaryApplication();
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
        for (int i = 0; i < dictCom.dictManage.dict.wordsList.size(); i++) {
            listModel.addElement(dictCom.dictManage.dict.vocabList.get(i));
        }
    }

    private void showDefinition() {
        String lexiconSelected = lexiconList.getSelectedValue();
        String definitionValue = dictCom.dictManage.wordPair.get(lexiconSelected);
        defDisplay.setText(definitionValue);
    }

    private int binarySearch(JList<String> list, String pattern, int left, int right) {
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

    private void createGoogleTranslateFrame() {
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

        JLabel langFrom = new JLabel();
        JLabel langTo = new JLabel();

        DefaultComboBoxModel<String> list = new DefaultComboBoxModel<>();
        list.addElement("Choose one option");
        list.addElement("English -> Vietnamese");
        list.addElement("Vietnamese -> English");
        JComboBox<String> choiceBox = new JComboBox<>(list);
        choiceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = choiceBox.getSelectedItem().toString();
                if (option.equals("English -> Vietnamese")) {
                    langFrom.setText("English:");
                    langTo.setText("Vietnamese:");
                } else if (option.equals("Vietnamese -> English")) {
                    langFrom.setText("Vietnamese:");
                    langTo.setText("English:");
                }
            }
        });

        JButton translateButton = new JButton("Translate");
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Translator trans = new Translator();
                try {
                    String definition;
                    String option = choiceBox.getSelectedItem().toString();
                    if(option.equals("English -> Vietnamese")) {
                        definition = trans.callUrlAndParseResult("en", "vi", lexBox.getText());
                        defBox.setText(definition);
                    }
                    else if(option.equals("Vietnamese - > English")) {
                        definition = trans.callUrlAndParseResult("vi", "en", lexBox.getText());
                        defBox.setText(definition);
                    }
                } catch (Exception e1) {
                    System.out.println("Cannot access Google Translate API");
                    e1.printStackTrace();
                }
            }
        });

        JPanel myPanel = new JPanel(new GridLayout(3, 2));
        myPanel.add(langFrom);
        myPanel.add(lexiconScroll);
        myPanel.add(langTo);
        myPanel.add(defScroll);
        myPanel.add(translateButton);
        myPanel.add(choiceBox);
        myPanel.setPreferredSize(new Dimension(400, 275));
        JOptionPane.showConfirmDialog(null, myPanel, "Google Translator", JOptionPane.OK_CANCEL_OPTION);
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
            if(newLexicon == null || newDefinition == null || newLexicon.equals("") || newDefinition.equals("")) {
                JOptionPane.showMessageDialog(null, "Please finish your word", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(dictCom.dictManage.wordPair.containsKey(newLexicon)) {
                JOptionPane.showMessageDialog(null, newLexicon + " is already existed", "Invalid", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                Word word = new Word(newLexicon, newDefinition);
                dictCom.dictManage.dict.addWord(word);
                dictCom.dictManage.wordPair.put(newLexicon, newDefinition);
                dictCom.dictManage.tree.insert(newLexicon);
                FileManager fileManager = new FileManager();
                try {
                    fileManager.addManager(newLexicon, newDefinition, dictCom.dictManage.dict);
                    JOptionPane.showMessageDialog(null, "Please restart to save your changes");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createEditWordFrame() {
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

        DefaultComboBoxModel<String> list = new DefaultComboBoxModel<>();
        list.addElement("Choose one option");
        list.addElement("Edit lexicon only");
        list.addElement("Edit definition only");
        JComboBox<String> choiceBox = new JComboBox<>(list);
        choiceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = choiceBox.getSelectedItem().toString();
                if(option.equals("Edit lexicon only")) {
                    lexBox.setEditable(true);
                    defBox.setEditable(false);
                }
                else if (option.equals("Edit definition only")) {
                    defBox.setEditable(true);
                    lexBox.setEditable(false);
                }
            }
        });
        // Store lexicon value that need to change
        JLabel saveItem = new JLabel();

        JButton saveButton = new JButton("save lexicon");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defBox.setText(dictCom.dictManage.wordPair.get(lexBox.getText()));
                saveItem.setText(lexBox.getText());
            }
        });

        JPanel myPanel = new JPanel(new GridLayout(3, 2));
        myPanel.add(new JLabel("Lexicon: "));
        myPanel.add(lexiconScroll);
        myPanel.add(new JLabel("Definition: "));
        myPanel.add(defScroll);
        myPanel.add(saveButton);
        myPanel.add(choiceBox);
        myPanel.setPreferredSize(new Dimension(400, 275));

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Edit word", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION) {
            String optionChosen = choiceBox.getSelectedItem().toString();
            FileManager fileManager = new FileManager();
            if(optionChosen.equals("Edit lexicon only")) {
                String oldLexicon = saveItem.getText();
                String newLexicon = lexBox.getText();
                String oldDefinition = defBox.getText();
                try {
                    fileManager.editLexiconManager(oldLexicon, newLexicon, oldDefinition, dictCom.dictManage.dict);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else if (optionChosen.equals("Edit definition only")) {
                String oldLexicon = lexBox.getText();
                String newDefinition = defBox.getText();
                try {
                    fileManager.editDefinitionManager(oldLexicon, newDefinition);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, "Please restart to save your changes");  
        }
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
        insertUpdate(e);
    }
}
