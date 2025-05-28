package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketOutFriendList extends ServerContextualPacket {
    @Override
    public String getName() {
        return "PacketOutFriendList";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {

    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
