package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleStackSearch extends SearchAlgorithm {

    public SimpleStackSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    protected void simpleStackSearch() {
        List<Tile> stack = new ArrayList<>();

        stack.add(0, startTile);

        while (!stack.isEmpty()) {
            Tile current = stack.remove(0);

            List<Tile> edges = grid.getUncheckedEdges(current);

            if (commonMiddleStep(current)) {
                return;
            }

            for (Tile edge : edges) {
                if (!stack.contains(edge)) {
                    stack.add(0, edge);
                    edge.setParent(current);
                }
            }

            sleep();
        }
    }

}
