package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Login extends Application {

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

            // 简单的验证逻辑
            if (isValidLogin(username, password)) {
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

    private void openrgstWindow() {
        try {
            RegisterApp registerApp=new RegisterApp();
            Stage registstage=new Stage();
            registerApp.start(registstage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isValidLogin(String username, String password) {
        return "admin".equals(username) && "123456".equals(password);
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
}
