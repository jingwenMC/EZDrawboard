package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketOutRegister extends Packet {
    @Override
    public String getName() {
        return "PacketOutRegister";
    }
    Result result;
    String message;

    @Override
    public void handlePacketIn(DataOutputStream out,DataInputStream in) throws IOException {
        result = Result.values()[in.readInt()];
        message = in.readUTF();
        //TODO: Client interaction
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeInt(result.ordinal());
        out.writeUTF(message);
    }
    public enum Result {
        SUCCESS,FAILURE
    }
}
