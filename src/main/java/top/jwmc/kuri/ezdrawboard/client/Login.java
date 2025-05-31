package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketInLogin;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;
import top.jwmc.kuri.ezdrawboard.server.Util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Login extends Application {
    public static volatile boolean UPDATED = false;
    public static boolean RESULT = false;
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userLabel = new Label("用户名:");
        TextField userTextField = new TextField();
        Label pwLabel = new Label("密码:");
        PasswordField pwField = new PasswordField();
        Button loginBtn = new Button("登录");
        Button regst=new Button("注册");
        regst.setOnAction(event -> {
            openrgstWindow();
        });
        grid.add(userLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(pwLabel, 0, 1);
        grid.add(pwField, 1, 1);
        grid.add(loginBtn, 1, 2);
        grid.add(regst,1,3);



        loginBtn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwField.getText();
            try {
                UPDATED = false;
                RESULT = false;
                new PacketInLogin(username, Util.getSHA256Str(password,username)).sendPacket(Mainapp.out);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // 简单的验证逻辑
            if (isValidLogin()) {
                showAlert(Alert.AlertType.INFORMATION, "登录成功", "欢迎 " + username + "!");

                // 启动 Choose 窗口
                openprintWindow();

                // 关闭当前登录窗口
                primaryStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "登录失败", "用户名或密码错误");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setTitle("登录系统");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.show();
    }


    private void openprintWindow() {
        try {
            OnlineBoard printApp = new OnlineBoard();
            Stage printStage = new Stage();
            printApp.start(printStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证登录信息
     */
    private boolean isValidLogin() {
        while (!UPDATED) {
            Thread.onSpinWait();
        }
        return RESULT;
    }

    private void openrgstWindow() {
        try {
            RegisterApp registerApp=new RegisterApp();
            Stage registstage=new Stage();
            registerApp.start(registstage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示提示框
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void boot(String ip, int port, Stage mainStage) {
        Mainapp.networkThread = new Thread(()->{
            try (Socket socket = new Socket(ip, port)) {
                    Router router = new ClientRouterImpl(socket);
                    new PacketPing().sendPacket(router.getDataOutputStream());
                    Mainapp.out = router.getDataOutputStream();
                    Mainapp.ONLINE_MODE = true;
                    router.startHandleRequest();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("网络错误");
                        alert.setContentText("与服务器的连接丢失，现在退出程序……");
                        alert.showAndWait();
                        System.exit(0);
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        Mainapp.networkThread.start();
        Platform.runLater(()->start(mainStage));
    }
}
