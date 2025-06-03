package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.data.Message;
import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketOutLogin;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketOutRegister;
import top.jwmc.kuri.ezdrawboard.networking.board.*;

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
        PacketDrawFreehand packetDrawFreehand = new PacketDrawFreehand(null);
        packets.put(packetDrawFreehand.getName(), packetDrawFreehand);
        PacketOutOnline packetOutOnline = new PacketOutOnline(null);
        packets.put(packetOutOnline.getName(), packetOutOnline);
    }
}
