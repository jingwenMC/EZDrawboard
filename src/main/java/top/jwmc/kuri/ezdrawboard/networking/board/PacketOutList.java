package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.client.OnlineBoard;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.*;
import java.util.ArrayList;

public class PacketOutList extends ServerContextualPacket {
    public ArrayList<String> ids;

    public PacketOutList(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketOutList";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        ids = new ArrayList<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            ids.add(in.readUTF());
        }
        OnlineBoard.userList = ids;
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        ids = new ArrayList<>(getAgent().getInstance().getBoardMap().keySet());
        out.writeInt(ids.size());
        for (String id : ids) {
            out.writeUTF(id);
        }
    }
}
