package top.jwmc.kuri.ezdrawboard.networking;

import top.jwmc.kuri.ezdrawboard.networking.auth.*;

import java.net.Socket;
import java.util.HashMap;

public class Router {
    private HashMap<String,Packet> packets;
    private final Socket socket;
    public Router(Socket socket) {
        this.socket = socket;
        packets = new HashMap<>();
        //注册路由
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
    }

    public void startHandleRequest() {
        while(!socket.isClosed()) {
            //TODO
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
