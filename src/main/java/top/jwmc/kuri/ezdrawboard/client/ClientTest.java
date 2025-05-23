package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket("127.0.0.1",6060)) {
            new PacketPing().sendPacket(socket);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            if(in.readInt()==0x10311101) {
                System.out.println(STR."Service:\{in.readUTF()}");
            }
        }
    }
}
