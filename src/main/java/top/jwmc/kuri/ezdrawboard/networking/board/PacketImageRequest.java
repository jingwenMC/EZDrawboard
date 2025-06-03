package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.client.EnhancedDrawingBoard;
import top.jwmc.kuri.ezdrawboard.client.Mainapp;
import top.jwmc.kuri.ezdrawboard.client.Util;
import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketImageRequest extends ServerContextualPacket implements Authenticated {
    public static volatile boolean UPDATED=false;
    String direction;
    public PacketImageRequest(AgentThread context) {
        super(context);
    }
    public PacketImageRequest(String direction) {
        super(null);
        this.direction = direction;
    }

    @Override
    public String getName() {
        return "PacketImageRequest";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        direction = in.readUTF();
        if(agent==null) {
            EnhancedDrawingBoard.saveCanvas();
            UPDATED=false;
            while (!UPDATED) {
                Thread.onSpinWait();
            }
            new PacketImageDeliver(Util.fileToByte(),direction).sendPacket(Mainapp.out);
        } else {
            this.sendPacket(agent.getBoard().getOwner().getRouter().getDataOutputStream());
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(direction);
    }
}
