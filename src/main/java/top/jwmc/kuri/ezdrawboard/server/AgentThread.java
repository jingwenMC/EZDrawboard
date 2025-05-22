package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.networking.Router;

import java.io.IOException;
import java.net.Socket;

public class AgentThread extends Thread {
    private Socket connection;
    private Router router;
    public AgentThread(Socket _connection) {
        connection = _connection;
        router = new Router(connection);
        start();
    }
    @Override
    public void run() {
        try {

        }catch (Exception e) {
            System.err.println(STR."Agent thread got exception: \{e}");
            System.err.println(STR."Socket INET: \{connection.getInetAddress()}");
            e.printStackTrace();
        }
    }
}
