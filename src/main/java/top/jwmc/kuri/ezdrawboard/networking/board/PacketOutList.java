package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;
import top.jwmc.kuri.ezdrawboard.server.Board;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PacketOutList extends ServerContextualPacket {
    public int items;
    public ArrayList<String> ids;
    public ArrayList<String> names;
    public ArrayList<Integer> onlines;

    public PacketOutList(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketOutList";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        items = in.readInt();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            ids.add(in.readUTF());
        }
        size = in.readInt();
        for (int i = 0; i < size; i++) {
            names.add(in.readUTF());
        }
        size = in.readInt();
        for (int i = 0; i < size; i++) {
            onlines.add(in.readInt());
        }
        //TODO:Client
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        ids = new ArrayList<>(getAgent().getInstance().getBoardMap().keySet());
        names = new ArrayList<>();
        onlines = new ArrayList<>();
        for (int i = 0; i < getAgent().getInstance().getBoardMap().size(); i++) {
            String id = ids.get(i);
            Board board = getAgent().getInstance().getBoardMap().get(id);
            names.add(board.name);
            onlines.add(board.getUsers().size());
        }
        out.writeInt(items);
        out.writeInt(ids.size());
        for (String id : ids) {
            out.writeUTF(id);
        }
        out.writeInt(names.size());
        for (String name : names) {
            out.writeUTF(name);
        }
        out.writeInt(onlines.size());
        for (Integer i : onlines) {
            out.writeInt(i);
        }
    }
}
