package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.auth.*;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRouterImpl extends Router {
    private final AgentThread agent;
    public ServerRouterImpl(Socket socket, AgentThread agent) throws IOException {
        super(socket);
        this.agent = agent;
        initiateRouterMap(getPackets());
    }

    @Override
    public void initiateRouterMap(ConcurrentHashMap<String, Packet> packets) {
        PacketInLogin packetInLogin = new PacketInLogin(agent);
        packets.put(packetInLogin.getName(), packetInLogin);
        PacketInRegister packetInRegister = new PacketInRegister();
        packets.put(packetInRegister.getName(), packetInRegister);
        PacketPing packetPing = new PacketPing();
        packets.put(packetPing.getName(), packetPing);
    }
}
