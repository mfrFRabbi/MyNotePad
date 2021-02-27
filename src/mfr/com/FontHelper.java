package mfr.com;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class FontHelper extends JDialog implements ListSelectionListener {
    JPanel pan1, pan2, pan3;
    JLabel fontLabel, sizeLabel, typeLabel, previewLabel;
    JTextField previewText, fontText, sizeText, typeText;
    JScrollPane fontScroll, typeScroll, sizeScroll;
    JList fontList, sizeList, typeList;
    JButton ok, cancle;
    GridBagLayout gbl;
    GridBagConstraints gbc;

    public FontHelper() {
        this.setTitle("Choose Font");
        this.setSize(400, 500);
        this.setResizable(false);
        gbl = new GridBagLayout();
        this.setLayout(gbl);
        gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        fontLabel = new JLabel("Fonts");
        this.getContentPane().add(fontLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        sizeLabel = new JLabel("Sizes:");
        this.getContentPane().add(sizeLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        typeLabel = new JLabel("Types:");
        this.getContentPane().add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        fontText = new JTextField("Arial", 12);
        this.getContentPane().add(fontText, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        sizeText = new JTextField("20", 4);
        this.getContentPane().add(sizeText, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        typeText = new JTextField("8", 6);
        this.getContentPane().add(typeText, gbc);
//list add
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontList = new JList(fonts);
        fontList.setFixedCellWidth(110);
        fontList.addListSelectionListener(this);
        fontList.setSelectedIndex(3);
        fontScroll = new JScrollPane(fontList);
        this.getContentPane().add(fontScroll, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] sizes = {"8", "10", "12", "16", "18", "20", "24", "28", "32", "33", "35"};
        sizeList = new JList(sizes);
        sizeList.setFixedCellWidth(32);
        sizeList.addListSelectionListener(this);
        sizeList.setSelectedIndex(5);
        sizeScroll = new JScrollPane(sizeList);
        this.getContentPane().add(sizeScroll, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] types = {"Regular", "Bold", "Italic", "Bold Italic"};
        typeList = new JList(types);
        typeList.setFixedCellWidth(60);
        typeList.addListSelectionListener(this);
        typeList.setSelectedIndex(0);
        typeScroll = new JScrollPane(typeList);
        this.getContentPane().add(typeScroll, gbc);
//preview
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        pan1 = new JPanel();
        pan1.setLayout(new FlowLayout());
        previewLabel = new JLabel("Preview:");
        pan1.add(previewLabel);
        this.getContentPane().add(pan1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        pan2 = new JPanel();
        pan2.setLayout(new FlowLayout());
        previewText = new JTextField("aaAAbbBBccCCddDDeeEEffFF");
        previewText.setEditable(false);
        previewText.setBorder(BorderFactory.createEtchedBorder());
        previewText.setFont(new Font("Arial", Font.PLAIN, 20));
        pan2.add(previewText);
        this.getContentPane().add(pan2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        pan3 = new JPanel();
        pan3.setLayout(new FlowLayout());
        ok = new JButton("OK");
        cancle = new JButton("Cancel");
        ok.setFocusable(false);
        cancle.setFocusable(false);
        pan3.add(ok);
        pan3.add(cancle);
        this.getContentPane().add(pan3, gbc);
    }

    public Font font() {
        Font font = new Font((String) fontList.getSelectedValue(), typeList.getSelectedIndex(), Integer.parseInt((String) sizeList.getSelectedValue()));
        return font;
    }

    public JButton getOk() {
        return ok;
    }

    public JButton getCancle() {
        return cancle;
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            if (e.getSource() == fontList) {
                Font font = new Font((String) fontList.getSelectedValue(), typeList.getSelectedIndex(), Integer.parseInt((String) sizeList.getSelectedValue()));
                fontText.setText((String) fontList.getSelectedValue());
                previewText.setFont(font);
            }
            if (e.getSource() == sizeList) {
                Font font = new Font((String) fontList.getSelectedValue(), typeList.getSelectedIndex(), Integer.parseInt((String) sizeList.getSelectedValue()));
                sizeText.setText((String) sizeList.getSelectedValue());
                previewText.setFont(font);

            }
            if (e.getSource() == typeList) {
                Font font = new Font((String) fontList.getSelectedValue(), typeList.getSelectedIndex(), Integer.parseInt((String) sizeList.getSelectedValue()));
                typeText.setText((String) typeList.getSelectedValue());
                previewText.setFont(font);

            }

        } catch (NullPointerException e1){
            e1.fillInStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }
}
