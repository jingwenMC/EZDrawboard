package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.ContextualPacket;
import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketInList extends ContextualPacket {

    @Override
    public String getName() {
        return "PacketInList";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {

    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
