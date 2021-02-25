package mfr.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FindAndReplace extends JDialog implements ActionListener {
    boolean foundOne,isReplace;
    JTextField searchText,replaceText;
    JCheckBox checkBoxCase,checkBoxWhole;
    JRadioButton radioButtonUp,radioButtonDown;
    JLabel labelStatusInfo;
    JFrame owner;
    JPanel north, center, south;
    public FindAndReplace(JFrame owner,boolean isReplace) {
        super(owner,true);
        north = new JPanel();
        center = new JPanel();
        south = new JPanel();
        this.isReplace = isReplace;
        if(isReplace){
            this.setTitle("Find and Replace");
            this.setReplacePanel(north);
        }else {
            this.setTitle("Find");
            this.setFindPanel(north);
        }
        this.addComponent(center);
        labelStatusInfo = new JLabel("Status Info");
        south.add(labelStatusInfo);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.getContentPane().add(north,BorderLayout.NORTH);
        this.getContentPane().add(center,BorderLayout.CENTER);
        this.getContentPane().add(south,BorderLayout.SOUTH);
        pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        int x = (owner.getWidth()*3/5)-(getWidth()/2);
        int y = (owner.getHeight()*3/5)-(getHeight()/2);
        this.setLocation(x,y);
        this.setVisible(true);

    }
    private void addComponent(JPanel center){
        JPanel east = new JPanel();
        JPanel west = new JPanel();
        center.setLayout(new GridLayout(1,2));
        east.setLayout(new GridLayout(2,1));
        west.setLayout(new GridLayout(2,1));
        checkBoxCase = new JCheckBox("Match Case",false);
        checkBoxWhole = new JCheckBox("Match word",false);
        ButtonGroup group = new ButtonGroup();
        radioButtonUp = new JRadioButton("Search Up",false);
        radioButtonDown = new JRadioButton("Sarch down",true);
        group.add(radioButtonUp);
        group.add(radioButtonDown);
        east.add(checkBoxCase);
        east.add(checkBoxWhole);
        east.setBorder(BorderFactory.createTitledBorder("Search Options"));
        west.add(radioButtonUp);
        west.add(radioButtonDown);
        west.setBorder(BorderFactory.createTitledBorder("Search Direction"));
        center.add(east);
        center.add(west);

    }
   //frame maker method
    private void setFindPanel(JPanel north){
        final JButton NEXT = new JButton("Find Next");
        NEXT.addActionListener(this);
        NEXT.setEnabled(false);
        searchText = new JTextField(20);
        searchText.addActionListener(this);
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean state = (searchText.getDocument().getLength()>0);
                NEXT.setEnabled(state);
                foundOne = false;
            }
        });

        if(searchText.getText().length()>0){
            NEXT.setEnabled(true);
        }
        north.add(new JLabel("Find Word: "));
        north.add(searchText);
        north.add(NEXT);
    }


    private void setReplacePanel(JPanel north){
        GridBagLayout bagLayout = new GridBagLayout();
        north.setLayout(bagLayout);
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.HORIZONTAL;
        JLabel labelFindWord = new JLabel("Find Word:");
        JLabel labelReplaceWord = new JLabel("Replace word:");
        final JButton NEXT = new JButton("Replace Text");
        NEXT.addActionListener(this);
        NEXT.setEnabled(false);
        final JButton REPLACE = new JButton("Replace All");
        REPLACE.addActionListener(this);
        REPLACE.setEnabled(false);
        searchText = new JTextField(20);
        replaceText = new JTextField(20);
        replaceText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean state = (replaceText.getDocument().getLength()>0);
                NEXT.setEnabled(state);
                REPLACE.setEnabled(state);
                foundOne = false;
            }
        });
        con.gridx = 0;
        con.gridy = 0;
        bagLayout.setConstraints(labelFindWord,con);
        north.add(labelFindWord);
        con.gridx = 1;
        con.gridy = 0;
        bagLayout.setConstraints(searchText,con);
        north.add(searchText);
        con.gridx = 2;
        con.gridy = 0;
        bagLayout.setConstraints(NEXT,con);
        north.add(NEXT);
        con.gridx = 0;
        con.gridy = 1;
        bagLayout.setConstraints(labelReplaceWord,con);
        north.add(labelReplaceWord);
        con.gridx = 1;
        con.gridy = 1;
        bagLayout.setConstraints(replaceText,con);
        north.add(replaceText);
        con.gridx = 2;
        con.gridy = 1;
        bagLayout.setConstraints(REPLACE,con);
        north.add(REPLACE);




    }
    private void process(){
        if(isReplace){
            labelStatusInfo.setText("Replacing : '"+searchText.getText()+"'");
        }else {
            labelStatusInfo.setText("Searching For : '"+ searchText.getText()+"'");
        }
        int caret = NotPad.getTextArea().getCaretPosition();
        String word = getWord();
        String text = getAllText();
        caret = search(text,word,caret);
        if(caret<0){
            endResult(false,0);
        }
    }

    // searching method making
    private int search(String text,String word,int caret){
        boolean found = false;
        int all = text.length();
        int check = word.length();
        if(isSearchDown()){
            int add = 0;
            for(int i = caret+1;i<=(all-check);i++){
                String temp = text.substring(i,(i+check));
                if(temp.equals(word)){
                    if(isWholeWordSearch()){
                        if(checkForWholeWorld(check,text,add,caret)){
                            caret = i;
                            found = true;
                            break;
                        }
                    }else {
                        caret = i;
                        found = true;
                        break;
                    }
                }
            }
        }else {
            int add = caret;
            System.out.println(add);
            for(int i = caret-1;i>= check; i--){
                add --;
                String temp = text.substring(i-check,i);
                if(temp.equals(word)){
                    if(isWholeWordSearch()){
                        if(checkForWholeWorld(check,text,add,caret)){
                            caret = i;
                            found = true;
                            break;
                        }
                    }else {
                        caret = i;
                        found = true;
                        break;
                    }
                }
            }
        }
        NotPad.getTextArea().setCaretPosition(0);
        if(found){
            NotPad.getTextArea().requestFocus();
            if(isSearchDown()){
                NotPad.getTextArea().select(caret,caret+check);
            }else {
                NotPad.getTextArea().select(caret -check,caret);
            }
            //for Replace
            if(isReplace){
                String replace = replaceText.getText();
                NotPad.getTextArea().replaceSelection(replace);
                if(isSearchDown()){
                    NotPad.getTextArea().select(caret,caret+replace.length());
                }else {
                    NotPad.getTextArea().select(caret-check,caret+replace.length()-1);
                }
            }
            foundOne = true;
            return caret;
        }

        return -1;
    }
    private void endResult(boolean isReplaceAll,int tally){
        String message = "";
        if(isReplaceAll){
            if(tally == 0){
                message = searchText.getText() + " not found";
            }else if(tally == 1){
                message = "one change was made to "+searchText.getText();
            }else {
                message = ""+tally + "changes were made to "+searchText.getText();
            }


        }else {
            String str = "";
            if(isSearchDown()){
                str = "Search Down";
            }else {
                str = "Search Up";
            }
            if(foundOne && !isReplace){
                message = "End of" + str +" for   "+searchText.getText();
            }else if(foundOne && isReplace){
                message = "End of Replace"+searchText.getText() +" with"+replaceText.getText();
            }
        }
        labelStatusInfo.setText(message);
    }
    private String getWord(){
        if(caseNotSelected()){
            return searchText.getText().toLowerCase();
        }
        return searchText.getText();
    }
    private String getAllText(){
        if(caseNotSelected()){
            return NotPad.getTextArea().getText().toLowerCase();
        }
        return NotPad.getTextArea().getText();
    }
    private  boolean caseNotSelected(){
        return !checkBoxCase.isSelected();
    }

    private boolean isSearchDown(){
        return radioButtonDown.isSelected();
    }
    private boolean isWholeWordSearch(){
        return checkBoxWhole.isSelected();
    }
    private  boolean checkForWholeWorld(int check,String text,int add,int caret){
        int offSetLeft = (caret + add) - 1;
        int offSetRight = (caret + add) + check;
        if((offSetLeft<0) || offSetRight>text.length() ){
            return true;
        }
        return ((!Character.isLetterOrDigit(text.charAt(offSetLeft))) &&
                (!Character.isLetterOrDigit(text.charAt(offSetRight))));
    }

    private void replaceAll(){
        String word = searchText.getText();
        String text = NotPad.getTextArea().getText();
        String insert = replaceText.getText();
        StringBuffer stringBuffer = new StringBuffer(text);
        int diff = insert.length() - word.length();
        int offset = 0;
        int tally = 0;
        for(int i = 0; i<=text.length()-word.length();i++){
            String temp = text.substring(i,i+word.length());
            System.out.println(temp);
            if(temp.equals(word) && checkForWholeWorld(word.length(),text,0,i)){
                tally++;
                stringBuffer.replace(i+offset,i+offset+ word.length(),insert);
                offset = offset +diff;
            }
            NotPad.getTextArea().setText(stringBuffer.toString());
            endResult(true,tally);
            NotPad.getTextArea().setCaretPosition(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(replaceText)||(e.getSource().equals(searchText))){
            validate();
        }

        process();

        if(e.getActionCommand().equals("Replace All")){
            replaceAll();
        }
    }

}
