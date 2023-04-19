package hr.mlinx.actions;

import hr.mlinx.board.Tile;
import hr.mlinx.board.Grid;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.Util;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GridMouseAdapter extends MouseAdapter {

    private final Grid grid;
    private final Canvas canvas;
    private boolean startPressed, goalPressed;

    public GridMouseAdapter(Grid grid, Canvas canvas) {
        super();

        this.grid = grid;
        this.canvas = canvas;
    }

    private int mapMouseXToColumn(MouseEvent e) {
        return (int) Util.map(e.getPoint().getX(), 0, canvas.getWidth(), 0, grid.getColumns());
    }

    private int mapMouseYToRow(MouseEvent e) {
        return (int) Util.map(e.getPoint().getY(), 0, canvas.getHeight(), 0, grid.getRows());
    }

    private Tile getTileFromMouse(MouseEvent e) {
        int x = mapMouseXToColumn(e);
        int y = mapMouseYToRow(e);

        if (x >= grid.getColumns())
            x = grid.getColumns() - 1;
        else if (x < 0)
            x = 0;
        if (y >= grid.getRows())
            y = grid.getRows() - 1;
        else if (y < 0)
            y = 0;

        return grid.getTile(x, y);
    }

    private void setTileState(MouseEvent e, boolean calledOnMousePressed) {
        Tile tile = getTileFromMouse(e);

        if (calledOnMousePressed) {
            if (tile.isStart())
                startPressed = true;
            if (tile.isGoal())
                goalPressed = true;
        }

        if (startPressed) {
            grid.uiSetStartTile(tile);
        } else if (goalPressed) {
            grid.uiSetGoalTile(tile);
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            tile.setSolid(true);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            tile.setSolid(false);
        }

        canvas.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (canvas.searchNotRunning()) {
            setTileState(e, true);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (canvas.searchNotRunning()) {
            setTileState(e, false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (canvas.searchNotRunning()) {
            if (startPressed || goalPressed)
                getTileFromMouse(e).setSolid(false);

            startPressed = goalPressed = false;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (canvas.searchNotRunning()) {
            if (e.getWheelRotation() < 0) {
                grid.decrement(canvas.getWidth(), canvas.getHeight());
                canvas.repaint();
            } else if (e.getWheelRotation() > 0) {
                grid.increment(canvas.getWidth(), canvas.getHeight());
                canvas.repaint();
            }
        }
    }
}
