package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AStarSearch extends SearchAlgorithm {

    public AStarSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    @Override
    public String getIdentifier() {
        return "A*";
    }

    @Override
    protected long sleepMilli() {
        return 33;
    }

    @Override
    public void search() {
        Comparator<Tile> comparator = Comparator.comparingInt(Tile::getFCost).thenComparingInt(Tile::getHCost);
        // openSet acts as a priority queue; if in this specific algorithm implementation
        // a PriorityQueue class was used then the search wouldn't work
        // properly since we are adding tiles to the openSet before calculating costs
        // and the PriorityQueue determines the place of the element in the queue upon adding it
        List<Tile> openSet = new ArrayList<>();

        openSet.add(startTile);

        while (!openSet.isEmpty()) {
            // should never be null because openSet should not be empty
            Tile current = openSet.stream().min(comparator).orElse(null);
            openSet.remove(current);
            List<Tile> edges = grid.getUncheckedEdges(current);

            if (commonMiddleStep(current)) {
                return;
            }

            for (Tile edge : edges) {
                    int totalWeight = current.getGCost() + 1;

                    if (!openSet.contains(edge)) {
                        openSet.add(edge);
                    } else if (totalWeight >= edge.getGCost()) {
                        continue;
                    }

                    edge.setGCost(totalWeight);
                    edge.setFCost(edge.getGCost() + edge.getHCost());
                    edge.setParent(current);
            }

            sleep();
        }
    }

}
