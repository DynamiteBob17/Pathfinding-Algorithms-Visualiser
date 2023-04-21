package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

import java.util.*;

public abstract class SimpleQueueSearch extends SearchAlgorithm {

    public SimpleQueueSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    protected void simpleQueueSearch(boolean usesHeuristic) {
        Queue<Tile> queue = usesHeuristic
                ? new PriorityQueue<>(Comparator.comparingInt(Tile::getHCost))
                : new ArrayDeque<>();

        queue.add(startTile);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            List<Tile> edges = grid.getUncheckedEdges(current);

            if (commonMiddleStep(current)) {
                return;
            }

            for (Tile edge : edges) {
                if (!queue.contains(edge)) {
                    queue.add(edge);
                    edge.setParent(current);
                }
            }

            sleep();
        }
    }

}
