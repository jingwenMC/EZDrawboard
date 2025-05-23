package top.jwmc.kuri.ezdrawboard.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static boolean running = true;
    public Server(int port) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while(running) {
                Socket socket = serverSocket.accept();
                System.out.println(STR."[CONN] Accepted connection from \{socket.getInetAddress()}");
                new AgentThread(socket);
            }
        }
    }
}
