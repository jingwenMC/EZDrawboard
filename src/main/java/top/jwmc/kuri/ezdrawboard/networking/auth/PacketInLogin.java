package top.jwmc.kuri.ezdrawboard.networking.auth;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.data.User;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PacketInLogin extends ServerContextualPacket {
    private static DatabaseAccessor databaseAccessor;

    public PacketInLogin(AgentThread context) {
        super(context);
    }

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
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        PacketOutLogin packetOutLogin = new PacketOutLogin();
        username = in.readUTF();
        token = in.readUTF();
        if(databaseAccessor.authenticateUser(username,token)) {
            User user = databaseAccessor.getUserByName(username);
            getAgent().setUserInfo(user);
            packetOutLogin.result = PacketOutLogin.Result.SUCCESS;
            packetOutLogin.message = "登录成功";
            packetOutLogin.sendPacket(out);
        } else {
            packetOutLogin.result = PacketOutLogin.Result.FAILURE;
            packetOutLogin.message = "登录失败，可能是错误的用户名或密码";
            packetOutLogin.sendPacket(out);
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(token);
    }
}
