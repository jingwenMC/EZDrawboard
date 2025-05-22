package top.jwmc.kuri.ezdrawboard.client;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.List;

public class Painter {
    private final GraphicsContext gc;
    private final Canvas canvas;
    private Color currentColor = Color.BLACK;
    private final Color backgroundColor = Color.WHITE;
    private final double eraserWidth = 20;
    private Image backgroundImage;
    public Painter(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        clearCanvas();
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        redrawBackground();
    }

    public void clearBackgroundImage() {
        this.backgroundImage = null; // 移除背景图片
        clearCanvas(); // 恢复白色背景
    }

    private void redrawBackground() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (backgroundImage != null) {
            // 直接使用 backgroundImage，不需要转换
            gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            gc.setFill(backgroundColor);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void clearCanvas() {
        redrawBackground();
    }

    public void drawTempShape(
            double x1, double y1, double x2, double y2,
            EnhancedDrawingBoard.ToolType tool,
            List<EnhancedDrawingBoard.Drawing> drawings // 🔁 新增参数：传入已有图形
    ) {
        clearCanvas();
        redrawAll(drawings); // ✅ 先重绘已有内容

        gc.setStroke(currentColor);
        gc.setLineWidth(1);

        switch (tool) {
            case LINE -> gc.strokeLine(x1, y1, x2, y2);
            case RECTANGLE -> drawRectangle(x1, y1, x2, y2);
            case ELLIPSE -> drawEllipse(x1, y1, x2, y2);
        }
    }


    public void drawFreehandPath(List<Point2D> path, EnhancedDrawingBoard.ToolType tool) {
        if (path.size() < 2) return;

        gc.setStroke(tool == EnhancedDrawingBoard.ToolType.ERASER ? backgroundColor : currentColor);
        gc.setLineWidth(tool == EnhancedDrawingBoard.ToolType.ERASER ? eraserWidth : 1);
        gc.beginPath();
        gc.moveTo(path.get(0).getX(), path.get(0).getY());

        for (int i = 1; i < path.size(); i++) {
            Point2D point = path.get(i);
            gc.lineTo(point.getX(), point.getY());
        }

        gc.stroke();
        gc.closePath();
    }

    public void redrawAll(List<EnhancedDrawingBoard.Drawing> drawings) {
        clearCanvas();

        for (EnhancedDrawingBoard.Drawing drawing : drawings) {
            gc.setStroke(drawing.color);
            if (drawing.type == EnhancedDrawingBoard.ToolType.FREEHAND || drawing.type == EnhancedDrawingBoard.ToolType.ERASER) {
                gc.setLineWidth(drawing.type == EnhancedDrawingBoard.ToolType.ERASER ? eraserWidth : 1);
                drawFreehandPath(drawing.path, drawing.type);
            } else {
                gc.setLineWidth(1);
                switch (drawing.type) {
                    case LINE -> gc.strokeLine(drawing.x1, drawing.y1, drawing.x2, drawing.y2);
                    case RECTANGLE -> drawRectangle(drawing.x1, drawing.y1, drawing.x2, drawing.y2);
                    case ELLIPSE -> drawEllipse(drawing.x1, drawing.y1, drawing.x2, drawing.y2);
                }
            }
        }
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
}

