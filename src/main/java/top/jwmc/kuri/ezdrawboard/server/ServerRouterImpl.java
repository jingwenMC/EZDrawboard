package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.auth.*;
import top.jwmc.kuri.ezdrawboard.networking.board.*;
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
        PacketInList packetInList = new PacketInList(agent);
        packets.put(packetInList.getName(), packetInList);
        PacketInJoin packetInJoin = new PacketInJoin(agent);
        packets.put(packetInJoin.getName(), packetInJoin);
        PacketChat packetChat = new PacketChat(agent);
        packets.put(packetChat.getName(), packetChat);
        PacketDrawingAdd packetDrawingAdd = new PacketDrawingAdd(agent);
        packets.put(packetDrawingAdd.getName(), packetDrawingAdd);
        PacketInOnline packetInOnline = new PacketInOnline(agent);
        packets.put(packetInOnline.getName(), packetInOnline);
        PacketImageDeliver packetImageDeliver = new PacketImageDeliver(agent);
        packets.put(packetImageDeliver.getName(), packetImageDeliver);
        PacketImageRequest packetImageRequest = new PacketImageRequest(agent);
        packets.put(packetImageRequest.getName(), packetImageRequest);
        PacketDrawClear packetDrawClear = new PacketDrawClear(agent);
        packets.put(packetDrawClear.getName(), packetDrawClear);
        PacketDrawTempShape packetDrawTempShape = new PacketDrawTempShape(agent);
        packets.put(packetDrawTempShape.getName(), packetDrawTempShape);
        PacketDrawFreehand packetDrawFreehand = new PacketDrawFreehand(agent);
        packets.put(packetDrawFreehand.getName(), packetDrawFreehand);
    }
}
