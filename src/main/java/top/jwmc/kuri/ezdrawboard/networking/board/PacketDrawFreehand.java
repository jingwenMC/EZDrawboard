package top.jwmc.kuri.ezdrawboard.networking.board;

import javafx.geometry.Point2D;
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
    int eraserSize;
    public PacketDrawFreehand(AgentThread agent) {
        super(agent);
    }
    public PacketDrawFreehand(List<Point2D> path, EnhancedDrawingBoard.ToolType tool, int eraserSize) {
        super(null);
        this.path = path;
        this.tool = tool;
        this.eraserSize = eraserSize;
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
        eraserSize = in.readInt();
        if(agent==null) { //Client
            Mainapp.painter.drawFreehandPath(true, path, tool, eraserSize);
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
        out.writeInt(eraserSize);
    }
}
