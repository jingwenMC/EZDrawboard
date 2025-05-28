package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.auth.*;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRouterImpl extends Router {
    private final AgentThread agent;
    public ServerRouterImpl(Socket socket, AgentThread agent) {
        super(socket);
        this.agent = agent;
    }

    @Override
    public void initiateRouterMap(ConcurrentHashMap<String, Packet> packets) {
        PacketInLogin packetInLogin = new PacketInLogin();
        packets.put(packetInLogin.getName(), packetInLogin);
        PacketOutLogin packetOutLogin = new PacketOutLogin();
        packets.put(packetOutLogin.getName(), packetOutLogin);

        PacketInRegister packetInRegister = new PacketInRegister();
        packets.put(packetInRegister.getName(), packetInRegister);
        PacketOutRegister packetOutRegister = new PacketOutRegister();
        packets.put(packetOutRegister.getName(), packetOutRegister);

        PacketInToken packetInToken = new PacketInToken();
        packets.put(packetInToken.getName(), packetInToken);
        PacketOutToken packetOutToken = new PacketOutToken();
        packets.put(packetOutToken.getName(), packetOutToken);

        PacketPing packetPing = new PacketPing();
        packets.put(packetPing.getName(), packetPing);
    }
}
