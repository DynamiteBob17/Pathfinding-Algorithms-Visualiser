package hr.mlinx.actions;

import hr.mlinx.algorithms.SearchAlgorithm;
import hr.mlinx.board.Grid;
import hr.mlinx.ui.Canvas;
import hr.mlinx.ui.Frame;
import hr.mlinx.ui.KeyBindPanel;
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

    public Action createRandomMazeAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.searchNotRunning()) {
                    grid.clearAll();
                    grid.randomMaze();
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

    public Action createMouseAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, """
                        Left-click to add a solid tile, right-click to remove it.
                        Drag the start/goal tile to change its position.""");
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
                    grid.reset();
                    canvas.repaint();
                }
            }
        };
    }

    public Action createInfoAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, """
                        Depth-First and Breadth-First Search algorithms are unweighted.
                        In this grid visualisation it means they don't consider any distances,
                        such as the number of steps the path took, the distance from the goal, etc.
                        
                        The other three algorithms are weighted and each take into consideration
                        different variables.
                        
                        Out of the algorithms in this program, the following guarantee the shortest path:
                        Breadth-First Search, Dijkstra's Algorithm and A* Search.
                        
                        Note:
                        Since all 'nodes' or the tiles in the grid all have the same weight of 1,
                        meaning the distance/amount of steps it takes to go from one tile to any
                        neighboring/connected tile, that means that with grids like this,
                        Breadth-First Search and Dijkstra's Algorithm are essentially the same.
                        It would be different if, for example, the distance between each node/tile
                        was different.""");
            }
        };
    }

}
