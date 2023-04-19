package hr.mlinx.maze_runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazeGenerator {

    // recursive backtracking
    public static double[][] generateMaze(int c, int r) {
        if (c % 2 == 0 || r % 2 == 0
            || c < 3 || r < 3)
            throw new IllegalArgumentException("Both columns and rows of the maze must be odd numbers and >= 3.");

        int columns = c + 2, rows = r + 2;

        // 0 = passage, 1 = wall, 0.5 = visited
        double[][] maze = new double[columns][rows];

        // creates a grid which has a fake outer helper visited layer as 0.5,
        // then another layer of borders as walls,
        // then in that border of walls an alternating pattern of walls and passages,
        // like this:
        // .5 .5 .5 .5 .5 .5 .5
        // .5  1  1  1  1  1 .5
        // .5  1  0  1  0  1 .5
        // .5  1  1  0  1  1 .5
        // .5  1  0  1  0  1 .5
        // .5  1  1  1  1  1 .5
        // .5 .5 .5 .5 .5 .5 .5
        for (int i = 0; i < columns; ++i) {
            for (int j = 0; j < rows; ++j) {
                if (i % 2 == 1 || j % 2 == 1)
                    maze[i][j] = 1;
                if (i == 0 || j == 0 || i == columns - 1 || j == rows - 1)
                    maze[i][j] = 0.5;
            }
        }

        Random random = new Random();
        // numbers like [2, 4, 6, ..., up to columns - 3];
        // these coordinates are guaranteed to be unvisited
        int startX = random.nextInt(1, (columns - 2) / 2 + 1) * 2;
        int startY = random.nextInt(1, (rows - 2) / 2 + 1) * 2;
        generate(startX, startY, maze);

        return maze;
    }

    private static void generate(int x, int y, double[][] maze) {
        maze[x][y] = 0.5;

        if (maze[x - 2][y] != 0.5 || maze[x + 2][y] != 0.5
            || maze[x][y - 2] != 0.5 || maze[x][y + 2] != 0.5) {
            List<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 2, 3)); // up, down, left, right

            while (!directions.isEmpty()) {
                int dir = directions.remove(rand(directions.size()));
                int nx = x, ny = y, mx = x, my = y;

                // after randomly picking a direction, get the wall set for removal between 2 tiles/cells
                // and the potentially unvisited passage tile that connects the wall with the current x, y
                if (dir == 0) {
                    ny -= 2;
                    my -= 1;
                } else if (dir == 1) {
                    ny += 2;
                    my += 1;
                } else if (dir == 2) {
                    nx -= 2;
                    mx -= 1;
                } else if (dir == 3) {
                    nx += 2;
                    mx += 1;
                }

                // carve the chosen path if we haven't visited the selected passage tile
                // by removing the wall and continuing the recursion on the selected tile
                if (maze[nx][ny] != 0.5) {
                    maze[mx][my] = 0.5;
                    generate(nx, ny, maze);
                }
            }
        }
    }

    private static int rand(int range) {
        return (int) (Math.random() * range);
    }

}
