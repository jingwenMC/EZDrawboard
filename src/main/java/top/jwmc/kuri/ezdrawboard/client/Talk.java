package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import java.net.Socket;

public class Talk extends Application {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private TextArea chatArea;
    private TextField inputField;
    private String username = "用户" + (int)(Math.random() * 1000); // 自动生成用户名

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // ===== 界面布局 =====
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("输入消息...");
        inputField.setOnAction(e -> sendMessage());

        Button sendButton = new Button("发送");
        sendButton.setOnAction(e -> sendMessage());

        Button onlineButton = new Button("查看在线好友");
        onlineButton.setOnAction(e -> requestOnlineUsers());

        HBox inputBox = new HBox(10, inputField, sendButton, onlineButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        root.getChildren().addAll(new Label("群组聊天"), chatArea, inputBox);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("聊天室 - " + username);
        primaryStage.setScene(scene);
        primaryStage.show();

        // ===== 启动连接线程 =====
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 接收线程
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        String finalMsg = msg;
                        Platform.runLater(() -> chatArea.appendText(finalMsg + "\n"));
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> chatArea.appendText("服务器连接断开。\n"));
                }
            }).start();

            // 通知服务器新用户上线（可用于扩展在线列表）
            out.println("[上线] " + username);

        } catch (IOException e) {
            chatArea.appendText("无法连接到服务器。\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty() && out != null) {
            out.println(username + ": " + message);
            inputField.clear();
        }
    }

    private void requestOnlineUsers() {
        // 发送一个特殊请求，服务器可识别并返回在线用户列表（需配合服务器实现）
        if (out != null) {
            out.println("[请求在线用户]");
        }
    }
}
