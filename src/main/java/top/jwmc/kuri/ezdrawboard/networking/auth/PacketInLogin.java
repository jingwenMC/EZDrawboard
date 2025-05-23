package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketInLogin extends Packet {
    private static DatabaseAccessor databaseAccessor;
    public static void setDatabaseAccessor(DatabaseAccessor databaseAccessor) {
        PacketInLogin.databaseAccessor = databaseAccessor;
    }
    String username;
    String token;
    @Override
    public String getName() {
        return "PacketInLogin";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {
        PacketOutLogin packetOutLogin = new PacketOutLogin();
        username = in.readUTF();
        token = in.readUTF();
        if(databaseAccessor.checkTokenExpire(token)<0) {
            packetOutLogin.result = PacketOutLogin.Result.FAILURE;
            packetOutLogin.message = "Token is expired";
            packetOutLogin.sendPacket(socket);
        } else {
            packetOutLogin.result = PacketOutLogin.Result.SUCCESS;
            packetOutLogin.message = "Token is expired";
            packetOutLogin.sendPacket(socket);
            //TODO:Authenticate
        }

    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(token);
    }
}
