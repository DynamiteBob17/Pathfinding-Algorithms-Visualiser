package hr.mlinx.ui;

import hr.mlinx.actions.GridMouseAdapter;
import hr.mlinx.actions.KeyBindings;
import hr.mlinx.board.Grid;
import hr.mlinx.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class Canvas extends JPanel {

    public static final int INITIAL_WIDTH = (int) (1280 * Util.SCALE);
    public static final int INITIAL_HEIGHT = (int) (720 * Util.SCALE);
    private final Grid grid;
    private boolean searchRunning;
    private boolean pauseRunningSearch;
    private boolean stopRunningSearch;
    private final boolean isUnix;
    private Toolkit tk;

    public Canvas(Grid grid) {
        super();

        this.grid = grid;

        setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));

        MouseAdapter mouseAdapter = new GridMouseAdapter(grid, this);
        addMouseListener(mouseAdapter); // for just pressing/clicking
        addMouseMotionListener(mouseAdapter); // for dragging

        isUnix = Util.isUnix();
        if (isUnix) tk = Toolkit.getDefaultToolkit();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grid.render(g2);

        if (isUnix) tk.sync();
    }

    public void initialBindKeys(KeyBindings keyBindings, Action[] actionsToBind) {
        String actionKey;
        int count = keyBindings.count();
        for (int i = 0; i < count; ++i) {
            actionKey = keyBindings.getActionName(i);
            getInputMap().put(KeyStroke.getKeyStroke(keyBindings.getKey(i)), actionKey);
            getActionMap().put(actionKey, actionsToBind[i]);
        }
    }

    public void rebindKeys(String[] keysToRemove, KeyBindings keyBindings) {
        for (String keyBefore : keysToRemove) {
            getInputMap().remove(KeyStroke.getKeyStroke(keyBefore));
        }

        int count = keyBindings.count();
        String inputKey;
        for (int i = 0; i < count; ++i) {
            inputKey = keyBindings.getKey(i);
            getInputMap().put(KeyStroke.getKeyStroke(inputKey), keyBindings.getActionName(i));
        }
    }

    public void setSearchRunning(boolean searchRunning) {
        this.searchRunning = searchRunning;
    }

    public boolean searchNotRunning() {
        return !searchRunning;
    }

    public void togglePauseRunningSearch() {
        this.pauseRunningSearch = !pauseRunningSearch;
    }

    public void turnOffPauseRunningSearch() {
        pauseRunningSearch = false;
    }

    public boolean isPauseRunningSearch() {
        return pauseRunningSearch;
    }

    public void setStopRunningSearch(boolean stopRunningSearch) {
        this.stopRunningSearch = stopRunningSearch;
    }

    public boolean isStopRunningSearch() {
        return stopRunningSearch;
    }

}
