package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class PacketInToken extends Packet {
    private static DatabaseAccessor databaseAccessor;
    public static void setDatabaseAccessor(DatabaseAccessor databaseAccessor) {
        PacketInToken.databaseAccessor = databaseAccessor;
    }

    String username;
    String hashedPassword;

    @Override
    public String getName() {
        return "PacketInToken";
    }

    @Override
    public void handlePacketIn(Socket socket,DataInputStream in) throws IOException {
        username = in.readUTF();
        hashedPassword = in.readUTF();
        PacketOutToken token = new PacketOutToken();
        if(!databaseAccessor.authenticateUser(username,hashedPassword)) {
            token.message = "Invalid username or password";
            token.result= PacketOutToken.Result.FAILURE;
            token.sendPacket(socket);
        } else {
            UUID uuid = UUID.randomUUID();
            databaseAccessor.updateToken(username,uuid.toString());
            token.message = "Successfully updated token";
            token.result = PacketOutToken.Result.SUCCESS;
            token.token = uuid.toString();
            token.sendPacket(socket);
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(hashedPassword);
    }
}
