package top.jwmc.kuri.ezdrawboard.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Router {
    private final ConcurrentHashMap<String, Packet> packets;
    private final Socket socket;
    private DataOutputStream dataOutputStream;
    public abstract void initiateRouterMap(ConcurrentHashMap<String, Packet> packets);
    public Router(Socket socket) {
        this.socket = socket;
        packets = new ConcurrentHashMap<>();
        //注册路由
        initiateRouterMap(packets);
    }

    public void startHandleRequest() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        while(!socket.isClosed()) {
            if(dataInputStream.available()<=0) break;
            int magic = dataInputStream.readInt();
            if (magic == 0x10311101) {
                String packetName = dataInputStream.readUTF();
                Packet packet = packets.get(packetName);
                if(packet != null) {
                    if((packet instanceof ServerContextualPacket serverContextualPacket) && (packet instanceof Authenticated)) {
                        if(serverContextualPacket.getAgent().getUserInfo()==null)throw new IllegalStateException();
                    }
                    packet.handlePacketIn(dataOutputStream, dataInputStream);
                }
            }
        }
        System.out.println(STR."[CLOS] Closed connection from \{socket.getInetAddress()}");
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
