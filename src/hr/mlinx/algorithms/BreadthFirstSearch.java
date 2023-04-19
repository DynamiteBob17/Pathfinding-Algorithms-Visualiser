package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class BreadthFirstSearch extends SearchAlgorithm {

    public BreadthFirstSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    @Override
    public void search() {
        List<Tile> queue = new ArrayList<>();

        queue.add(startTile);

        while (!queue.isEmpty()) {
            Tile current = queue.remove(0);

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
