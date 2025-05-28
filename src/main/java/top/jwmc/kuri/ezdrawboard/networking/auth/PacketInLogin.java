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
    public void handlePacketIn(Socket socket, DataInputStream in) throws IOException {
        PacketOutLogin packetOutLogin = new PacketOutLogin();
        username = in.readUTF();
        token = in.readUTF();
        if(databaseAccessor.checkTokenExpire(username,token)<0) {
            packetOutLogin.result = PacketOutLogin.Result.FAILURE;
            packetOutLogin.message = "凭证无效";
            packetOutLogin.sendPacket(socket);
        } else {
            User user = databaseAccessor.getUserByName(username);
            getAgent().setUserInfo(user);
            packetOutLogin.result = PacketOutLogin.Result.SUCCESS;
            packetOutLogin.message = "登录成功";
            packetOutLogin.sendPacket(socket);
        }
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(token);
    }
}
