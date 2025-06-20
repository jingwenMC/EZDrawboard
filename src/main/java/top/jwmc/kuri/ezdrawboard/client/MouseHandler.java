package top.jwmc.kuri.ezdrawboard.client;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MouseHandler {
    private double startX, startY;
    private final List<Point2D> currentPath = new ArrayList<>();
    private final EnhancedDrawingBoard.ToolType[] currentTool;
    private final List<EnhancedDrawingBoard.Drawing> drawings;
    private final Painter painter;
    private final EnhancedDrawingBoard board;

    public MouseHandler(List<EnhancedDrawingBoard.Drawing> drawings,
                        EnhancedDrawingBoard.ToolType[] currentTool,
                        Painter painter, EnhancedDrawingBoard board) {
        this.drawings = drawings;
        this.currentTool = currentTool;
        this.painter = painter;
        this.board = board;
    }

    public void onMousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        currentPath.clear();
        currentPath.add(new Point2D(startX, startY));


        if (currentTool[0] == EnhancedDrawingBoard.ToolType.ERASER) {
            painter.eraseAtPoint(startX, startY, board.getBrushSize());
        }
    }

    public void onMouseDragged(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

        switch (currentTool[0]) {
            case FREEHAND:
                currentPath.add(new Point2D(endX, endY));
                painter.drawFreehandPath(currentPath, currentTool[0], board.getBrushSize(),Painter.INSTANCE.currentColor);
                break;
            case ERASER:
                    currentPath.add(new Point2D(endX, endY));
                    painter.eraseAtPoint(endX, endY, board.getBrushSize());
                break;
            default:
                painter.drawTempShape(startX, startY, endX, endY, currentTool[0],drawings, board.getBrushSize());
                break;
        }
    }

    public void onMouseReleased(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();
        if (currentTool[0] == EnhancedDrawingBoard.ToolType.FREEHAND) {
            drawings.add(new EnhancedDrawingBoard.Drawing(
                    currentTool[0],
                    painter.getCurrentColor(),
                    new ArrayList<>(currentPath),
                    board.getBrushSize()
            ));
        }
        else if (currentTool[0] == EnhancedDrawingBoard.ToolType.ERASER) {
                drawings.add(new EnhancedDrawingBoard.Drawing(
                        currentTool[0],
                        Color.WHITE, // 橡皮擦使用白色
                        new ArrayList<>(currentPath),
                        board.getBrushSize()
                ));
        }
        else {
            drawings.add(new EnhancedDrawingBoard.Drawing(
                    currentTool[0],
                    painter.getCurrentColor(),
                    startX, startY, endX, endY,
                    board.getBrushSize()
            ));
        }

        currentPath.clear();
        painter.redrawAll(drawings);
    }
}

