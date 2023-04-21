package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

public class DijkstrasAlgorithm extends GWeightedQueueSearch {

    public DijkstrasAlgorithm(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    @Override
    public void search() {
        gWeightedQueueSearch(false);
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
        return "Dijkstra";
    }

}
