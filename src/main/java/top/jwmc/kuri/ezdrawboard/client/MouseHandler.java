package top.jwmc.kuri.ezdrawboard.client;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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

        //橡皮擦点擦除
        if (currentTool[0] == EnhancedDrawingBoard.ToolType.ERASER &&
                board.getCurrentEraserMode() == EnhancedDrawingBoard.EraserMode.PIXEL) {
            painter.eraseAtPoint(startX, startY, board.getEraserSize());
        }
    }

    public void onMouseDragged(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

        switch (currentTool[0]) {
            case FREEHAND:
            case ERASER:
//                currentPath.add(new Point2D(endX, endY));
//                painter.redrawAll(drawings);
//                painter.drawFreehandPath(currentPath, currentTool[0]);
                if (board.getCurrentEraserMode() == EnhancedDrawingBoard.EraserMode.PIXEL) {
                    // 点擦除模式
                    currentPath.add(new Point2D(endX, endY));
                    painter.eraseAtPoint(endX, endY, board.getEraserSize());
                } else {
                    // 线擦除模式
                    currentPath.add(new Point2D(endX, endY));
                    painter.redrawAll(drawings);
                    painter.drawFreehandPath(currentPath, currentTool[0], board.getEraserSize());
                }
                break;
            default:
                painter.drawTempShape(startX, startY, endX, endY, currentTool[0],drawings);
                break;
        }
    }

    public void onMouseReleased(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

//        if (currentTool[0] == EnhancedDrawingBoard.ToolType.FREEHAND ||
//                currentTool[0] == EnhancedDrawingBoard.ToolType.ERASER) {
//            drawings.add(new EnhancedDrawingBoard.Drawing(currentTool[0], painter.getCurrentColor(), new ArrayList<>(currentPath)));
//        } else {
//            drawings.add(new EnhancedDrawingBoard.Drawing(currentTool[0], painter.getCurrentColor(), startX, startY, endX, endY));
//        }
        if (currentTool[0] == EnhancedDrawingBoard.ToolType.FREEHAND) {
            drawings.add(new EnhancedDrawingBoard.Drawing(
                    currentTool[0],
                    painter.getCurrentColor(),
                    new ArrayList<>(currentPath)
            ));
        }
        else if (currentTool[0] == EnhancedDrawingBoard.ToolType.ERASER) {
            if (board.getCurrentEraserMode() == EnhancedDrawingBoard.EraserMode.LINE) {
                // 线擦除模式：添加橡皮擦路径到绘图列表
                drawings.add(new EnhancedDrawingBoard.Drawing(
                        currentTool[0],
                        Color.WHITE, // 橡皮擦使用白色
                        new ArrayList<>(currentPath),
                        board.getEraserSize(),
                        board.getCurrentEraserMode()
                ));
            }
            // 点擦除模式不需要保存到drawings，因为效果已直接绘制
        }
        else {
            drawings.add(new EnhancedDrawingBoard.Drawing(
                    currentTool[0],
                    painter.getCurrentColor(),
                    startX, startY, endX, endY
            ));
        }

        currentPath.clear();
        painter.redrawAll(drawings);
    }
}

