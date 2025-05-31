package top.jwmc.kuri.ezdrawboard.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Router {
    private final ConcurrentHashMap<String, Packet> packets;
    private final Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    public abstract void initiateRouterMap(ConcurrentHashMap<String, Packet> packets);
    public Router(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        packets = new ConcurrentHashMap<>();
        //注册路由
        //initiateRouterMap(packets);
    }

    public void startHandleRequest() throws IOException {
        while(!socket.isClosed()) {
            int magic;
            try {
                magic = dataInputStream.readInt();
            } catch (EOFException e) {
                System.out.println("[EOF] EOF from "+socket.getInetAddress());
                break;
            } catch (SocketException e) {
                System.out.println("[SDC] Socket disconnect from "+socket.getInetAddress());
                break;
            }
            if (magic == 0x10311101) {
                String packetName = dataInputStream.readUTF();
                Packet packet = packets.get(packetName);
                if(packet != null) {
                    if((packet instanceof ServerContextualPacket serverContextualPacket) && (packet instanceof Authenticated)) {
                        if(serverContextualPacket.getAgent()!=null&&serverContextualPacket.getAgent().getUserInfo()==null)throw new IllegalStateException();
                    }
                    packet.handlePacketIn(dataOutputStream, dataInputStream);
                }
            }
        }
        if(!socket.isClosed()) socket.close();
        System.out.println("[CLOS] Closed connection from "+socket.getInetAddress());
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public ConcurrentHashMap<String, Packet> getPackets() {
        return packets;
    }
}
