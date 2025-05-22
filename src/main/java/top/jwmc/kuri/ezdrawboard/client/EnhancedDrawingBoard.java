package top.jwmc.kuri.ezdrawboard.client;/*import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EnhancedDrawingBoard extends Application {
    private enum ToolType { LINE, RECTANGLE, ELLIPSE, FREEHAND, ERASER ,}
    private double eraserWidth = 20;
    private double eraserHeight = 20;
    private Canvas canvas;
    private GraphicsContext gc;
    private Color currentColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;
    private ToolType currentTool = ToolType.LINE;
    private double startX, startY;
    private List<Point2D> currentPath = new ArrayList<>();
    private List<Drawing> drawings = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ColorPicker colorPicker = new ColorPicker(currentColor);
        ToggleGroup toolGroup = new ToggleGroup();

        RadioButton lineButton = createToolButton("线条", ToolType.LINE, toolGroup);
        RadioButton rectButton = createToolButton("矩形", ToolType.RECTANGLE, toolGroup);
        RadioButton ellipseButton = createToolButton("椭圆", ToolType.ELLIPSE, toolGroup);
        RadioButton freehandButton = createToolButton("自由绘制", ToolType.FREEHAND, toolGroup);
        RadioButton eraserButton = createToolButton("橡皮擦", ToolType.ERASER, toolGroup);
        lineButton.setSelected(true);
        Button clearButton = new Button("清除");
        Button talkButton = new Button("聊天");
        clearButton.setOnAction(e -> {
            drawings.clear();
            clearCanvas();
        });

        HBox toolbar = new HBox(10, colorPicker, lineButton, rectButton, ellipseButton, freehandButton, eraserButton, clearButton,talkButton);
        toolbar.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        canvas = new Canvas(800, 550);
        gc = canvas.getGraphicsContext2D();
        initCanvas();

        colorPicker.setOnAction(e -> currentColor = colorPicker.getValue());

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(canvas);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("增强版画板");
        primaryStage.show();
    }

    private void initCanvas() {
        gc.setLineWidth(1);
        clearCanvas();
    }

    private RadioButton createToolButton(String text, ToolType type, ToggleGroup group) {
        RadioButton button = new RadioButton(text);
        button.setToggleGroup(group);
        button.setOnAction(e -> currentTool = type);
        return button;
    }

    private void handleMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();
        currentPath.clear();
        currentPath.add(new Point2D(startX, startY));
    }

    private void handleMouseDragged(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();

        if (currentTool == ToolType.ERASER) {
            currentPath.add(new Point2D(endX, endY));
            redrawAll();

            // 用背景色绘制橡皮擦路径临时线条
            gc.setStroke(backgroundColor);
            gc.setLineWidth(eraserWidth);  // 使用橡皮擦宽度作为线宽
            drawFreehandPath(currentPath);
            return;
        }

        if (currentTool == ToolType.FREEHAND) {
            currentPath.add(new Point2D(endX, endY));
            redrawAll();
            gc.setStroke(currentColor);
            gc.setLineWidth(1);
            drawFreehandPath(currentPath);
        } else {
            drawTempShape(endX, endY);
        }
    }




    private void handleMouseReleased(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();

        if (currentTool == ToolType.ERASER) {
            drawings.add(new Drawing(currentTool, backgroundColor, new ArrayList<>(currentPath)));
            currentPath.clear();
            redrawAll();
            return;
        }

        if (currentTool == ToolType.FREEHAND) {
            drawings.add(new Drawing(currentTool, currentColor, new ArrayList<>(currentPath)));
        } else {
            drawings.add(new Drawing(currentTool, currentColor, startX, startY, endX, endY));
        }
        currentPath.clear();
        redrawAll();
    }


    private void drawFreehandPath(List<Point2D> path) {
        if (path == null || path.size() < 2) return;

        gc.beginPath();
        Point2D prev = path.get(0);
        gc.moveTo(prev.getX(), prev.getY());

        for (int i = 1; i < path.size(); i++) {
            Point2D point = path.get(i);
            gc.lineTo(point.getX(), point.getY());
        }
        gc.stroke();
        gc.closePath();
    }


    private void drawTempShape(double endX, double endY) {
        clearCanvas();
        redrawAll();
        gc.setStroke(currentColor);
        gc.setLineWidth(1);

        switch (currentTool) {
            case LINE:
                gc.strokeLine(startX, startY, endX, endY);
                break;
            case RECTANGLE:
                drawRectangle(startX, startY, endX, endY);
                break;
            case ELLIPSE:
                drawEllipse(startX, startY, endX, endY);
                break;
        }
    }

    private void redrawAll() {
        clearCanvas();
        for (Drawing drawing : drawings) {
            gc.setStroke(drawing.color);
            if (drawing.type == ToolType.FREEHAND || drawing.type == ToolType.ERASER) {
                gc.setLineWidth(drawing.type == ToolType.ERASER ? eraserWidth : 1);
                drawFreehandPath(drawing.path);
            } else {
                gc.setLineWidth(1);
                switch (drawing.type) {
                    case LINE:
                        gc.strokeLine(drawing.x1, drawing.y1, drawing.x2, drawing.y2);
                        break;
                    case RECTANGLE:
                        drawRectangle(drawing.x1, drawing.y1, drawing.x2, drawing.y2);
                        break;
                    case ELLIPSE:
                        drawEllipse(drawing.x1, drawing.y1, drawing.x2, drawing.y2);
                        break;
                }
            }
        }
    }


    private void clearCanvas() {
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawRectangle(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        gc.strokeRect(x, y, width, height);
    }

    private void drawEllipse(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        gc.strokeOval(x, y, w, h);
    }

    class Drawing {
        ToolType type;
        Color color;
        List<Point2D> path;
        double x1, y1, x2, y2;

        // 形状构造器
        Drawing(ToolType type, Color color, double x1, double y1, double x2, double y2) {
            this.type = type;
            this.color = color;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.path = null;
        }

        // 路径构造器（自由绘制和橡皮擦）
        Drawing(ToolType type, Color color, List<Point2D> path) {
            this.type = type;
            this.color = color;
            this.path = path;
        }
    }

}"""*/

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EnhancedDrawingBoard extends Application {
    public enum ToolType { LINE, RECTANGLE, ELLIPSE, FREEHAND, ERASER }

    public static class Drawing {
        ToolType type;
        Color color;
        List<Point2D> path;
        double x1, y1, x2, y2;

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
    }

    private ToolType[] currentTool = {ToolType.LINE};
    private final List<Drawing> drawings = new ArrayList<>();
    private Canvas canvas;
    private Painter painter;
    private MouseHandler mouseHandler;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(800, 550);
        painter = new Painter(canvas);
        mouseHandler = new MouseHandler(drawings, currentTool, painter);

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
            painter.clearCanvas();
        });

        HBox toolbar = new HBox(10, colorPicker, line, rect, ellipse, freehand, eraser, clearButton);
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(canvas);

        canvas.setOnMousePressed(mouseHandler::onMousePressed);
        canvas.setOnMouseDragged(mouseHandler::onMouseDragged);
        canvas.setOnMouseReleased(mouseHandler::onMouseReleased);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("增强画板");
        primaryStage.show();
    }

    private RadioButton createToolButton(String text, ToolType tool, ToggleGroup group) {
        RadioButton button = new RadioButton(text);
        button.setToggleGroup(group);
        button.setOnAction(e -> currentTool[0] = tool);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

