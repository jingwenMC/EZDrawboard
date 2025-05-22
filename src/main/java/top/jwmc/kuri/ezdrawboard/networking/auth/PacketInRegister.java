package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketInRegister extends Packet {
    @Override
    public String getName() {
        return "PacketInRegister";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {

    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
