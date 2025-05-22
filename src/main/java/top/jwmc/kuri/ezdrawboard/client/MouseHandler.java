package top.jwmc.kuri.ezdrawboard.client;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class MouseHandler {
    private double startX, startY;
    private final List<Point2D> currentPath = new ArrayList<>();
    private final EnhancedDrawingBoard.ToolType[] currentTool;
    private final List<EnhancedDrawingBoard.Drawing> drawings;
    private final Painter painter;

    public MouseHandler(List<EnhancedDrawingBoard.Drawing> drawings,
                        EnhancedDrawingBoard.ToolType[] currentTool,
                        Painter painter) {
        this.drawings = drawings;
        this.currentTool = currentTool;
        this.painter = painter;
    }

    public void onMousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        currentPath.clear();
        currentPath.add(new Point2D(startX, startY));
    }

    public void onMouseDragged(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

        switch (currentTool[0]) {
            case FREEHAND:
            case ERASER:
                currentPath.add(new Point2D(endX, endY));
                painter.redrawAll(drawings);
                painter.drawFreehandPath(currentPath, currentTool[0]);
                break;
            default:
                painter.drawTempShape(startX, startY, endX, endY, currentTool[0],drawings);
                break;
        }
    }

    public void onMouseReleased(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

        if (currentTool[0] == EnhancedDrawingBoard.ToolType.FREEHAND ||
                currentTool[0] == EnhancedDrawingBoard.ToolType.ERASER) {
            drawings.add(new EnhancedDrawingBoard.Drawing(currentTool[0], painter.getCurrentColor(), new ArrayList<>(currentPath)));
        } else {
            drawings.add(new EnhancedDrawingBoard.Drawing(currentTool[0], painter.getCurrentColor(), startX, startY, endX, endY));
        }

        currentPath.clear();
        painter.redrawAll(drawings);
    }
}

