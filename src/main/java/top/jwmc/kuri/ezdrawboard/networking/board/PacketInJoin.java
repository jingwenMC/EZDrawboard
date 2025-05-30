package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.data.User;
import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;
import top.jwmc.kuri.ezdrawboard.server.Board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketInJoin extends ServerContextualPacket implements Authenticated {
    public PacketInJoin(AgentThread context) {
        super(context);
    }
    String id;
    @Override
    public String getName() {
        return "PacketInJoin";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        id = in.readUTF();
        User user = getAgent().getUserInfo();
        Board board = getAgent().getInstance().getBoardMap().get(id);
        PacketOutJoin packet = new PacketOutJoin();
        if(board != null) {
            getAgent().setBoard(board);
            board.getUsers().add(getAgent());
            packet.message = "Success";
            packet.result = PacketOutJoin.Result.SUCCESS;
        } else {
            packet.message = "加入失败，未找到对应的房间";
            packet.result = PacketOutJoin.Result.FAILURE;
        }
        packet.sendPacket(out);
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(id);
    }
}
