package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

public class BreadthFirstSearch extends SimpleQueueSearch {

    public BreadthFirstSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    @Override
    public void search() {
        simpleQueueSearch(false);
    }

    @Override
    protected long sleepMin() {
        return 2;
    }

    @Override
    protected long sleepMax() {
        return 28;
    }

    @Override
    public String getIdentifier() {
        return "Breadth";
    }

}
