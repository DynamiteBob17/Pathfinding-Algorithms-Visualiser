package hr.mlinx.algorithms;

import hr.mlinx.board.Grid;
import hr.mlinx.board.Tile;
import hr.mlinx.ui.Canvas;
import hr.mlinx.util.SoundPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class GWeightedQueueSearch extends SearchAlgorithm {

    public GWeightedQueueSearch(Grid grid, Canvas canvas, SoundPlayer soundPlayer) {
        super(grid, canvas, soundPlayer);
    }

    protected void gWeightedQueueSearch(boolean usesHeuristic) {
        Comparator<Tile> comparator;
        if (usesHeuristic)
            comparator = Comparator.comparingInt(Tile::getFCost).thenComparingInt(Tile::getHCost);
        else
            comparator = Comparator.comparingInt(Tile::getGCost);

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

                if (usesHeuristic)
                    edge.setFCost(edge.getGCost() + edge.getHCost());

                edge.setParent(current);
            }

            sleep();
        }
    }

}
