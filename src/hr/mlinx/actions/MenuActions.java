package hr.mlinx.actions;

import hr.mlinx.algorithms.SearchAlgorithm;
import hr.mlinx.board.Grid;
import hr.mlinx.ui.Canvas;
import hr.mlinx.ui.Frame;
import hr.mlinx.ui.KeyBindPanel;
import hr.mlinx.util.MazeAlgo;
import hr.mlinx.util.SoundPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuActions {

    private final Grid grid;
    private final Canvas canvas;
    private final Frame frame;

    public MenuActions(Grid grid, Canvas canvas, Frame frame) {
        this.grid = grid;
        this.canvas = canvas;
        this.frame = frame;
    }

    public Action createRunPauseAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    grid.clearPath();
                    grid.resetTileCosts();
                    canvas.repaint();
                    new Thread(frame.getCurrentSearch()).start();
                } else {
                    canvas.togglePauseRunningSearch();
                }
            }
        };
    }

    public Action createStopAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setStopRunningSearch(true);
            }
        };
    }

    public Action createAlgorithmAction(SearchAlgorithm searchAlgorithm) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    frame.setSearchAndTitle(searchAlgorithm);
                }
            }
        };
    }

    public Action createRandomMazeAction(MazeAlgo mazeAlgo) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    grid.clearAll();
                    grid.randomMaze(mazeAlgo);
                    canvas.repaint();
                }
            }
        };
    }

    public Action createClearPathAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    grid.clearPath();
                    canvas.repaint();
                }
            }
        };
    }

    public Action createClearSolidAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    grid.clearSolid();
                    canvas.repaint();
                }
            }
        };
    }

    public Action createClearAllAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    grid.clearAll();
                    canvas.repaint();
                }
            }
        };
    }

    public Action createToggleSoundAction(JMenuItem toggleSoundItem, SoundPlayer soundPlayer) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean soundOn = soundPlayer.toggleSound();
                toggleSoundItem.setText("Turn Sound " + (soundOn ? "OFF" : "ON"));
            }
        };
    }

    public Action createKeysAction(KeyBindings keyBindings) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] keysBefore = keyBindings.getCurrentKeys();
                KeyBindPanel keyBindPanel = new KeyBindPanel(keyBindings);

                int option = JOptionPane.showConfirmDialog(null, keyBindPanel, "Change Key Bindings", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    canvas.rebindKeys(keysBefore, keyBindings);
                    KeyBindings.save(keyBindings);
                } else {
                    for (int i = 0; i < keysBefore.length; ++i) {
                        keyBindings.setKey(i, keysBefore[i]);
                    }
                }
            }
        };
    }

    public Action createDefaultKeysAction(KeyBindings keyBindings) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "Set key bindings to default?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    String[] keysBefore = keyBindings.getCurrentKeys();
                    keyBindings.setDefaults();
                    canvas.rebindKeys(keysBefore, keyBindings);
                    KeyBindings.save(keyBindings);
                }
            }
        };
    }

    public Action createResetGridAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    grid.reset(canvas.getWidth(), canvas.getHeight());
                    canvas.repaint();
                }
            }
        };
    }

    public Action createMouseAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, """
                        Left-click to add a solid tile, right-click to remove it.
                        
                        Drag the start/goal tile to change its position.
                        
                        Use the scroll wheel to change the number of tiles.""");
            }
        };
    }

    public Action createInfoAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, """
                        Colors:
                            Red/green = start/goal tile
                            White = non-solid tile
                            Dark blue = solid tile
                            Grayish blue = tile marked to be analyzed at some point in each algorithm
                            Light blue = tile that has been analyzed
                            
                        The tiles of the grid in this visualisation can be thought of
                        as nodes in a graph in which every non-solid tile is connected
                        to 4 or less neighboring tiles, depending on which of them are
                        also non-solid, and the cost/distance/weight of moving from one
                        to another is always 1. So, all edges/neighbors of connected
                        nodes/tiles have the same weight.
                        
                        DFS is unweighted and doesn't guarantee the shortest path.
                        
                        BFS is unweighted, but since all edges have the same weight
                        it does guarantee the shortest path.
                        
                        Greedy Best-First Search is weighted, but doesn't guarantee
                        the shortest path.
                        
                        Dijkstra's Algorithm is weighted and does guarantee the shortest
                        path, but since all edges have the same weight in this particular
                        grid system of nodes, it works essentially the same as BFS.
                        
                        A* Search is weighted and does guarantee the shortest path.""");
            }
        };
    }

}
