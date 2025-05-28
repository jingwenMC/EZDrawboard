package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketInOnline extends ServerContextualPacket implements Authenticated {
    public PacketInOnline(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketInOnline";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {
        if(getAgent().getBoard()==null)throw new IllegalStateException();
        new PacketOutOnline(getAgent()).sendPacket(socket);
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
