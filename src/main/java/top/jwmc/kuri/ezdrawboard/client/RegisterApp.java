package top.jwmc.kuri.ezdrawboard.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("注册");

        // 创建表单布局
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // 表单字段
        Label userNameLabel = new Label("用户名:");
        TextField userNameField = new TextField();
        grid.add(userNameLabel, 0, 0);
        grid.add(userNameField, 1, 0);

        Label passwordLabel = new Label("密码:");
        PasswordField passwordField = new PasswordField();
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        Label confirmLabel = new Label("确认密码:");
        PasswordField confirmPasswordField = new PasswordField();
        grid.add(confirmLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        Button registerBtn = new Button("注册");
        Label resultLabel = new Label();
        grid.add(registerBtn, 1, 3);
        grid.add(resultLabel, 1, 4);

        // 注册按钮逻辑
        registerBtn.setOnAction(event -> {
            String username = userNameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                resultLabel.setText("所有字段不能为空！");
                resultLabel.setStyle("-fx-text-fill: red;");
            } else if (!password.equals(confirmPassword)) {
                resultLabel.setText("两次密码不一致！");
                resultLabel.setStyle("-fx-text-fill: red;");
            } else {
                // TODO: 实际注册逻辑（如写入数据库、文件等）
                resultLabel.setText("注册成功！");
                resultLabel.setStyle("-fx-text-fill: green;");
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 启动应用
    public static void main(String[] args) {
        launch(args);
    }
}
