package hr.mlinx.actions;

import com.formdev.flatlaf.util.StringUtils;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;

public class KeyBindings implements Serializable {

    @Serial
    // should be changed/incremented manually
    // when the class implementation is changed
    // (so that the load() method returns null if the class of the object being read is of a different version)
    // or when you configure that another (menu item) action should have a key binding (in the MenuBar class)
    // (so that the load() method also returns null since the array fields of this class will be bigger);
    // however, the saved key bindings will be lost in those cases, but whatever, not that important for this program
    // since it's not that serious, just wanted to play around with serialization :p
    private static final long serialVersionUID = 1L;
    private static final String SAVE_FOLDER_NAME = ".pathfinding";
    private static final String SAVE_FILE_NAME = "KeyBindings.ser";

    private final String[] defaultKeys;
    private final String[] inputKeys;
    private final String[] inputDescriptions;
    private final String actionName;

    public KeyBindings(String[] defaultKeys, String[] inputKeys, String[] inputDescriptions, String actionName) throws IllegalArgumentException {
        if (defaultKeys.length == inputKeys.length && defaultKeys.length == inputDescriptions.length) {
            this.defaultKeys = defaultKeys;
            this.inputKeys = inputKeys;
            this.inputDescriptions = inputDescriptions;
            this.actionName = actionName;
        } else {
            throw new IllegalArgumentException("Constructor array fields all need to be of the same length.");
        }
    }

    public void setKey(int index, String key) {
        inputKeys[index] = key;
    }

    public String getKey(int index) {
        return inputKeys[index];
    }

    public String getDescription(int index) {
        return inputDescriptions[index];
    }

    public int count() {
        return inputKeys.length;
    }

    public void setDefaults() {
        int count = count();
        if (count >= 0) System.arraycopy(defaultKeys, 0, inputKeys, 0, count);
    }

    public String[] getCurrentKeys() {
        return Arrays.copyOf(inputKeys,  count());
    }

    public String getActionName(int index) {
        return actionName + index;
    }

    private static String getHome() {
        String home = System.getenv("APPDATA");
        if (StringUtils.isEmpty(home)) {
            home = System.getProperty("user.home");
        }
        return home;
    }

    public static void save(KeyBindings keyBindings) {
        String home = getHome();
        File configHome = new File(home, SAVE_FOLDER_NAME).getAbsoluteFile();
        configHome.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(configHome + "/" + SAVE_FILE_NAME))) {
            oos.writeObject(keyBindings);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Saving failed.");
        }
    }

    public static KeyBindings load() {
        String home = getHome();

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(home + "/" + SAVE_FOLDER_NAME + "/" + SAVE_FILE_NAME))) {
            return (KeyBindings) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // will return null if the file doesn't exist or the class versions are different
            return null;
        }
    }

}
