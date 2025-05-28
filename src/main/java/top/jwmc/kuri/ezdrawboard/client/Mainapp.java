package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.stage.Stage;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.IOException;
import java.net.Socket;

public class Mainapp extends Application {
    static Socket socket;
    @Override
    public void start(Stage primaryStage) throws IOException {
        // 启动 Login 窗口
        Login loginApp = new Login();
        new PacketPing().sendPacket(socket);
        try {
            loginApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(Socket socket, String[] args) {
        Mainapp.socket = socket;
        launch(args);
    }}