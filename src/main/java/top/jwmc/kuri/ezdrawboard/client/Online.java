package top.jwmc.kuri.ezdrawboard.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketInOnline;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketOutOnline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Online extends Application {

    private Label onlineCountLabel;
    private ListView<String> onlineUserListView;
    public static List<String> userList = new ArrayList<>();
    public static volatile boolean UPDATED = false;
    Timeline timeline;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("在线用户列表");

        onlineCountLabel = new Label("当前在线人数: 获取中...");
        onlineCountLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #333;");

        onlineUserListView = new ListView<>();

        VBox root = new VBox(20, onlineCountLabel, onlineUserListView);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            timeline.stop();
        });
        primaryStage.show();

        startAutoRefresh();
    }

    private void startAutoRefresh() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> refreshOnlineUserList()),
                new KeyFrame(Duration.seconds(5))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refreshOnlineUserList() {
        List<String> userList = getOnlineUserListFromServer();
        onlineUserListView.getItems().setAll(userList);
        onlineCountLabel.setText("当前在线人数: " + userList.size());
    }

    // 模拟获取在线用户昵称列表
    private List<String> getOnlineUserListFromServer() {
        UPDATED = false;
        try {
            new PacketInOnline(null).sendPacket(Mainapp.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!UPDATED) {
            Thread.onSpinWait();
        }
        return userList;
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

