package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearch extends SearchAlgorithm {

    public DepthFirstSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    @Override
    public String getIdentifier() {
        return "Depth";
    }

    @Override
    protected long sleepMilli() {
        return 20;
    }

    @Override
    public void search() {
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
