package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.stage.Stage;
import top.jwmc.kuri.ezdrawboard.data.User;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.runtime.TemplateRuntime;
import java.net.Socket;

public class Mainapp {
    public static boolean ONLINE_MODE = false;
    public static DataOutputStream out;
    public static Painter painter;
    public static User user;
    public static Thread networkThread;
}