package hr.mlinx.util;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Util {

    public static final double SCALE = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1920.0;

    public static void configureUI() {
        float fontSize = 18f * (float) SCALE;

        UIManager.put("MenuBar.font", ((Font) UIManager.get("MenuBar.font")).deriveFont(fontSize));
        UIManager.put("Menu.font", ((Font) UIManager.get("Menu.font")).deriveFont(fontSize));
        UIManager.put("MenuItem.font", ((Font) UIManager.get("MenuItem.font")).deriveFont(fontSize));
        UIManager.put("Label.font", ((Font) UIManager.get("Label.font")).deriveFont(Font.PLAIN).deriveFont(fontSize * 1.2f));

        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static double map(double val, double valLow, double valHigh, double returnValLow, double returnValHigh) {
        double ratio = (val - valLow) / (valHigh - valLow);

        return ratio * (returnValHigh - returnValLow) + returnValLow;
    }

    public static boolean isUnix() {
        return System.getProperty("os.name").startsWith("Linux");
    }

}
