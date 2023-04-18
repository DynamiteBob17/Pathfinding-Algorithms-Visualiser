package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

public abstract class SearchAlgorithm implements Runnable {

    protected Grid grid;
    protected Canvas canvas;
    protected SoundPlayer soundPlayer;
    protected Tile startTile, goalTile;

    public SearchAlgorithm(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        this.grid = grid;
        this.canvas = canvas;
        this.soundPlayer = soundPlayer;
    }

    // this is just for the ui
    public abstract String getIdentifier();

    protected abstract void search();

    protected abstract long sleepMilli();

    @Override
    public void run() {
        canvas.setSearchRunning(true);
        canvas.setStopRunningSearch(false);
        canvas.turnOffPauseRunningSearch();

        startTile = grid.getStartTile();
        goalTile = grid.getGoalTile();
        grid.calculateHCosts();
        soundPlayer.setMaxHCost(grid.calculateMaxHCost());

        search();

        if (!canvas.isStopRunningSearch()) {
            trackAndFillPath();
        }

        soundPlayer.notesOff();

        canvas.setSearchRunning(false);
        canvas.setStopRunningSearch(false);
        canvas.turnOffPauseRunningSearch();
    }

    protected void sleep() {
        long nano = (long) (sleepMilli() * 1_000_000.0);
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + nano >= end);
    }

    protected boolean commonMiddleStep(Tile current) {
        current.setChecked(true);
        soundPlayer.play(current.getHCost());
        canvas.repaint();

        while (canvas.isPauseRunningSearch() && !canvas.isStopRunningSearch())
            sleep();

        return current.isGoal() || canvas.isStopRunningSearch();
    }

    protected void trackAndFillPath() {
        Tile current = goalTile;

        while (current.hasParent()) {
            current.setPath(true);
            soundPlayer.play(current.getHCost());
            canvas.repaint();
            sleep();
            current = current.getParent();
        }

        current.setPath(true);
        soundPlayer.play(current.getHCost());
    }

}
