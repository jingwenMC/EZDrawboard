package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketOutLogin;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketOutRegister;
import top.jwmc.kuri.ezdrawboard.networking.board.*;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketBoardTerminate;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRouterImpl extends Router {
    public ClientRouterImpl(Socket socket) throws IOException {
        super(socket);
        initiateRouterMap(getPackets());
    }

    @Override
    public void initiateRouterMap(ConcurrentHashMap<String, Packet> packets) {
        PacketOutLogin packetOutLogin = new PacketOutLogin();
        packets.put(packetOutLogin.getName(), packetOutLogin);
        PacketOutRegister packetOutRegister = new PacketOutRegister();
        packets.put(packetOutRegister.getName(), packetOutRegister);
        PacketOutList packetOutList = new PacketOutList(null);
        packets.put(packetOutList.getName(), packetOutList);
        PacketOutJoin packetOutJoin = new PacketOutJoin();
        packets.put(packetOutJoin.getName(), packetOutJoin);
        PacketChat packetChat = new PacketChat();
        packets.put(packetChat.getName(), packetChat);
        PacketDrawingAdd packetDrawingAdd = new PacketDrawingAdd((AgentThread) null);
        packets.put(packetDrawingAdd.getName(), packetDrawingAdd);
        PacketOutOnline packetOutOnline = new PacketOutOnline(null);
        packets.put(packetOutOnline.getName(), packetOutOnline);
        PacketImageDeliver packetImageDeliver = new PacketImageDeliver(new byte[]{});
        packets.put(packetImageDeliver.getName(), packetImageDeliver);
        PacketImageRequest packetImageRequest = new PacketImageRequest((AgentThread) null);
        packets.put(packetImageRequest.getName(), packetImageRequest);
        PacketBoardTerminate packetBoardTerminate = new PacketBoardTerminate(null);
        packets.put(packetBoardTerminate.getName(), packetBoardTerminate);
        PacketDrawClear packetDrawClear = new PacketDrawClear(null);
        packets.put(packetDrawClear.getName(), packetDrawClear);
    }
}
