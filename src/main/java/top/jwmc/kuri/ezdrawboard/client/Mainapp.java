package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.data.User;

import java.io.DataOutputStream;

public class Mainapp {
    public static boolean ONLINE_MODE = false;
    public static DataOutputStream out;
    public static User user;
    public static Thread networkThread;
    public static EnhancedDrawingBoard drawingBoard;
}