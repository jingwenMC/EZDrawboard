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

public class Mainapp extends Application {
    public static boolean ONLINE_MODE = false;
    public static DataOutputStream out;
    public static Painter painter;
    public static User user;
    @Override
    public void start(Stage primaryStage) throws IOException {
        // 启动 Login 窗口
        Login loginApp = new Login();
        new PacketPing().sendPacket(out);
        try {
            loginApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void boot(String ip, int port, Stage mainStage) throws IOException {
        try (Socket socket = new Socket(ip, port)) {
            Router router = new ClientRouterImpl(socket);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new Thread(()-> {
                try {
                    new PacketPing().sendPacket(out);
                    Mainapp.out = out;
                    ONLINE_MODE = true;
                    if(!socket.isClosed())socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            try {
                router.startHandleRequest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}