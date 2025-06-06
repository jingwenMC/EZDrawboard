package top.jwmc.kuri.ezdrawboard.networking.board;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import top.jwmc.kuri.ezdrawboard.client.DrawingList;
import top.jwmc.kuri.ezdrawboard.client.EnhancedDrawingBoard;
import top.jwmc.kuri.ezdrawboard.client.Mainapp;
import top.jwmc.kuri.ezdrawboard.client.MouseHandler;
import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketDrawingAdd extends ServerContextualPacket implements Authenticated {
    List<Point2D> path;
    EnhancedDrawingBoard.ToolType tool;
    double brushSize;
    EnhancedDrawingBoard.ToolType type;
    Color color;
    double x1, y1, x2, y2;
    public PacketDrawingAdd(AgentThread agent) {
        super(agent);
    }
    public PacketDrawingAdd(EnhancedDrawingBoard.Drawing drawing) {
        super(null);
        this.path = drawing.path;
        this.tool = drawing.type;
        this.brushSize = drawing.brushSize;
        this.type = drawing.type;
        this.color = drawing.color;
        this.x1 = drawing.x1;
        this.y1 = drawing.y1;
        this.x2 = drawing.x2;
        this.y2 = drawing.y2;
    }

    @Override
    public String getName() {
        return "PacketDrawingAdd";
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
        type = EnhancedDrawingBoard.ToolType.values()[in.readInt()];
        color = Color.valueOf(in.readUTF());
        x1 = in.readDouble();
        y1 = in.readDouble();
        x2 = in.readDouble();
        y2 = in.readDouble();
        if(agent==null) {
            if(DrawingList.INSTANCE !=null && DrawingList.INSTANCE.available)
                DrawingList.INSTANCE.add(true,new EnhancedDrawingBoard.Drawing(type,color,path,x1,y1,x2,y2,brushSize));
        } else for(AgentThread agentThread : agent.getBoard().getUsers()) { //Server
            if(agentThread!=agent) sendPacket(agentThread.getRouter().getDataOutputStream());
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeInt(path==null?0:path.size());
        if(path!=null) for (Point2D point : path) {
            out.writeDouble(point.getX());
            out.writeDouble(point.getY());
        }
        out.writeInt(tool.ordinal());
        out.writeDouble(brushSize);
        out.writeInt(type.ordinal());
        out.writeUTF(color.toString());
        out.writeDouble(x1);
        out.writeDouble(y1);
        out.writeDouble(x2);
        out.writeDouble(y2);
    }
}
