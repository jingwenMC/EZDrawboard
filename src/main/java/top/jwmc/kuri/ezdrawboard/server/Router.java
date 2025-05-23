package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.auth.*;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
    private final ConcurrentHashMap<String, Packet> packets;
    private final Socket socket;
    private final AgentThread agent;
    public Router(Socket socket,AgentThread agent) {
        this.socket = socket;
        this.agent = agent;
        packets = new ConcurrentHashMap<>();
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

        PacketPing packetPing = new PacketPing();
        packets.put(packetPing.getName(), packetPing);
    }

    public void startHandleRequest() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        while(!socket.isClosed()) {
            if(dataInputStream.available()<=0) break;
            int magic = dataInputStream.readInt();
            if (magic == 0x10311101) {
                String packetName = dataInputStream.readUTF();
                Packet packet = packets.get(packetName);
                if(packet != null) {
                    packet.handlePacketIn(socket, dataInputStream);
                }
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
