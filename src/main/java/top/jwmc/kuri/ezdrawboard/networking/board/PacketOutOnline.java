package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class PacketOutOnline extends ServerContextualPacket {
    public int items;
    public ArrayList<String> ids;
    public PacketOutOnline(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketOutOnline";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        items = in.readInt();
        ids = new ArrayList<>();
        for (int i = 0; i < items; i++) {
            ids.add(in.readUTF());
        }
        //TODO:Client
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        items = getAgent().getBoard().getUsers().size();
        for(AgentThread user : getAgent().getBoard().getUsers()) {
            ids.add(user.getName());
        }
        out.writeInt(items);
        for (int i = 0; i < items; i++) {
            out.writeUTF(ids.get(i));
        }
    }
}
