package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Talk extends Application {

    private Label titleLabel;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ScrollPane chatScroll = new ScrollPane();
    private VBox chatContent = new VBox(5); // 消息容器
    private TextField inputField;
    private String username = "用户" + (int)(Math.random() * 1000); // 自动生成用户名

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // ===== 界面布局 =====
        VBox root = new VBox(10);
        root.getStyleClass().add("chat-container");//根容器
        root.setPadding(new Insets(15));

        titleLabel = new Label("聊天 - " + username);
        titleLabel.getStyleClass().add("chat-title");

        chatScroll.setContent(chatContent);
        chatScroll.setFitToWidth(true);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.getStyleClass().add("chat-scroll");
        chatContent.setFillWidth(true);
        chatContent.getStyleClass().add("chat-content");

        inputField = new TextField();
        inputField.getStyleClass().add("input-field");  //输入框
        inputField.setPromptText("输入消息...");
        inputField.setOnAction(e -> sendMessage());

        Button sendButton = new Button("发送");
        sendButton.getStyleClass().addAll("button", "send-button");  //发送按钮
        sendButton.setOnAction(e -> sendMessage());

        Button onlineButton = new Button("查看在线好友");
        onlineButton.setOnAction(e -> requestOnlineUsers());

        //输入布局
        HBox inputBox = new HBox(10, inputField, sendButton, onlineButton);
        inputBox.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(inputField, Priority.ALWAYS);

        root.getChildren().addAll(titleLabel, chatScroll, inputBox);

        //加载CSS
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("css/Talk.css").toExternalForm());
        primaryStage.setTitle("聊天室");
        primaryStage.setScene(scene);
        primaryStage.show();

        // ===== 启动连接线程 =====
        connectToServer();
    }

    private void createMessageBubble(String time, String user, String content, boolean isCurrentUser) {
        HBox messageBubble = new HBox();
        messageBubble.getStyleClass().addAll("message-bubble",
                isCurrentUser ? "my-message" : "other-message");

        // 时间标签
        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().add("timestamp");

        // 用户标签
        Label userLabel = new Label(user + ":");
        userLabel.getStyleClass().add("username");

        // 内容标签
        Label contentLabel = new Label(content);
        contentLabel.getStyleClass().add("message-content");
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(300);

        VBox messageBox = new VBox(5, timeLabel, new HBox(5, userLabel, contentLabel));
        messageBubble.getChildren().add(messageBox);

        // 设置对齐方式
        HBox.setHgrow(messageBox, Priority.ALWAYS);
        messageBubble.setAlignment(isCurrentUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        chatContent.getChildren().add(messageBubble);
        chatScroll.setVvalue(1.0);
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
                        String[] parts = msg.split("\\|"); //服务器发送格式：时间|用户|内容
                        if (parts.length == 3) {
                            boolean isCurrentUser = parts[1].equals(username);
                            String finalMsg = msg;
                            Platform.runLater(() -> {
                                // 使用前面提到的消息气泡创建逻辑
                                createMessageBubble(parts[0], parts[1], parts[2], isCurrentUser);
                            });
                        }
                    }
                } catch (IOException e) {
                    Platform.runLater(() ->  createMessageBubble(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                            "系统",
                            "服务器连接已断开",
                            false
                    ));
                }
            }).start();

            // 通知服务器新用户上线
            out.println("[上线] " + username);

        } catch (IOException e) {
            Platform.runLater(() -> createMessageBubble(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    "系统",
                    "无法连接到服务器：" + e.getMessage(),
                    false
            ));
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty() && out != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String formattedMsg = String.format("%s|%s|%s", timestamp, username, message);
            out.println(formattedMsg);
            inputField.clear();
        }
    }

    private void requestOnlineUsers() {
        if (out != null) {
            out.println("[请求在线用户]");
            //TODO:发送一个特殊请求，服务器可识别并返回在线用户列表（需配合服务器实现）
        }
    }

}