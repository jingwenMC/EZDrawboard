package top.jwmc.kuri.ezdrawboard.client;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class EnhancedDrawingBoard extends Application {
    public enum ToolType { LINE, RECTANGLE, ELLIPSE, FREEHAND, ERASER }

//    public enum EraserMode{
//        LINE("线擦除");
//
//        private final String displayName;
//
//        EraserMode(String displayName) {
//            this.displayName = displayName;
//        }
//        @Override
//        public String toString(){
//            return displayName;
//        }
//    }

    public static class Drawing {
        ToolType type;
        Color color;
        List<Point2D> path;
        double x1, y1, x2, y2;
        int eraserSize;     //橡皮擦大小

        Drawing(ToolType type, Color color, double x1, double y1, double x2, double y2) {
            this.type = type;
            this.color = color;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        Drawing(ToolType type, Color color, List<Point2D> path) {
            this.type = type;
            this.color = color;
            this.path = path;
        }

        //橡皮擦构造方法
        Drawing(ToolType type, Color color, List<Point2D> path, int eraserSize) {
            this.type = type;
            this.color = color;
            this.path = path;
            this.eraserSize = eraserSize;
        }
    }

    private ToolType[] currentTool = {ToolType.LINE};
    private final List<Drawing> drawings = new ArrayList<>();
    private Canvas canvas;
    private Painter painter;
    private MouseHandler mouseHandler;

    // 新增成员变量：背景图像、橡皮擦相关成员变量
    private Image backgroundImage = null;
    private int eraserSize = 5;
    private Slider eraserSizeSlider;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(800, 550);
        painter = new Painter(canvas);
        mouseHandler = new MouseHandler(drawings, currentTool, painter, this);

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> painter.setCurrentColor(colorPicker.getValue()));

        ToggleGroup group = new ToggleGroup();
        RadioButton line = createToolButton("线条", ToolType.LINE, group);
        RadioButton rect = createToolButton("矩形", ToolType.RECTANGLE, group);
        RadioButton ellipse = createToolButton("椭圆", ToolType.ELLIPSE, group);
        RadioButton freehand = createToolButton("自由绘制", ToolType.FREEHAND, group);
        RadioButton eraser = createToolButton("橡皮擦", ToolType.ERASER, group);
        line.setSelected(true);

        Button clearButton = new Button("清除");
        clearButton.setOnAction(e -> {
            drawings.clear(); // 清空所有绘制的图形
            painter.clearBackgroundImage(); // 移除背景并恢复白色画布
        });

        // 新增保存和读取PNG按钮
        Button saveButton = new Button("保存背景PNG");
        Button loadButton = new Button("读取PNG背景");
        Button talkButton=new Button("聊天");
        talkButton.setOnAction(event -> {
            if(!Mainapp.ONLINE_MODE){
                showAlert("提示", "已选择本地模式", "无法使用该功能");
            }
            else{
            Talk talk = new Talk();
            talk.showChatWindow();}
        });

        saveButton.setOnAction(e -> saveCanvasToPNG(primaryStage));
        loadButton.setOnAction(e -> loadBackgroundFromPNG(primaryStage));

        //橡皮擦模式控件
        VBox eraserControls = new VBox(5);
        eraserControls.setPadding(new Insets(5));
        eraserControls.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5;");

//        //橡皮擦模式选择
//        ToggleGroup eraserModeGroup = new ToggleGroup();
//        RadioButton pixelMode = new RadioButton(EraserMode.PIXEL.toString());
//        pixelMode.setToggleGroup(eraserModeGroup);
//        pixelMode.setSelected(true);
//        pixelMode.setOnAction(e -> currentEraserMode = EraserMode.PIXEL);
//        RadioButton lineMode = new RadioButton(EraserMode.LINE.toString());
//        lineMode.setToggleGroup(eraserModeGroup);
//        lineMode.setOnAction(e -> currentEraserMode = EraserMode.LINE);
//
//        HBox eraserModeBox = new HBox(10, pixelMode, lineMode);

        //橡皮擦大小滑块
        Label sizeLabel = new Label("橡皮擦大小：");
        eraserSizeSlider = new Slider(1, 50, eraserSize);
        eraserSizeSlider.setShowTickLabels(true);
        eraserSizeSlider.setShowTickMarks(true);
        eraserSizeSlider.setMajorTickUnit(10);
        eraserSizeSlider.setMinorTickCount(5);
        eraserSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            eraserSize = newVal.intValue();
        });

        eraserControls.getChildren().addAll(sizeLabel, eraserSizeSlider);
        eraserControls.visibleProperty().bind(eraser.selectedProperty());
        eraserControls.managedProperty().bind(eraser.selectedProperty());


        HBox toolbar = new HBox(5, colorPicker, line, rect, ellipse, freehand, eraser, clearButton, saveButton, loadButton,talkButton);
        BorderPane root = new BorderPane();
        VBox topContainer = new VBox(toolbar, eraserControls);
        root.setTop(topContainer);
        root.setCenter(canvas);

        canvas.setOnMousePressed(mouseHandler::onMousePressed);
        canvas.setOnMouseDragged(mouseHandler::onMouseDragged);
        canvas.setOnMouseReleased(mouseHandler::onMouseReleased);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("增强画板");
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.show();
    }

    private RadioButton createToolButton(String text, ToolType tool, ToggleGroup group) {
        RadioButton button = new RadioButton(text);
        button.setToggleGroup(group);
        button.setOnAction(e -> currentTool[0] = tool);
        return button;
    }


    public int getEraserSize(){
        return eraserSize;
    }

    // 新增：保存当前画布为PNG文件（包含当前绘制内容）
    private void saveCanvasToPNG(Stage stage) {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存PNG文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG文件", "*.png"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                BufferedImage bufferedImage = writableImageToBufferedImage(writableImage);
                ImageIO.write(bufferedImage, "png", file);
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "保存失败: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    // 新增：读取PNG作为背景图片，并显示在画布上
    private void loadBackgroundFromPNG(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择PNG背景图片");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG文件", "*.png"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                // 使用Image的公共构造函数加载图像
                backgroundImage = new Image(file.toURI().toString());
                painter.setBackgroundImage(backgroundImage);
                painter.redrawAll(drawings);
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "加载失败: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    // 新增：绘制背景图像到画布
    private void redrawWithBackground() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        }
        // 不自动绘制已有drawings，绘图逻辑由Painter和MouseHandler控制，保持原样
    }

    // 新增：WritableImage转BufferedImage，绕过SwingFXUtils
    private BufferedImage writableImageToBufferedImage(WritableImage writableImage) {
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PixelReader pixelReader = writableImage.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color fxColor = pixelReader.getColor(x, y);
                int argb = ((int)(fxColor.getOpacity()*255) << 24) |
                        ((int)(fxColor.getRed()*255) << 16) |
                        ((int)(fxColor.getGreen()*255) << 8) |
                        ((int)(fxColor.getBlue()*255));
                bufferedImage.setRGB(x, y, argb);
            }
        }
        return bufferedImage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


