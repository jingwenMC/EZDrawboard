package top.jwmc.kuri.ezdrawboard.client;

import javafx.stage.Stage;
import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientBootstrap {
    public static String IP = "127.0.0.1";
    public static int PORT = 8080;
    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        File file = new File("config.properties");
        if (!file.exists()) {
            prop.setProperty("server.port", "6060");
            prop.setProperty("server.ip", "127.0.0.1");
            prop.store(new FileOutputStream("config.properties"), null);
        }
        prop.load(new FileInputStream("config.properties"));
        String port = prop.getProperty("server.port");
        String ip = prop.getProperty("server.ip");
        IP = ip;
        PORT = Integer.parseInt(port);
        prop.store(new FileOutputStream("config.properties"), null);
        boot(IP,PORT,args);
        //Choose.main(args);

    }
    public static void boot(String ip, int port, String[] args) throws IOException {
        try (Socket socket = new Socket(ip, port)) {
            Router router = new ClientRouterImpl(socket);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new Thread(()-> {
                try {
                    new PacketPing().sendPacket(out);
                    Mainapp.out = out;
                    Mainapp.ONLINE_MODE = true;
                    Talk.main(args);
                    if(!socket.isClosed())socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            try {
                router.startHandleRequest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
