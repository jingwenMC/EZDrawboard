package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;
import top.jwmc.kuri.ezdrawboard.server.Board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

public class PacketImageDeliver extends ServerContextualPacket {
    byte[] image;
    String direction = "";
    public PacketImageDeliver(byte[] image) {
        super(null);
        this.image = image;
    }
    public PacketImageDeliver(byte[] image, String direction) {
        super(null);
        this.image = image;
        this.direction = direction;
    }
    public PacketImageDeliver(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketImageDeliver";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        int size = in.readInt();
        this.image = new byte[size];
        for (int i = 0; i < size; i++) {
            image[i] = in.readByte();
        }
        String direction = in.readUTF();
        if(agent != null) {
            Board board = agent.getBoard();
            if(direction.isEmpty()) {
                for(AgentThread agentThread : board.getUsers()) {
                    if(!agentThread.equals(agent)) {
                        this.sendPacket(agentThread.getRouter().getDataOutputStream());
                    }
                }
            } else {
                Optional<AgentThread> optional = board.getUsers().stream()
                        .filter(agentThread -> agentThread.getUserInfo().name().equals(direction))
                        .findAny();
                if(optional.isPresent()) {
                    AgentThread agentThread = optional.get();
                    this.sendPacket(agentThread.getRouter().getDataOutputStream());
                }
            }
        } else {

        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeInt(image.length);
        for (byte b : image) {
            out.writeByte(b);
        }
        out.writeUTF(direction);
    }
}
