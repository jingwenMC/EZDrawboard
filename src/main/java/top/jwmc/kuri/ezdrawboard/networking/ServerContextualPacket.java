package top.jwmc.kuri.ezdrawboard.networking;

import top.jwmc.kuri.ezdrawboard.server.AgentThread;

public abstract class ServerContextualPacket extends Packet {
    protected final AgentThread agent;
    public ServerContextualPacket(AgentThread context) {
        this.agent = context;
    }
    protected AgentThread getAgent() {
        return agent;
    }
}
