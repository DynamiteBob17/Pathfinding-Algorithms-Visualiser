package hr.mlinx.board;

import hr.mlinx.malkfilipp_maze_runner.Maze;
import hr.mlinx.maze_runner.MazeGenerator;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.MazeAlgo;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import static hr.mlinx.board.GridDimensionChange.DECREMENT;
import static hr.mlinx.board.GridDimensionChange.INCREMENT;

public class Grid {

    // these are just for painting the canvas so that
    // every time we change the number of columns and rows
    // we add 6 columns and 4 rows to keep some kind of
    // aspect ratio so that the tiles appear as close as possible to
    // a square; also, we are not making it 3 and 2 because we need the
    // number of columns and rows to always be odd
    private static final int COLUMN_ASPECT = 6;
    private static final int ROW_ASPECT = 4;
    public static final int MAX_STEPS = 30;
    private static final int MIN_COLUMNS = 5;
    private static final int MAX_COLUMNS = calculateColumns(MAX_STEPS);
    private static final int MIN_ROWS = 3;
    private static final int MAX_ROWS = calculateRows(MAX_STEPS);

    private int steps;
    private final int defaultSteps;
    private int columns, rows;
    private final Tile[][] tiles;
    private Tile startTile, goalTile;
    private double tileWidth, tileHeight;

    public Grid(int steps) {
        if (steps < 0)
            throw new IllegalArgumentException("Steps cannot be negative.");
        this.steps = steps;
        defaultSteps = steps;
        columns = calculateColumns(steps);
        rows = calculateRows(steps);

        // not defined as [rows][columns] to sort of match how it's drawn on screen in the xy cartesian system
        tiles = new Tile[MAX_COLUMNS][MAX_ROWS];
        for (int i = 0; i < MAX_COLUMNS; ++i) {
            for (int j = 0; j < MAX_ROWS; ++j) {
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

    public void reset(int canvasWidth, int canvasHeight) {
        steps = defaultSteps;
        columns = calculateColumns(steps);
        rows = calculateRows(steps);
        setTileSizes(canvasWidth, canvasHeight);

        clear(true, true, true);
    }

    public void randomMaze(MazeAlgo mazeAlgo) {
        unsetStartGoal();

        if (mazeAlgo == MazeAlgo.KRUSKAL) {
            Maze maze = new Maze(rows, columns);

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
        } else if (mazeAlgo == MazeAlgo.REC_BACK) {
            double[][] maze = MazeGenerator.generateMaze(columns, rows);

            for (int i = 0; i < columns; ++i) {
                for (int j = 0; j < rows; ++j) {
                    tiles[i][j].setSolid(maze[i + 1][j + 1] == 1);
                }
            }
        }

        int secondColumn = 1, firstRow = 0;
        int secondToLastColumn = columns - 2, lastRow = rows - 1;
        setStartGoal(secondColumn, firstRow, secondToLastColumn, lastRow);
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

        // we don't do this automatically when changing start/goal tile on the canvas in the ui
        // to not change the colors already on the cells we are dragging over,
        // only when we drop the start/tile do we set that tile to non-solid, but we do that
        // in the GridMouseAdapter manually
        startTile.setSolid(false);
        goalTile.setSolid(false);
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
        int column, row;

        if ((column = tile.getColumn() - 1) >= 0) {
            edge = tiles[column][tile.getRow()];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        }
        if ((row = tile.getRow() + 1) < rows) {
            edge = tiles[tile.getColumn()][row];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        }
        if ((column = tile.getColumn() + 1) < columns) {
            edge = tiles[column][tile.getRow()];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        }
        if ((row = tile.getRow() - 1) >= 0) {
            edge = tiles[tile.getColumn()][row];
            if (edge.notSolid() && edge.notChecked()) {
                edges.add(edge);
                edge.setMarked(true);
            }
        }

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

    private void changeStep(GridDimensionChange gdc, int canvasWidth, int canvasHeight) {
        int steps = this.steps + (gdc == DECREMENT ? -1 : 1);

        // only have to check to columns, because if columns are out of bounds, the rows will be too
        if (steps < 0 || steps > MAX_STEPS)
            return;

        this.steps = steps;
        this.columns = calculateColumns(steps);
        this.rows = calculateRows(steps);

        // check if start or goal tiles are out of the visible grid now
        // and set them to their default positions if so
        if (startTile.getColumn() >= columns || startTile.getRow() >= rows
            || goalTile.getColumn() >= columns || goalTile.getRow() >= rows) {
            unsetStartGoal();
            setStartGoal(getDefaultStartX(), getDefaultStartY(), getDefaultGoalX(), getDefaultGoalY());
        }

        setTileSizes(canvasWidth, canvasHeight);
    }

    public void decrement(int canvasWidth, int canvasHeight) {
        changeStep(DECREMENT, canvasWidth, canvasHeight);
    }

    public void increment(int canvasWidth, int canvasHeight) {
        changeStep(INCREMENT, canvasWidth, canvasHeight);
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

    public int getSteps() {
        return steps;
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

    private static int calculateDimension(int min, int aspect, int steps) {
        return min + aspect * steps;
    }

    private static int calculateColumns(int steps) {
        return calculateDimension(MIN_COLUMNS, COLUMN_ASPECT, steps);
    }

    private static int calculateRows(int steps) {
        return calculateDimension(MIN_ROWS, ROW_ASPECT, steps);
    }

}
