package top.jwmc.kuri.ezdrawboard.networking.util;

import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketPing extends Packet {
    @Override
    public String getName() {
        return "Ping";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {
        sendPacket(socket);
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
