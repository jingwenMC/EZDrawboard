package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.stage.Stage;
public class Mainapp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 启动 Login 窗口
        Login loginApp = new Login();
        try {
            loginApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }}