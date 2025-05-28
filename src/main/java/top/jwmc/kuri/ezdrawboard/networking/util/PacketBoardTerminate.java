package top.jwmc.kuri.ezdrawboard.networking.util;

import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketBoardTerminate extends ServerContextualPacket {
    //TODO:WIP
    public PacketBoardTerminate(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {

    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
