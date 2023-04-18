package hr.mlinx.board;

import hr.mlinx.malkfilipp_maze_runner.Maze;
import hr.mlinx.ui.Canvas;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Grid {

    private final int columns, rows;
    private final Tile[][] tiles;
    private Tile startTile, goalTile;
    private double tileWidth, tileHeight;

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;

        tiles = new Tile[columns][rows];
        for (int i = 0; i < columns; ++i) {
            for (int j = 0; j < rows; ++j) {
                tiles[i][j] = new Tile(i, j);
            }
        }
        startTile = tiles[getDefaultStartX()][getDefaultStartY()];
        goalTile = tiles[getDefaultGoalX()][getDefaultGoalY()];
        startTile.setStart(true);
        goalTile.setGoal(true);

        setTileSizes(Canvas.INITIAL_WIDTH, Canvas.INITIAL_HEIGHT);
    }

    public void render(Graphics2D g2) {
        for (int i = 0; i < columns; ++i) {
            for (int j = 0; j < rows; ++j) {
                tiles[i][j].render(g2, tileWidth, tileHeight);
            }
        }
    }

    public void clear(boolean clearPath, boolean clearSolid, boolean resetStartGoal) {
        for (int i = 0; i < columns; ++i) {
            for (int j = 0; j < rows; ++j) {
                if (clearSolid) {
                    tiles[i][j].setSolid(false);
                }
                if (clearPath) {
                    tiles[i][j].setPath(false);
                    tiles[i][j].setChecked(false);
                    tiles[i][j].setMarked(false);
                    tiles[i][j].setParent(null);
                }
            }
        }

        if (resetStartGoal) {
            unsetStartGoal();
            setStartGoal(getDefaultStartX(), getDefaultStartY(), getDefaultGoalX(), getDefaultGoalY());
        }
    }

    public void clearPath() {
        clear(true, false, false);
    }

    public void clearSolid() {
        clear(false, true, false);
    }

    public void clearAll() {
        clear(true, true,  false);
    }

    public void reset() {
        clear(true, true, true);
    }

    public void randomMaze() {
        Maze maze = new Maze(rows, columns);
        unsetStartGoal();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                // indices are flipped here because
                // the Maze class is created to be accessed as a matrix where
                // the i is row and j is column,
                // whereas the in the Grid and Tile classes i is column and j is row
                // to sort of match how things are drawn on the screen regarding the x and y coordinates
                tiles[j][i].setSolid(maze.getGrid()[i][j].isWall());
            }
        }

        setStartGoal(maze.getEntranceColumn(), 0, maze.getExitColumn(), rows - 1);
    }

    // if this method is used, the setStartGoal method has to be used at an appropriate point afterwards
    private void unsetStartGoal() {
        startTile.setStart(false);
        goalTile.setGoal(false);
    }

    private void setStartGoal(int startColumn, int startRow, int goalColumn, int goalRow) {
        Tile newStartTile = tiles[startColumn][startRow];
        Tile newGoalTile = tiles[goalColumn][goalRow];
        newStartTile.setStart(true);
        newGoalTile.setGoal(true);
        startTile = newStartTile;
        goalTile = newGoalTile;
    }

    public void uiSetStartTile(Tile tile) {
        if (tile.isGoal()) return;

        startTile.setStart(false);
        tile.setStart(true);
        startTile = tile;
    }

    public void uiSetGoalTile(Tile tile) {
        if (tile.isStart()) return;

        goalTile.setGoal(false);
        tile.setGoal(true);
        goalTile = tile;
    }

    public Tile getTile(int column, int row) {
        return tiles[column][row];
    }

    // this method marks the edges that are not checked,
    // so it should be used before repainting the canvas
    public List<Tile> getUncheckedEdges(Tile tile) {
        List<Tile> edges = new ArrayList<>();
        Tile edge;

        try {
            edge = tiles[tile.getColumn() - 1][tile.getRow()];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {/**/}
        try {
            edge = tiles[tile.getColumn()][tile.getRow() + 1];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {/**/}
        try {
            edge = tiles[tile.getColumn() + 1][tile.getRow()];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {/**/}
        try {
            edge = tiles[tile.getColumn()][tile.getRow() - 1];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {/**/}

        return edges;
    }

    public void resetTileCosts() {
        for (int i = 0; i < columns; ++i) {
            for (int j = 0; j < rows; ++j) {
                tiles[i][j].setGCost(0);
                tiles[i][j].setHCost(0);
                tiles[i][j].setFCost(0);
            }
        }
    }

    public void setTileSizes(int canvasWidth, int canvasHeight) {
        tileWidth = (double) canvasWidth / columns;
        tileHeight = (double) canvasHeight / rows;
    }

    public void calculateHCosts() {
        for (int i = 0; i < columns; ++i) {
            for (int j = 0; j < rows; ++j) {
                tiles[i][j].setHCost(tiles[i][j].getManhattanDistance(goalTile));
            }
        }
    }

    public int calculateMaxHCost() {
        int furthestColumn = goalTile.getColumn() >= columns / 2 ? 0 : columns - 1;
        int furthestRow = goalTile.getRow() >= rows / 2 ? 0 : rows - 1;

        return goalTile.getManhattanDistance(tiles[furthestColumn][furthestRow]);
    }

    private int getDefaultStartX() {
        return getColumns() / 4;
    }

    private int getDefaultStartY() {
        return getRows() / 2;
    }

    private int getDefaultGoalX() {
        return getColumns() * 3 / 4;
    }

    private int getDefaultGoalY() {
        return getRows() / 2;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Tile getStartTile() {
        return startTile;
    }

    public Tile getGoalTile() {
        return goalTile;
    }
}
