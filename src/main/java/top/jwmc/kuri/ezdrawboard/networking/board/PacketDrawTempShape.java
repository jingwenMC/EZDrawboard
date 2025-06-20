package top.jwmc.kuri.ezdrawboard.networking.board;

import javafx.scene.paint.Color;
import top.jwmc.kuri.ezdrawboard.client.DrawingList;
import top.jwmc.kuri.ezdrawboard.client.EnhancedDrawingBoard;
import top.jwmc.kuri.ezdrawboard.client.Painter;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketDrawTempShape extends ServerContextualPacket {
    double x1,y1,x2,y2,size;
    EnhancedDrawingBoard.ToolType tool;
    String color;
    public PacketDrawTempShape(double x1, double y1, double x2, double y2, double size,
                               EnhancedDrawingBoard.ToolType tool,String color) {
        super(null);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.size = size;
        this.tool = tool;
        this.color = color;
    }
    public PacketDrawTempShape(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketTempShape";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        x1 = in.readDouble();
        y1 = in.readDouble();
        x2 = in.readDouble();
        y2 = in.readDouble();
        size = in.readDouble();
        tool = EnhancedDrawingBoard.ToolType.values()[in.readInt()];
        color = in.readUTF();
        if(agent==null) {
            synchronized (Painter.INSTANCE) {
                Color tmp = Painter.INSTANCE.currentColor;
                Painter.INSTANCE.currentColor = Color.valueOf(color);
                Painter.INSTANCE.drawTempShape(true,x1,y1,x2,y2,tool, DrawingList.INSTANCE,size);
                Painter.INSTANCE.currentColor = tmp;
            }
        } else {
            for(AgentThread agentThread : agent.getBoard().getUsers()) //Server
                if(agentThread!=agent) sendPacket(agentThread.getRouter().getDataOutputStream());
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeDouble(x1);
        out.writeDouble(x2);
        out.writeDouble(y1);
        out.writeDouble(y2);
        out.writeDouble(size);
        out.writeInt(tool.ordinal());
        out.writeUTF(color);
    }
}
