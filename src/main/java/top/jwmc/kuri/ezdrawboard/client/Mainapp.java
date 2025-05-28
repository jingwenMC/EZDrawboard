package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.stage.Stage;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Mainapp extends Application {
    static DataOutputStream out;
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

    public static void main(DataOutputStream out, String[] args) throws IOException {
        new PacketPing().sendPacket(out);
        Mainapp.out = out;
        launch(args);
    }}