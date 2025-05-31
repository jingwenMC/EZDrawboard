package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket("127.0.0.1",6060)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new PacketPing().sendPacket(out);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            if(in.readInt()==0x10311101) {
                System.out.println("Service:"+in.readUTF());
            }
            new PacketPing().sendPacket(out);
            DataInputStream in2 = new DataInputStream(socket.getInputStream());
            if(in.readInt()==0x10311101) {
                System.out.println("Service:"+in.readUTF());
            }
        }
    }
}
