package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.networking.Router;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketPing;

import java.io.*;
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
        Choose.main(args);
    }
}
