package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.networking.Router;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientBootstrap {
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
        prop.store(new FileOutputStream("config.properties"), null);
        try (Socket socket = new Socket(ip, Integer.parseInt(port))) {
            Router router = new ClientRouterImpl(socket);
            new Thread(()->Mainapp.main(socket,args)).start();
            try {
                router.startHandleRequest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
