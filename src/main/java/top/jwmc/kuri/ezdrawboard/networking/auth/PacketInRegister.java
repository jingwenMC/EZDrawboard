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
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        name = in.readUTF();
        passwordHash = in.readUTF();
        salt = in.readUTF();
        PacketOutRegister register = new PacketOutRegister();
        if(databaseAccessor.getUserByName(name) != null) {
            register.message="用户已注册";
            register.result= PacketOutRegister.Result.FAILURE;
            register.sendPacket(out);
            return;
        }
        try {
            databaseAccessor.registerUser(name, passwordHash, salt);
            register.message="注册成功，请登录";
            register.result= PacketOutRegister.Result.SUCCESS;
        } catch (IllegalStateException e) {
            register.message="数据库错误";
            register.result= PacketOutRegister.Result.FAILURE;
        }
        register.sendPacket(out);
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(name);
        out.writeUTF(passwordHash);
        out.writeUTF(salt);
    }
}
