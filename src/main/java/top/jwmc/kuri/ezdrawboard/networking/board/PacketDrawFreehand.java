package top.jwmc.kuri.ezdrawboard.networking.board;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import top.jwmc.kuri.ezdrawboard.client.DrawingList;
import top.jwmc.kuri.ezdrawboard.client.EnhancedDrawingBoard;
import top.jwmc.kuri.ezdrawboard.client.Mainapp;
import top.jwmc.kuri.ezdrawboard.client.Painter;
import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketDrawFreehand extends ServerContextualPacket implements Authenticated {
    List<Point2D> path;
    EnhancedDrawingBoard.ToolType tool;
    double brushSize;
    String color;
    public PacketDrawFreehand(AgentThread agent) {
        super(agent);
    }
    public PacketDrawFreehand(List<Point2D> path, EnhancedDrawingBoard.ToolType tool, double brushSize,String color) {
        super(null);
        this.path = path;
        this.tool = tool;
        this.brushSize = brushSize;
        this.color = color;
    }

    @Override
    public String getName() {
        return "PacketDrawFreehand";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        int size = in.readInt();
        path = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            path.add(new Point2D(in.readDouble(), in.readDouble()));
        }
        tool = EnhancedDrawingBoard.ToolType.values()[in.readInt()];
        brushSize = in.readDouble();
        color = in.readUTF();
        if(agent==null) { //Client
            if(DrawingList.INSTANCE.available) {
                synchronized (Painter.INSTANCE) {
                    Color tmp = Painter.INSTANCE.currentColor;
                    Painter.INSTANCE.currentColor = Color.valueOf(color);
                    Painter.INSTANCE.drawFreehandPath(true, path, tool, brushSize);
                    Painter.INSTANCE.currentColor = tmp;
                }
            }
        } else for(AgentThread agentThread : agent.getBoard().getUsers()) { //Server
            if(agentThread!=agent) sendPacket(agentThread.getRouter().getDataOutputStream());
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeInt(path.size());
        for (Point2D point : path) {
            out.writeDouble(point.getX());
            out.writeDouble(point.getY());
        }
        out.writeInt(tool.ordinal());
        out.writeDouble(brushSize);
        out.writeUTF(color);
    }
}
