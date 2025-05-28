package top.jwmc.kuri.ezdrawboard.networking;

import java.io.*;

public abstract class Packet {
    public abstract String getName();
    public abstract void handlePacketIn(DataOutputStream out,DataInputStream in) throws IOException;
    public abstract void handlePacketOut(DataOutputStream out) throws IOException;
    public final void sendPacket(DataOutputStream out) throws IOException {
        out.writeInt(0x10311101);
        out.writeUTF(getName());
        handlePacketOut(out);
        //out.flush();
    }
}
