package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PacketOutList extends ServerContextualPacket {
    public int items;
    public ArrayList<String> ids;
    public ArrayList<String> names;
    public ArrayList<Integer> onlines;
    public ArrayList<Boolean> privates;

    @Override
    public String getName() {
        return "PacketOutList";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {
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
        size = in.readInt();
        for (int i = 0; i < size; i++) {
            privates.add(in.readBoolean());
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        //TODO:Gather data
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
        out.writeInt(privates.size());
        for (Boolean b : privates) {
            out.writeBoolean(b);
        }
    }
}
