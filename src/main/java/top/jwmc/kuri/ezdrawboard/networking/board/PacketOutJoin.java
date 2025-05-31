package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.client.OnlineBoard;
import top.jwmc.kuri.ezdrawboard.client.RegisterApp;
import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketOutRegister;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOutJoin extends Packet {
    Result result;
    String message;

    @Override
    public String getName() {
        return "PacketOutJoin";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        result = Result.values()[in.readInt()];
        message = in.readUTF();
        OnlineBoard.RESULT = result == Result.SUCCESS;
        OnlineBoard.MESSAGE = message;
        OnlineBoard.UPDATED = true;
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
