package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import top.jwmc.kuri.ezdrawboard.data.Message;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketChat;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;


public class Talk extends Application {

    private Label titleLabel;
    private ScrollPane chatScroll = new ScrollPane();
    private VBox chatContent = new VBox(5); // 消息容器
    private TextField inputField;
    private final String username = "用户" + Mainapp.user.name(); // 自动生成用户名
    public static final LinkedList<Message> chatMessages = new LinkedList<>();
    public static Talk INSTANCE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        INSTANCE = this;
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
        Button onlineButton=new Button("在线好友");
        onlineButton.getStyleClass().addAll("button", "send-button");
        onlineButton.setOnAction(event -> {
            Online online = new Online();
            online.showChatWindow();

        });

        sendButton.getStyleClass().addAll("button", "send-button");  //发送按钮
        sendButton.setOnAction(e -> sendMessage());

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

        //Sync
        for(Message message : chatMessages) {
            Platform.runLater(()->createMessageBubble(message.time(),message.user(),message.content(),message.user().equals(Mainapp.user.name())));
        }
    }

    public void createMessageBubble(String time, String user, String content, boolean isCurrentUser) {
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

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            try {
                new PacketChat(new Message(timestamp,Mainapp.user.name(),message)).sendPacket(Mainapp.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inputField.clear();
        }
    }

    private void requestOnlineUsers() {
        //TODO
    }

    public void showChatWindow() {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}