package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.client.Login;
import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketOutLogin extends Packet {
    @Override
    public String getName() {
        return "PacketOutLogin";
    }
    Result result;
    String message;

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        result = Result.values()[in.readInt()];
        message = in.readUTF();
        Login.RESULT = result==Result.SUCCESS;
        Login.UPDATED = true;
        System.out.println(message);
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
