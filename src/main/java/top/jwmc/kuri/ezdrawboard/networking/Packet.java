package top.jwmc.kuri.ezdrawboard.networking;

import java.io.*;
import java.net.Socket;

public abstract class Packet {
    public abstract String getName();
    public abstract void handlePacketIn(Socket socket,DataInputStream in) throws IOException;
    public abstract void handlePacketOut(DataOutputStream out) throws IOException;
    public final void sendPacket(Socket socket) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(0x10311101);
        out.writeUTF(getName());
        handlePacketOut(out);
        out.flush();
    }
}
