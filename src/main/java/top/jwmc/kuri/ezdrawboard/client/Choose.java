package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Choose extends Application {

    public interface ModeSelectionListener {
        void onModeSelected(String mode);
    }

    private ModeSelectionListener listener;

    public void setModeSelectionListener(ModeSelectionListener listener) {
        this.listener = listener;
    }

    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Button localBtn = createModeButton("本地模式");
        Button onlineBtn = createModeButton("在线模式");

        localBtn.setOnAction(e -> {
            handleLocalMode();
            primaryStage.close();
        });

        onlineBtn.setOnAction(e -> {
            handleOnlineMode();
            primaryStage.close();
        });


        root.getChildren().addAll(localBtn, onlineBtn);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("模式选择");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Button createModeButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(150);
        btn.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        return btn;
    }

    private void handleLocalMode() {
        showAlert("模式选择", "已选择本地模式", "将使用本地存储数据\n功能受限版本");
        if (listener != null) {
            listener.onModeSelected("local");  // 通知监听者已选择本地模式
        }
        openprintWindow();
    }


    private void handleOnlineMode() {
        showAlert("模式选择", "已选择在线模式", "需要连接服务器\n需要验证网络...");
        openloginWindow();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void openprintWindow() {
        try {
            // 创建新的 Stage 对象并启动 Choose 窗口
            EnhancedDrawingBoard printApp = new EnhancedDrawingBoard();
            Stage printStage = new Stage();
            printApp.start(printStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openloginWindow() {
        try {
            Login loginapp=new Login();
            Stage loginStage = new Stage();
            loginapp.boot(ClientBootstrap.IP,ClientBootstrap.PORT,loginStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
