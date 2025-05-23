package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.User;

import java.net.Socket;

public class AgentThread extends Thread {
    private final Socket connection;
    private final Router router;
    private User userInfo;
    public AgentThread(Socket _connection) {
        connection = _connection;
        router = new Router(connection,this);
        start();
    }
    @Override
    public void run() {
        try {
            //TODO:INIT
            router.startHandleRequest();
            if(!connection.isClosed())connection.close();
        }catch (Exception e) {
            System.err.println(STR."Agent thread got exception: \{e}");
            System.err.println(STR."Socket INET: \{connection.getInetAddress()}");
            e.printStackTrace();
        }
    }
}
