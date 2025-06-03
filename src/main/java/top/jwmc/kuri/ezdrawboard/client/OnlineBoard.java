package top.jwmc.kuri.ezdrawboard.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import top.jwmc.kuri.ezdrawboard.data.User;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketInJoin;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketInList;

import java.io.IOException;
import java.util.*;

public class OnlineBoard extends Application {

    private Label onlineCountLabel;
    private ListView<String> onlineUserListView;
    public static final String NO_BOARD = "当前没有在线白板！";
    public static final String CREATE_BOARD = "+ > 创建白板";
    public static final String FETCH_BOARD = "正在获取……";
    public static List<String> userList = new ArrayList<>(List.of(FETCH_BOARD));
    public static volatile boolean UPDATED = false;
    public static boolean RESULT = false;
    public static String MESSAGE = "";
    private Stage primaryStage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("在线白板列表");
        this.primaryStage = primaryStage;
        onlineCountLabel = new Label("当前在线人数: 获取中...");
        onlineCountLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #333;");

        onlineUserListView = new ListView<>();

        VBox root = new VBox(20, onlineCountLabel, onlineUserListView);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        startAutoRefresh();

        onlineUserListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 双击
                String selectedUser = onlineUserListView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    showUserWindow(selectedUser);
                }
            }
        });
    }

    private void startAutoRefresh() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    try {
                        refreshOnlineUserList();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }),
                new KeyFrame(Duration.seconds(5))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refreshOnlineUserList() throws IOException {
        List<String> userList = getOnlineUserListFromServer();
        if(userList.isEmpty()) {
            userList.add(NO_BOARD);
        }
        userList.add(CREATE_BOARD);
        onlineUserListView.getItems().setAll(userList);
        onlineCountLabel.setText("当前在线白板如下: " );
    }

    // 模拟获取在线用户昵称列表
    private List<String> getOnlineUserListFromServer() throws IOException {
        new PacketInList(null).sendPacket(Mainapp.out);
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
    public void showUserWindow(String selectedUser) {
        if(selectedUser.equals(FETCH_BOARD) || selectedUser.equals(NO_BOARD)) return;
        PacketInJoin packetInJoin = new PacketInJoin(selectedUser.equals(CREATE_BOARD)?Mainapp.user.name():selectedUser);
        try {
            UPDATED = false;
            MESSAGE = "";
            RESULT = false;
            packetInJoin.sendPacket(Mainapp.out);
            while (!UPDATED) {
                Thread.onSpinWait();
            }
            if(RESULT) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(MESSAGE);
                alert.showAndWait();
                openPrintWindow();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(MESSAGE);
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPrintWindow() {
        try {
            // 创建新的 Stage 对象并启动 Choose 窗口
            EnhancedDrawingBoard printApp = new EnhancedDrawingBoard();
            Stage printStage = new Stage();
            printApp.start(printStage);
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

