
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JPanel pane; // Main panel
    private JTextField filenameField; // Filename field as well as search by word field
    private JButton saveButton; // SAVE button
    private JButton loadButton; // LOAD button
    private JButton searchButton, previousButton, nextButton; // SEARCH, PREVIOUS, NEXT buttons
    private JCheckBox checkBox; // Use RegExp checkbox
    private JTextArea textArea; // Main text area
    private JScrollPane scrollPane; // Scrolling function to text area
    private JFileChooser fileChooser; // file manager
    private ArrayList<ArrayList<Integer>> listOfIndices; // indices of all word occurrences from start to end index
    private int currentCursor; // current word in list, i.e. first, second etc.
    private MatchResult matchResult; // keeps information of found word


    public TextEditor() {
        setTitle("Text Editor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        addPane();
        addMenu();
        addSaveButton();
        addLoadButton();
        addTextField();
        addSearchButton();
        addPreviousButton();
        addNextButton();
        addCheckbox();
        addFileChooser();
        addIcon(new ImageIcon("icons\\save.png"), saveButton);
        addIcon(new ImageIcon("icons\\load.png"), loadButton);
        addIcon(new ImageIcon("icons\\search.png"), searchButton);
        addIcon(new ImageIcon("icons\\previous.png"), previousButton);
        addIcon(new ImageIcon("icons\\next.png"), nextButton);
        addTextArea();

        setVisible(true);
    }

    // adding main panel
    private void addPane() {
        pane = new JPanel(new FlowLayout());
        add(pane, BorderLayout.PAGE_START);
        //setting borders
        add(new JPanel(), BorderLayout.PAGE_END);
        add(new JPanel(), BorderLayout.LINE_START);
        add(new JPanel(), BorderLayout.LINE_END);
    }

    // adding Filename field
    private void addTextField() {
        pane.add(filenameField = new JTextField(15));
        filenameField.setName("SearchField");
    }

    // adding menu
    private void addMenu() {
        JMenuItem menuSave = new JMenuItem("Save");
        menuSave.setName("MenuSave");
        menuSave.addActionListener(e -> saveFile());

        JMenuItem menuOpen = new JMenuItem("Open");
        menuOpen.addActionListener(e -> loadFile());
        menuOpen.setName("MenuOpen");

        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.setName("MenuExit");
        menuExit.addActionListener(e -> dispose());

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setName("MenuFile");

        fileMenu.add(menuSave);
        fileMenu.add(menuOpen);
        fileMenu.addSeparator();
        fileMenu.add(menuExit);

        JMenuItem startSearch = new JMenuItem("Start search");
        startSearch.setName("MenuStartSearch");
        startSearch.addActionListener(e -> searchWord());

        JMenuItem previousMatch = new JMenuItem("Previous match");
        previousMatch.setName("MenuPreviousMatch");
        previousMatch.addActionListener(e -> previousWord());

        JMenuItem nextMatch = new JMenuItem("Next match");
        nextMatch.setName("MenuNextMatch");
        nextMatch.addActionListener(e -> nextWord());

        JMenuItem useRegExp = new JMenuItem("Use regular expressions");
        useRegExp.setName("MenuUseRegExp");
        useRegExp.addActionListener(e -> checkBox.doClick());

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setMnemonic(KeyEvent.VK_S);
        searchMenu.setName("MenuSearch");

        searchMenu.add(startSearch);
        searchMenu.add(previousMatch);
        searchMenu.add(nextMatch);
        searchMenu.add(useRegExp);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);

    }

    // adding SAVE button
    private void addSaveButton() {
        saveButton = new JButton();
        saveButton.setName("SaveButton");
        saveButton.addActionListener(e -> saveFile());
        pane.add(saveButton);

    }

    // adding LOAD button
    private void addLoadButton() {
        loadButton = new JButton();
        loadButton.setName("OpenButton");
        loadButton.addActionListener(e -> loadFile());
        pane.add(loadButton);
    }

    // adding SEARCH button
    private void addSearchButton() {
        searchButton = new JButton();
        searchButton.setName("StartSearchButton");
        searchButton.addActionListener(e -> searchWord());
        pane.add(searchButton);
    }

    // adding PREVIOUS button
    private void addPreviousButton() {
        previousButton = new JButton();
        previousButton.setName("PreviousMatchButton");
        previousButton.addActionListener(e -> previousWord());
        pane.add(previousButton);
    }

    // adding NEXT button
    private void addNextButton() {
        nextButton = new JButton();
        nextButton.setName("NextMatchButton");
        nextButton.addActionListener(e -> nextWord());
        pane.add(nextButton);
    }

    // adding RegExp checkbox
    private void addCheckbox() {
        checkBox = new JCheckBox("Use regex");
        checkBox.setName("UseRegExCheckbox");
        pane.add(checkBox);
    }

    // adding File manager
    private void addFileChooser() {
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        fileChooser.setName("FileChooser");
        add(fileChooser);
    }

    // changes the size of image and adds it to specific button
    private void addIcon(ImageIcon icon, JButton button) {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(20,20, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        button.setIcon(icon);
    }

    //adding Text Area
    private void addTextArea() {
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

    }

    // loads text from file
    private void loadFile() {
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file)) {
                byte [] array = fis.readAllBytes();
                if (array.length == 0) {
                    textArea.setText("");
                }
                filenameField.setText(file.getName());
                String readBytes = new String(array, StandardCharsets.UTF_8);
                textArea.setText(readBytes);

            } catch (IOException fileNotFoundException) {
                textArea.resetKeyboardActions();
                textArea.setText("");
                fileNotFoundException.printStackTrace();
            }
        }

    }

    // saves text from TextArea to file
    private void saveFile() {
        fileChooser.showSaveDialog(null);
        String fileName = filenameField.getText();
        File file = new File(fileName);
        try (FileOutputStream writer = new FileOutputStream(file, false)){
            writer.write(textArea.getText().getBytes());

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // searches word taken from Filename field
    private void searchWord() {
        listOfIndices = new ArrayList<>();
        String word = filenameField.getText();
        String text = textArea.getText();

        Pattern pattern = Pattern.compile(word);
        Matcher matcher = pattern.matcher(text);

        if (!checkBox.isSelected()) {
            String temp = ".[]{}()<>*+-=!?^$|";
            char[] specialCharacters = temp.toCharArray();
            word = word.replace("\\", "\\\\");
            for (char c : specialCharacters) {
                if (word.contains(String.valueOf(c))) {
                    word = word.replace(String.valueOf(c), "\\" + c);
                }
            }

            pattern = Pattern.compile(word);
            matcher = pattern.matcher(text);
        }
        if (matcher.find()) {
            matchResult = matcher.toMatchResult();
            textArea.setCaretPosition(matchResult.start() + (matchResult.end() - matchResult.start()));
            textArea.select(matchResult.start(), matchResult.end());
            textArea.grabFocus();

            listOfIndices.add(new ArrayList<>());
            listOfIndices.get(0).add(matchResult.start());
            listOfIndices.get(0).add(matchResult.end());
            currentCursor = 0;
        }

        while (matcher.find()) {
            currentCursor++;
            matchResult = matcher.toMatchResult();
            listOfIndices.add(new ArrayList<>());
            listOfIndices.get(currentCursor).add(matchResult.start());
            listOfIndices.get(currentCursor).add(matchResult.end());
        }
        currentCursor = 0;
    }

    // selects next word occurrence
    private void nextWord() {
        if (currentCursor == listOfIndices.size() - 1) {
            currentCursor = 0;
            int startIndex = listOfIndices.get(currentCursor).get(0);
            int endIndex = listOfIndices.get(currentCursor).get(1);
            textArea.setCaretPosition(startIndex + (endIndex - startIndex));
            textArea.select(startIndex, endIndex);
        } else {
            currentCursor++;
            int startIndex = listOfIndices.get(currentCursor).get(0);
            int endIndex = listOfIndices.get(currentCursor).get(1);
            textArea.setCaretPosition(startIndex + (endIndex - startIndex));
            textArea.select(startIndex, endIndex);
        }
        textArea.grabFocus();
    }

    // selects previous word occurrence
    private void previousWord() {
        if (currentCursor == 0) {
            currentCursor = listOfIndices.size() - 1;
            textArea.setCaretPosition(matchResult.start() + (matchResult.end() - matchResult.start()));
            textArea.select(matchResult.start(), matchResult.end());
        } else {
            currentCursor--;
            int startIndex = listOfIndices.get(currentCursor).get(0);
            int endIndex = listOfIndices.get(currentCursor).get(1);
            textArea.setCaretPosition(startIndex + (endIndex - startIndex));
            textArea.select(startIndex, endIndex);
        }
        textArea.grabFocus();

    }
}