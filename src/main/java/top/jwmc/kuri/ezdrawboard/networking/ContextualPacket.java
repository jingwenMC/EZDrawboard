package top.jwmc.kuri.ezdrawboard.networking;

import top.jwmc.kuri.ezdrawboard.server.AgentThread;

public abstract class ContextualPacket extends Packet {
    protected final AgentThread agent;
    public ContextualPacket() {
        agent = null;
    }
    public ContextualPacket(AgentThread context) {
        this.agent = context;
    }
}
