package hr.mlinx.ui;

import hr.mlinx.algorithms.DepthFirstSearch;
import hr.mlinx.algorithms.SearchAlgorithm;
import hr.mlinx.board.Grid;
import hr.mlinx.util.SoundPlayer;
import hr.mlinx.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Frame extends JFrame {

    private SearchAlgorithm currentSearch;

    public Frame() {
        super();

        Grid grid = new Grid(8);
        Canvas canvas = new Canvas(grid);
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component component = e.getComponent();
                grid.setTileSizes(component.getWidth(), component.getHeight());
                component.repaint();
            }
        });

        SoundPlayer soundPlayer = new SoundPlayer();
        setSearchAndTitle(new DepthFirstSearch(grid, canvas, soundPlayer));
        JMenuBar menuBar = new MenuBar(grid, canvas, this, soundPlayer);

        setJMenuBar(menuBar);
        add(canvas);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public SearchAlgorithm getCurrentSearch() {
        return currentSearch;
    }

    public void setSearchAndTitle(SearchAlgorithm currentSearch) {
        this.currentSearch = currentSearch;
        setTitle("Pathfinding Algorithms Visualiser (" + currentSearch.getIdentifier() + ")");
    }

    public static void main(String[] args) {
        if (Util.isUnix()) {
            System.setProperty("sun.java2d.opengl", "true");
        }
        Util.configureUI();
        SwingUtilities.invokeLater(Frame::new);
    }

}
