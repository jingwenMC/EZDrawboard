package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.networking.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketInRegister extends Packet {
    private static DatabaseAccessor databaseAccessor;
    public static void setDatabaseAccessor(DatabaseAccessor databaseAccessor) {
        PacketInRegister.databaseAccessor = databaseAccessor;
    }

    String name;
    String passwordHash;
    String salt;
    @Override
    public String getName() {
        return "PacketInRegister";
    }

    @Override
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {

    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
