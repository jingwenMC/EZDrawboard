package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;
import top.jwmc.kuri.ezdrawboard.server.Board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class PacketInCreate extends ServerContextualPacket implements Authenticated {
    public PacketInCreate(AgentThread context) {
        super(context);
    }
    String name;
    String description;
    @Override
    public String getName() {
        return "PacketInCreate";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {
        name = in.readUTF();
        description = in.readUTF();
        String id = UUID.randomUUID().toString();
        Board board = new Board(id,name,description,getAgent());
        getAgent().getInstance().getBoardMap().put(id,board);
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(name);
        out.writeUTF(description);
    }
}
