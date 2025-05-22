package top.jwmc.kuri.ezdrawboard.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerBootstrap {
    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        File file = new File("config.properties");
        if (!file.exists()) {
            prop.setProperty("server.port", "6060");
            prop.store(new FileOutputStream("config.properties"), "Drawboard Server Config v1 | By: xxx xxx xxx");
        }
        prop.load(new FileInputStream("config.properties"));
        String port = prop.getProperty("server.port");
        prop.store(new FileOutputStream("config.properties"), null);
        System.out.println(STR."Server starting on port \{port}...");
        Server server = new Server(Integer.parseInt(port));
    }
}
