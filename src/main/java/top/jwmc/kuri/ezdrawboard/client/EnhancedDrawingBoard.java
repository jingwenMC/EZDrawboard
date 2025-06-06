package top.jwmc.kuri.ezdrawboard.client;
import javafx.application.Application;
import javafx.application.Platform;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.stage.FileChooser;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketDrawClear;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketImageDeliver;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketImageRequest;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class EnhancedDrawingBoard extends Application {
    private static Stage PRIMARY_STAGE;
    private static EnhancedDrawingBoard INSTANCE;
    public enum ToolType { LINE, RECTANGLE, ELLIPSE, FREEHAND, ERASER }

    public static class Drawing {
        public ToolType type;
        public Color color;
        public List<Point2D> path;
        public double x1, y1, x2, y2;
        public double brushSize;     //橡皮擦大小

        Drawing(ToolType type, Color color, double x1, double y1, double x2, double y2, double brushSize) {
            this.type = type;
            this.color = color;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.brushSize = brushSize;
        }
        public Drawing(ToolType type, Color color,List<Point2D> path, double x1, double y1, double x2, double y2, double brushSize) {
            this.type = type;
            this.color = color;
            this.path = path;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.brushSize = brushSize;
        }
        Drawing(ToolType type, Color color, List<Point2D> path, double brushSize) {
            this.type = type;
            this.color = color;
            this.path = path;
            this.brushSize = brushSize;
        }
    }

    private ToolType[] currentTool = {ToolType.LINE};
    private final List<Drawing> drawings = new DrawingList();
    private Canvas canvas;
    private Painter painter;
    private MouseHandler mouseHandler;

    private Image backgroundImage = null;
    private Slider sizeSlider;

    @Override
    public void start(Stage primaryStage) throws IOException {
        INSTANCE = this;
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
            drawings.clear();
            painter.clearBackgroundImage();
            if(Mainapp.ONLINE_MODE) {
                try {
                    new PacketDrawClear(null).sendPacket(Mainapp.out);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


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

        PRIMARY_STAGE = primaryStage;
        saveButton.setOnAction(e -> saveCanvasToPNG(primaryStage));
        loadButton.setOnAction(e -> loadBackgroundFromPNG(primaryStage));


        VBox brushSizeControls = new VBox(5);
        brushSizeControls.setPadding(new Insets(5));
        brushSizeControls.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5;");



        Label sizeLabel = new Label("粗细调节：");
        sizeSlider = new Slider(1, 50, 5);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setMajorTickUnit(10);
        sizeSlider.setMinorTickCount(5);
        brushSizeControls.getChildren().addAll(sizeLabel, sizeSlider);


        HBox toolbar = new HBox(5, colorPicker, line, rect, ellipse, freehand, eraser, clearButton, saveButton, loadButton,talkButton);
        BorderPane root = new BorderPane();
        VBox topContainer = new VBox(toolbar, brushSizeControls);
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
        if(Mainapp.ONLINE_MODE)new PacketImageRequest(Mainapp.user.name()).sendPacket(Mainapp.out);
    }

    private RadioButton createToolButton(String text, ToolType tool, ToggleGroup group) {
        RadioButton button = new RadioButton(text);
        button.setToggleGroup(group);
        button.setOnAction(e -> currentTool[0] = tool);
        return button;
    }


    public int getBrushSize(){
        return (int) Math.round(sizeSlider.getValue());
    }
    private void saveCanvasToPNG(Stage stage) {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存PNG文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG文件", "*.png"));
        fileChooser.setInitialFileName("mypaint.png");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
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
    public static void saveCanvas() {
        Platform.runLater(()-> {
            INSTANCE.saveCanvasToPNGOnline(PRIMARY_STAGE);
            PacketImageRequest.UPDATED = true;
        });
    }
    private void saveCanvasToPNGOnline(Stage stage) {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);

        File file = new File(Util.FILE_NAME);

        try {
            BufferedImage bufferedImage = writableImageToBufferedImage(writableImage);
            ImageIO.write(bufferedImage, "png", file);
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "保存失败: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void loadBackgroundFromPNG(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择PNG背景图片");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG文件", "*.png"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                backgroundImage = new Image(file.toURI().toString());
                painter.setBackgroundImage(backgroundImage);
                painter.redrawAll(drawings);
                if(Mainapp.ONLINE_MODE)new PacketImageDeliver(Util.fileToByte(file)).sendPacket(Mainapp.out);
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "加载失败: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }
    public static void loadCanvas() {
        File file = new File(Util.FILE_NAME);
        while (!file.exists()) {
            Thread.onSpinWait();
        }
        Platform.runLater(()->INSTANCE.loadBackgroundFromPNGOnline(PRIMARY_STAGE));
    }
    private void loadBackgroundFromPNGOnline(Stage stage) {
        File file = new File(Util.FILE_NAME);

        if (file.exists()) {
            try {
                backgroundImage = new Image(file.toURI().toString());
                painter.setBackgroundImage(backgroundImage);
                painter.redrawAll(drawings);
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "加载失败: " + ex.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "未找到背景文件: " + file.getAbsolutePath());
            alert.showAndWait();
        }
    }

    private void redrawWithBackground() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

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

    public static boolean preparing() {
        return INSTANCE == null;
    }
}


