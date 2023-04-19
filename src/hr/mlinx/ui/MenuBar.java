package hr.mlinx.ui;

import hr.mlinx.actions.KeyBindings;
import hr.mlinx.actions.MenuActions;
import hr.mlinx.algorithms.*;
import hr.mlinx.board.Grid;
import hr.mlinx.util.MazeAlgo;
import hr.mlinx.util.SoundPlayer;

import javax.swing.*;
import java.util.Arrays;

public class MenuBar extends JMenuBar {

    // these JMenuItem's don't have to be fields, they are because they are used in the helper method
    // to not write everything in the constructor itself, but that's not a requirement
    private final JMenuItem runPauseItem, terminateItem;
    private final JMenuItem depthFirstItem, breadthFirstItem, greedyFirstItem, dijkstrasItem, aStarItem;
    private final JMenuItem kruskalsItem, recBackItem;
    private final JMenuItem clearPathItem, clearSolidItem, clearAllItem, toggleSoundItem;
    private final JMenuItem keysItem, defaultsItem, resetGridItem;
    private final JMenuItem mouseItem, infoItem;

    public MenuBar(Grid grid, Canvas canvas, Frame frame, SoundPlayer soundPlayer) {
        super();

        runPauseItem = new JMenuItem("Run/Pause Search");
        terminateItem = new JMenuItem(("Terminate Search"));
        JMenu runMenu = new JMenu("Run");
        runMenu.add(runPauseItem);
        runMenu.add(terminateItem);
        add(runMenu);

        depthFirstItem = new JMenuItem("Depth-First Search");
        breadthFirstItem = new JMenuItem("Breadth-First Search");
        greedyFirstItem = new JMenuItem("Greedy Best-First Search");
        dijkstrasItem = new JMenuItem("Dijkstra's Algorithm");
        aStarItem = new JMenuItem("A* Search");
        kruskalsItem = new JMenuItem("Kruskal's Algorithm");
        recBackItem = new JMenuItem("Recursive Backtracking");
        clearPathItem = new JMenuItem("Clear Path");
        clearSolidItem = new JMenuItem("Clear Solid Tiles");
        clearAllItem = new JMenuItem("Clear All Tiles");
        toggleSoundItem = new JMenuItem("Turn Sound OFF");
        JMenu algorithmsSubMenu = new JMenu("Choose an Algorithm");
        algorithmsSubMenu.add(depthFirstItem);
        algorithmsSubMenu.add(breadthFirstItem);
        algorithmsSubMenu.add(greedyFirstItem);
        algorithmsSubMenu.add(dijkstrasItem);
        algorithmsSubMenu.add(aStarItem);
        JMenu mazeSubMenu = new JMenu("Generate Random Maze");
        mazeSubMenu.add(kruskalsItem);
        mazeSubMenu.add(recBackItem);
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.add(algorithmsSubMenu);
        optionsMenu.add(mazeSubMenu);
        optionsMenu.add(clearPathItem);
        optionsMenu.add(clearSolidItem);
        optionsMenu.add(clearAllItem);
        optionsMenu.add(toggleSoundItem);
        add(optionsMenu);

        JMenu settingsMenu = new JMenu("Settings");
        keysItem = new JMenuItem("Key Bindings");
        defaultsItem = new JMenuItem("Reset Key Bindings");
        resetGridItem = new JMenuItem("Reset Grid");
        settingsMenu.add(keysItem);
        settingsMenu.add(defaultsItem);
        settingsMenu.add(resetGridItem);
        add(settingsMenu);

        JMenu aboutMenu = new JMenu("About");
        mouseItem = new JMenuItem("Mouse Usage");
        infoItem = new JMenuItem("Info on the Algorithms");
        aboutMenu.add(mouseItem);
        aboutMenu.add(infoItem);
        add(aboutMenu);

        addActionListenersAndKeyBindings(grid, canvas, frame, soundPlayer);
    }

    private void addActionListenersAndKeyBindings(Grid grid, Canvas canvas, Frame frame, SoundPlayer soundPlayer) {
        MenuActions menuActions = new MenuActions(grid, canvas, frame);
        KeyBindings keyBindings;
        // load the saved key bindings, if there aren't any then manually set the key bindings;
        // the order of key and description strings as well as number has to match to make sense in the ui,
        // and also for the action map of the component since they will also be in order,
        // i.e. action0, action1...
        // (the component that has the key bindings is Canvas)
        if ((keyBindings = KeyBindings.load()) == null) {
            String[] defaultKeys = new String[] {
                    "SPACE", "C",
                    "W", "S", "A", "T"
            };
            keyBindings = new KeyBindings(defaultKeys, Arrays.copyOf(defaultKeys, defaultKeys.length), new String[] {
                    "Run/pause search", "Terminate search",
                    "Clear path", "Clear solid tiles", "Clear all tiles", "Toggle sound"
            }, "action");
        }

        runPauseItem.addActionListener(menuActions.createRunPauseAction());
        terminateItem.addActionListener(menuActions.createStopAction());

        depthFirstItem.addActionListener(menuActions.createAlgorithmAction(new DepthFirstSearch(grid, canvas, soundPlayer)));
        breadthFirstItem.addActionListener(menuActions.createAlgorithmAction(new BreadthFirstSearch(grid, canvas, soundPlayer)));
        greedyFirstItem.addActionListener(menuActions.createAlgorithmAction(new GreedyBestFirstSearch(grid, canvas, soundPlayer)));
        dijkstrasItem.addActionListener(menuActions.createAlgorithmAction(new DijkstrasAlgorithm(grid, canvas, soundPlayer)));
        aStarItem.addActionListener(menuActions.createAlgorithmAction(new AStarSearch(grid, canvas, soundPlayer)));
        kruskalsItem.addActionListener(menuActions.createRandomMazeAction(MazeAlgo.KRUSKAL));
        recBackItem.addActionListener(menuActions.createRandomMazeAction(MazeAlgo.REC_BACK));
        clearPathItem.addActionListener(menuActions.createClearPathAction());
        clearSolidItem.addActionListener(menuActions.createClearSolidAction());
        clearAllItem.addActionListener(menuActions.createClearAllAction());
        toggleSoundItem.addActionListener(menuActions.createToggleSoundAction(toggleSoundItem, soundPlayer));

        keysItem.addActionListener(menuActions.createKeysAction(keyBindings));
        defaultsItem.addActionListener(menuActions.createDefaultKeysAction(keyBindings));
        resetGridItem.addActionListener(menuActions.createResetGridAction());

        mouseItem.addActionListener(menuActions.createMouseAction());
        infoItem.addActionListener(menuActions.createInfoAction());

        // here, as well, the order the actions are added as well as the number
        // should match the order of key bindings
        Action[] actionsToBind = new Action[] {
                getAction(runPauseItem), getAction(terminateItem),
                getAction(clearPathItem), getAction(clearSolidItem), getAction(clearAllItem), getAction(toggleSoundItem)
        };
        canvas.initialBindKeys(keyBindings, actionsToBind);
    }

    private Action getAction(JMenuItem item) {
        return (Action) item.getActionListeners()[0];
    }

}
