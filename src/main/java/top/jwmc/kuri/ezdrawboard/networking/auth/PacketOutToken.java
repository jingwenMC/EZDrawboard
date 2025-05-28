package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketOutToken extends Packet {
    @Override
    public String getName() {
        return "PacketOutToken";
    }
    Result result;
    String message;
    String token;

    @Override
    public void handlePacketIn(DataOutputStream out,DataInputStream in) throws IOException {
        //TODO: Client interaction
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeInt(result.ordinal());
        out.writeUTF(message);
        out.writeUTF(token);
    }
    public enum Result {
        SUCCESS,FAILURE
    }
}
