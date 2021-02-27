package mfr.com;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class NotPad extends JFrame {
    static JTextArea textArea;
    JMenuBar menuBar;
    JMenu mnuFile, mnuEdit, mnuFormat, mnuHelp;
    JMenuItem itmNew, itmOpen, itmSave, itmSaveAs, itmExit,
            itmCut, itmCopy, itmPaste, itmFontColor, itmFind,
            itmReplace,itmFontFormat;
    JCheckBoxMenuItem wordWrap;
    UndoAction undoAction;
    RedoAction redoAction;
    UndoManager undoManager;
    String fileName;
    String fileContent;
    JFileChooser fileChooser;
    FontHelper fontHelper;

    public NotPad() {
        intFrame();
        itmSave.addActionListener(e -> {
            save();
        });
        itmSaveAs.addActionListener(e -> {
            saveAs();
        });
        itmOpen.addActionListener(e -> {
            open();
        });
        itmNew.addActionListener(e -> {
            openNew();
        });
        itmExit.addActionListener(e -> {
            System.exit(0);
        });
        itmCut.addActionListener(e -> {
            textArea.cut();
        });
        itmCopy.addActionListener(e -> {
            textArea.copy();
        });
        itmPaste.addActionListener(e -> {
            textArea.paste();
        });
        //undo redo action
        textArea.getDocument().addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        });
        wordWrap.addActionListener(e -> {
            if (wordWrap.isSelected()) {
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
            } else {
                textArea.setLineWrap(false);
                textArea.setWrapStyleWord(false);
            }
        });
        itmFontColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose font color", Color.black);
            textArea.setForeground(color);
        });
        itmFind.addActionListener(e -> {
                new FindAndReplace(this,false);
        });
        itmReplace.addActionListener(e -> {
            new FindAndReplace(this,true);
        });
        itmFontFormat.addActionListener(e -> {
            fontHelper.setVisible(true);

        });
       fontHelper.getOk().addActionListener(e -> {
           textArea.setFont(fontHelper.font());
           fontHelper.setVisible(false);

        });
        fontHelper.getCancle().addActionListener(e -> {
            fontHelper.setVisible(false);

        });

    }

    private void intFrame() {
        fontHelper = new FontHelper();
        undoManager = new UndoManager();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        fileChooser = new JFileChooser();

        FileNameExtensionFilter filter;
        filter = new FileNameExtensionFilter(
                "*.txt", new String[]{"txt"});
        fileChooser.addChoosableFileFilter(filter);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("untitled-Notepad");
        this.setSize(600, 700);
        textArea = new JTextArea();
        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        //menu bar
        menuBar = new JMenuBar();
        //menu
        mnuFile = new JMenu("File");
        mnuEdit = new JMenu("Edit");
        mnuFormat = new JMenu("Format");
        mnuHelp = new JMenu("Help");
        //menu item
        itmNew = new JMenuItem("New");
        itmOpen = new JMenuItem("Open");
        itmSave = new JMenuItem("Save");
        itmSaveAs = new JMenuItem("Save As");
        itmExit = new JMenuItem("Exit");
        itmCut = new JMenuItem("Cut");
        itmCopy = new JMenuItem("Copy");
        itmPaste = new JMenuItem("Paste");
        wordWrap = new JCheckBoxMenuItem("Word Wrap");
        itmFontColor = new JMenuItem("Font Color");
        itmFind = new JMenuItem("Find");
        itmReplace = new JMenuItem("Replace");
        itmFontFormat = new JMenuItem("Font Format");
        //adding shortcut
        itmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        // itmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        // itmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        itmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        itmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        itmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        //add menu item to file
        mnuFile.add(itmNew);
        mnuFile.add(itmOpen);
        mnuFile.add(itmSave);
        mnuFile.add(itmSaveAs);
        mnuFile.addSeparator();
        mnuFile.add(itmExit);
        //add menu item to edit
        mnuEdit.add(undoAction);
        mnuEdit.add(redoAction);
        mnuEdit.add(itmCut);
        mnuEdit.add(itmCopy);
        mnuEdit.add(itmPaste);
        mnuEdit.addSeparator();
        mnuEdit.add(itmFind);
        mnuEdit.add(itmReplace);
        //add menu item to format
        mnuFormat.add(wordWrap);
        mnuFormat.add(itmFontColor);
        mnuFormat.add(itmFontFormat);
        //add menu item to menu bar
        menuBar.add(mnuFile);
        menuBar.add(mnuEdit);
        menuBar.add(mnuFormat);
        menuBar.add(mnuHelp);
        //add menu bar to frame
        this.setJMenuBar(menuBar);


        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void save() {
        PrintWriter printWriter = null;
        //   int chooserOption = -1;
        try {
            if (fileName == null) {
/*                chooserOption = fileChooser.showSaveDialog(this);
                if(chooserOption == JFileChooser.APPROVE_OPTION){
                    printWriter = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
                    StringTokenizer stringTokenizer = new StringTokenizer(textArea.getText(),
                            System.getProperty("line.separator"));
                    while (stringTokenizer.hasMoreElements()) {
                        printWriter.println(stringTokenizer.nextToken());
                    }
                    fileName = String.valueOf(fileChooser.getSelectedFile());
                    this.setTitle(fileChooser.getSelectedFile().getName());
                    JOptionPane.showMessageDialog(this, "File Saved");
                }*/
                saveAs();
            } else {
                printWriter = new PrintWriter(new FileWriter(fileName));
                StringTokenizer stringTokenizer = new StringTokenizer(textArea.getText(),
                        System.getProperty("line.separator"));
                while (stringTokenizer.hasMoreElements()) {
                    printWriter.println(stringTokenizer.nextToken());
                }
                fileContent = textArea.getText();
                JOptionPane.showMessageDialog(this, "File Saved");
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (printWriter != null)
                printWriter.close();
        }
    }

    private void saveAs() {
        PrintWriter printWriter = null;
        int chooserOption = -1;
        try {
            chooserOption = fileChooser.showSaveDialog(this);
            if (chooserOption == JFileChooser.APPROVE_OPTION) {
                if(fileChooser.getSelectedFile().exists()){
                    int option = JOptionPane.showConfirmDialog(this,"Do you want to replace this file?",
                            "Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(option == 0){
                        printWriter = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
                        StringTokenizer stringTokenizer = new StringTokenizer(textArea.getText(),
                                System.getProperty("line.separator"));
                        while (stringTokenizer.hasMoreElements()) {
                            printWriter.println(stringTokenizer.nextToken());
                        }
                        fileName = String.valueOf(fileChooser.getSelectedFile());
                        this.setTitle(fileChooser.getSelectedFile().getName());
                        fileContent = textArea.getText();
                        JOptionPane.showMessageDialog(this, "File Saved by replace");
                    }else {
                        saveAs();
                    }
                }else{
                    printWriter = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
                    StringTokenizer stringTokenizer = new StringTokenizer(textArea.getText(),
                            System.getProperty("line.separator"));
                    while (stringTokenizer.hasMoreElements()) {
                        printWriter.println(stringTokenizer.nextToken());
                    }
                    fileName = String.valueOf(fileChooser.getSelectedFile());
                    this.setTitle(fileChooser.getSelectedFile().getName());
                    fileContent = textArea.getText();
                    JOptionPane.showMessageDialog(this, "File Saved");
                }

            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (printWriter != null)
                printWriter.close();
        }
    }

    private void open() {
        BufferedReader bufferedReader = null;
        int chooserOption = -1;
        try {
            chooserOption = fileChooser.showOpenDialog(this);
            if (chooserOption == JFileChooser.APPROVE_OPTION) {
                bufferedReader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                Scanner sc = new Scanner(bufferedReader);
                while (sc.hasNextLine()) {
                    textArea.setText(sc.nextLine());
                }
                fileName = String.valueOf(fileChooser.getSelectedFile());
                this.setTitle(fileChooser.getSelectedFile().getName());
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void openNew() {
        if (!textArea.getText().isEmpty() && !textArea.getText().equals(fileContent)) {

            if (fileName != null) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Do you want to save changes?",
                        "Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == 0) {
                    save();
                } else if (option == 1) {
                    clear();
                }
            } else {
                int option = JOptionPane.showConfirmDialog(this,
                        "Do you want to save changes?",
                        "Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == 0) {
                    saveAs();
                } else if (option == 1) {
                    clear();
                }
            }
        } else {
            clear();
        }
    }

    private void clear() {
        textArea.setText(null);
        fileName = null;
        fileContent = null;
        this.setTitle("untitled-Notepad");
    }

    //undo redo working now
    class UndoAction extends AbstractAction {
        UndoAction() {
            super("undo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException undoException) {
                undoException.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undoManager.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "undo");
            } else {
                setEnabled(false);
            }
        }
    }

    class RedoAction extends AbstractAction {
        RedoAction() {
            super("redo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException redoException) {
                redoException.printStackTrace();
            }
            update();
            undoAction.update();

        }

        protected void update() {
            if (undoManager.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "redo");
            } else {
                setEnabled(false);
            }
        }
    }
    public static JTextArea getTextArea(){
        return textArea;
    }


}
