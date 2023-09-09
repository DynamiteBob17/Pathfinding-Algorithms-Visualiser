package hr.mlinx.board;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Tile {

    private static final double BORDER_STROKE = 1.0;
    private static final Color BORDER_COLOR = new Color(64, 64, 64);
    private static final Color START_COLOR = new Color(221,91,91);
    private static final Color GOAL_COLOR = new Color(119, 221, 119);
    private static final Color OPEN_COLOR = new Color(249, 248, 231);
    private static final Color SOLID_COLOR = new Color(0, 51, 102);
    private static final Color MARKED_COLOR = new Color (30, 119, 168);
    private static final Color CHECKED_COLOR = new Color(86, 163, 240);
    private static final Color PATH_COLOR = new Color(255, 165, 0);

    private Tile parent;
    private final int column, row;
    private int gCost, hCost, fCost;
    private boolean start, goal;
    private boolean checked, path, marked;
    private boolean solid;
    private Color currentColor;

    public Tile(int column, int row) {
        this.column = column;
        this.row = row;
        solid = false;
        currentColor = OPEN_COLOR;
    }

    public void render(Graphics2D g2, double tileWidth, double tileHeight) {
        double x = tileWidth * column;
        double y = tileHeight * row;
        g2.setColor(currentColor);
        g2.fill(new Rectangle2D.Double(x, y, tileWidth, tileHeight));
        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke((float) BORDER_STROKE));
        g2.draw(new Rectangle2D.Double(x, y, tileWidth, tileHeight));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Tile otherTile))
            return false;

        return this.column == otherTile.column && this.row == otherTile.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    @Override
    public String toString() {
        String state;
        if (start) state = "START";
        else if (goal) state = "GOAL";
        else if (solid) state = "SOLID";
        else state = "OPEN";

        return String.format("(%d, %d)=%s", column, row, state);
    }

    private void setColorBasedOnState() {
        if (solid)
            currentColor = SOLID_COLOR;
        else if (path)
            currentColor = PATH_COLOR;
        else if (checked)
            currentColor = CHECKED_COLOR;
        else if (marked)
            currentColor = MARKED_COLOR;
        else
            currentColor = OPEN_COLOR;
    }

    public void setStart(boolean start) {
        if (start && goal) return;

        this.start = start;

        if (start) {
            currentColor = START_COLOR;
        } else {
            setColorBasedOnState();
        }
    }

    public void setGoal(boolean goal) {
        if (goal && start) return;

        this.goal = goal;

        if (goal) {
            currentColor = GOAL_COLOR;
        } else {
            setColorBasedOnState();
        }
    }

    public void setSolid(boolean solid) {
        if (solid && (start || goal)) return;

        this.solid = solid;

        if (!start && !goal) {
            setColorBasedOnState();
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;

        if (!start && !goal) {
            setColorBasedOnState();
        }
    }

    public void setPath(boolean path) {
        this.path = path;

        if (!start && !goal) {
            setColorBasedOnState();
        }
    }

    public void setMarked(boolean marked) {
        this.marked = marked;

        if (!start && !goal) {
            setColorBasedOnState();
        }
    }

    public int getManhattanDistance(Tile other) {
        return Math.abs(this.getColumn() - other.getColumn())
                + Math.abs(this.getRow() - other.getRow());
    }

    public Tile getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setParent(Tile parent) {
        this.parent = parent;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isGoal() {
        return goal;
    }

    public boolean notChecked() {
        return !checked;
    }

    public boolean notSolid() {
        return !solid;
    }

}
