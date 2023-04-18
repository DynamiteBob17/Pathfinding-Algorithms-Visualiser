package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class GreedyBestFirstSearch extends SearchAlgorithm {

    public GreedyBestFirstSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    @Override
    protected long sleepMilli() {
        return 33;
    }

    @Override
    public String getIdentifier() {
        return "Greedy";
    }

    @Override
    public void search() {
        PriorityQueue<Tile> prioQueue = new PriorityQueue<>(Comparator.comparingInt(Tile::getHCost));

        prioQueue.add(startTile);

        while (!prioQueue.isEmpty()) {
            Tile current = prioQueue.poll();

            List<Tile> edges = grid.getUncheckedEdges(current);

            if (commonMiddleStep(current)) {
                return;
            }

            for (Tile edge : edges) {
                if (!prioQueue.contains(edge)) {
                    prioQueue.add(edge);
                    edge.setParent(current);
                }
            }

            sleep();
        }
    }

}
