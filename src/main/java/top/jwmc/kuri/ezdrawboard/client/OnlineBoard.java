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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OnlineBoard extends Application {

    private Label onlineCountLabel;
    private ListView<String> onlineUserListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("在线白板列表");

        onlineCountLabel = new Label("当前在线人数: 获取中...");
        onlineCountLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #333;");

        onlineUserListView = new ListView<>();

        VBox root = new VBox(20, onlineCountLabel, onlineUserListView);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        startAutoRefresh();

        onlineUserListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 双击
                String selectedUser = onlineUserListView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    showUserWindow();
                }
            }
        });
    }

    private void startAutoRefresh() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> refreshOnlineUserList()),
                new KeyFrame(Duration.seconds(5))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refreshOnlineUserList() {
        List<String> userList = getOnlineUserListFromServer();
        onlineUserListView.getItems().setAll(userList);
        onlineCountLabel.setText("当前在线白板如下: " );
    }

    // 模拟获取在线用户昵称列表
    private List<String> getOnlineUserListFromServer() {
        // TODO: 真实服务器请求替换这里
        String[] sampleUsers = {"Alice", "Bob", "Charlie", "David", "Eve", "Faythe", "Grace"};
        int count = new Random().nextInt(sampleUsers.length) + 1;
        return Arrays.asList(Arrays.copyOf(sampleUsers, count));
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
    public void showUserWindow(){
        //ToDO
    }
}

