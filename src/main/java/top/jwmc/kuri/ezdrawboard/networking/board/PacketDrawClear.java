package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.client.Painter;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketDrawClear extends ServerContextualPacket {
    public PacketDrawClear(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketDrawClear";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        if(agent != null)  {
            for(AgentThread agentThread : agent.getBoard().getUsers()) //Server
                if(agentThread!=agent) sendPacket(agentThread.getRouter().getDataOutputStream());
        }else {
            Painter.INSTANCE.clearCanvasOnline();
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
