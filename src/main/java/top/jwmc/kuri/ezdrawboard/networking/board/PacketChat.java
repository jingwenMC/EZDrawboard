package top.jwmc.kuri.ezdrawboard.networking.board;

import top.jwmc.kuri.ezdrawboard.client.Mainapp;
import top.jwmc.kuri.ezdrawboard.client.Talk;
import top.jwmc.kuri.ezdrawboard.data.Message;
import top.jwmc.kuri.ezdrawboard.networking.Authenticated;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketChat extends ServerContextualPacket implements Authenticated {
    Message message;
    public PacketChat(Message message) {
        super(null);
        this.message = message;
    }

    public PacketChat(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketChat";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        message = new Message(in.readUTF(),in.readUTF(),in.readUTF());
        if(agent==null) {
            Talk.chatMessages.add(message);
            if(Talk.INSTANCE!=null)Talk.INSTANCE.createMessageBubble(message.time(),message.user(),message.content(),message.user().equals(Mainapp.user.name()));
        } else {
            for(AgentThread agentThread : agent.getBoard().getUsers()) {
                sendPacket(agentThread.getRouter().getDataOutputStream());
            }
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(message.time());
        out.writeUTF(message.user());
        out.writeUTF(message.content());
    }
}
