package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketInList extends ServerContextualPacket implements Authenticated {

    public PacketInList(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketInList";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        new PacketOutList(getAgent()).sendPacket(out);
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
