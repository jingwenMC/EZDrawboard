package top.jwmc.kuri.ezdrawboard.networking;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Router {
    private final ConcurrentHashMap<String, Packet> packets;
    private final Socket socket;
    public abstract void initiateRouterMap(ConcurrentHashMap<String, Packet> packets);
    public Router(Socket socket) {
        this.socket = socket;
        packets = new ConcurrentHashMap<>();
        //注册路由
        initiateRouterMap(packets);
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
