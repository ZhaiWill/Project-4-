import javax.swing.*;

public class output {
    private static boolean debugMode = false;

    public static void print(String message) {
//        System.out.println(message);
        JOptionPane.showMessageDialog(null, message);
    }

    public static void debugPrint(String message) {
        if (debugMode) System.out.println("\u001B[31mDEBUG: \u001B[0m" + message);
    }
}
