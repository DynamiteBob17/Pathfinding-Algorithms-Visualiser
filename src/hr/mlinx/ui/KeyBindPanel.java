package hr.mlinx.ui;

import hr.mlinx.actions.KeyBindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeyBindPanel extends JPanel {

    private final static Color FOCUSED_BACKGROUND = new Color(221, 221, 221);
    private final static Color FOCUSED_FOREGROUND = new Color(30, 30, 30);

    private final JLabel[] keyLabels;

    public KeyBindPanel(KeyBindings keyBindings) {
        super();

        int count = keyBindings.count();
        setLayout(new GridLayout(count, 2));
        keyLabels = new JLabel[count];
        for (int i = 0; i < count; ++i) {
            keyLabels[i] = new JLabel(keyBindings.getKey(i));

            // anon = anonymous (class);
            // these variables will be set in the listener instances as
            // synthetic instance variables so each instance will have
            // the proper indices to get and use the JLabel's
            // (or the JLabel objects themselves by the index, not sure :] )
            final int iForAnon = i;
            final int countForAnon = count;
            // tbh using indices for everything related to key bindings here
            // and in other classed/methods is kinda scuffed :),
            // but whatever it's fine, maybe the KeyBindings class can be implemented differently
            // or use iterators so that it's simpler to use and doesn't require all this indexing;
            // right now won't bother since everything related to key bindings will have to be rewritten
            // if the KeyBindings class itself is implemented differently
            keyLabels[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (Character.isDefined(e.getKeyCode()) &&
                            (Character.isLetterOrDigit(e.getKeyCode())
                            || e.getKeyCode() == KeyEvent.VK_SPACE
                            || e.getKeyCode() == KeyEvent.VK_UP
                            || e.getKeyCode() == KeyEvent.VK_DOWN
                            || e.getKeyCode() == KeyEvent.VK_LEFT
                            || e.getKeyCode() == KeyEvent.VK_RIGHT)) {
                        String keyBinding = KeyEvent.getKeyText(e.getKeyCode()).toUpperCase();
                        String currentKeyBinding = keyBindings.getKey(iForAnon);

                        for (int j = 0; j < countForAnon; ++j) {
                            if (keyBindings.getKey(j).equals(keyBinding)) {
                                keyBindings.setKey(j, currentKeyBinding);
                                keyLabels[j].setText(currentKeyBinding);
                                break;
                            }
                        }

                        keyBindings.setKey(iForAnon, keyBinding);
                        keyLabels[iForAnon].setText(keyBinding);
                    }
                }
            });
            keyLabels[i].setFocusable(true);
            keyLabels[i].addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    keyLabels[iForAnon].setOpaque(true);
                    keyLabels[iForAnon].setBackground(FOCUSED_BACKGROUND);
                    keyLabels[iForAnon].setForeground(FOCUSED_FOREGROUND);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    keyLabels[iForAnon].setBackground(FOCUSED_FOREGROUND);
                    keyLabels[iForAnon].setForeground(FOCUSED_BACKGROUND);
                }
            });
            keyLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    keyLabels[iForAnon].requestFocusInWindow();
                }
            });
            keyLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            keyLabels[i].setFont(keyLabels[i].getFont().deriveFont(Font.BOLD));

            add(new JLabel(keyBindings.getDescription(i)));
            add(keyLabels[i]);
        }
    }

}
