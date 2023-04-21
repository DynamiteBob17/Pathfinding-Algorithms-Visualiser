package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

public class AStarSearch extends GWeightedQueueSearch {

    public AStarSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    @Override
    public void search() {
        gWeightedQueueSearch(true);
    }

    @Override
    protected long sleepMin() {
        return 5;
    }

    @Override
    protected long sleepMax() {
        return 50;
    }

    @Override
    public String getIdentifier() {
        return "A*";
    }

}
