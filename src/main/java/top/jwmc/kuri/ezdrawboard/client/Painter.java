package top.jwmc.kuri.ezdrawboard.client;
import com.sun.tools.javac.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketDrawFreehand;
import top.jwmc.kuri.ezdrawboard.networking.board.PacketDrawTempShape;

import java.io.IOException;
import java.util.List;

public class Painter {
    private final GraphicsContext gc;
    private final Canvas canvas;
    public Color currentColor = Color.BLACK;
    private final Color backgroundColor = Color.WHITE;
    private final double eraserWidth = 20;
    private Image backgroundImage;
    public static Painter INSTANCE;

    public Painter(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        clearCanvas();
        DrawingList.INSTANCE.available = true;
        INSTANCE = this;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        redrawBackground();
    }

    public void clearBackgroundImage() {
        this.backgroundImage = null; // 移除背景图片
        clearCanvas(); // 恢复白色背景
    }

    public void redrawBackground() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        gc.setStroke(color);
        gc.fillRect(0, 0, 0, 0);
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void clearCanvas() {
        redrawBackground();
    }

    public void clearCanvasOnline() {
        DrawingList.INSTANCE.clear();
        redrawBackground();
    }
    public void drawTempShape(
            double x1, double y1, double x2, double y2,
            EnhancedDrawingBoard.ToolType tool,
            List<EnhancedDrawingBoard.Drawing> drawings, // 传入已有图形
            double brushSize
    ) {
        drawTempShape(false, x1, y1, x2, y2, tool, drawings, brushSize);
    }

    public void drawTempShape(boolean receive,
            double x1, double y1, double x2, double y2,
            EnhancedDrawingBoard.ToolType tool,
            List<EnhancedDrawingBoard.Drawing> drawings, // 传入已有图形
            double brushSize
    ) {
        if(!receive) {
            try {
                if(Mainapp.ONLINE_MODE)new PacketDrawTempShape(x1,y1,x2,y2,brushSize,tool,currentColor.toString()).sendPacket(Mainapp.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        clearCanvas();
        redrawAll(drawings); //先重绘已有内容

        gc.setStroke(currentColor);
        gc.setLineWidth(brushSize);

        switch (tool) {
            case LINE -> gc.strokeLine(x1, y1, x2, y2);
            case RECTANGLE -> drawRectangle(x1, y1, x2, y2);
            case ELLIPSE -> drawEllipse(x1, y1, x2, y2);
        }
    }

    public void drawFreehandPath(List<Point2D> path, EnhancedDrawingBoard.ToolType tool, double brushSize) {
        drawFreehandPath(false, path, tool, brushSize);

    }


    public void drawFreehandPath(boolean receive, List<Point2D> path, EnhancedDrawingBoard.ToolType tool, double brushSize) {
        if(!receive) {
            if(Mainapp.ONLINE_MODE) {
                try {
                    new PacketDrawFreehand(path,tool,brushSize,currentColor.toString()).sendPacket(Mainapp.out);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (path.isEmpty()) return;

        if (tool == EnhancedDrawingBoard.ToolType.ERASER) {
            gc.setFill(backgroundColor);

            for (Point2D point : path) {
                gc.fillOval(point.getX() - brushSize / 2.0, point.getY() -
                        brushSize / 2.0, brushSize, brushSize);
            }

        }
        else {
            if (path.size() < 2) return;

            //gc.setStroke(currentColor);
            gc.setLineWidth(brushSize);

            gc.beginPath();
            gc.moveTo(path.get(0).getX(), path.get(0).getY());

            for (int i = 1; i < path.size(); i++) {
                Point2D point = path.get(i);
                gc.lineTo(point.getX(), point.getY());
            }

              gc.stroke();
            gc.closePath();
        }
    }

    public void eraseAtPoint(double x, double y, int size) {
        gc.setFill(backgroundColor);
        gc.fillOval(x - size/2.0, y - size/2.0, size, size);
    }

    public void redrawAll(List<EnhancedDrawingBoard.Drawing> drawings) {
        clearCanvas();

        for (EnhancedDrawingBoard.Drawing drawing : drawings) {
            gc.setStroke(drawing.color);
            if (drawing.type == EnhancedDrawingBoard.ToolType.FREEHAND) {
                gc.setStroke(drawing.color);
                gc.setLineWidth(drawing.brushSize);
                drawFreehandPath(drawing.path, drawing.type, drawing.brushSize);
            }

            else if (drawing.type == EnhancedDrawingBoard.ToolType.ERASER) {
                gc.setFill(backgroundColor);
                for (Point2D point : drawing.path) {
                    gc.fillOval(point.getX() - drawing.brushSize / 2.0,
                            point.getY() - drawing.brushSize / 2.0,
                            drawing.brushSize,
                            drawing.brushSize);
                }
            }

            else {
                gc.setStroke(drawing.color);
                gc.setLineWidth(drawing.brushSize);
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

