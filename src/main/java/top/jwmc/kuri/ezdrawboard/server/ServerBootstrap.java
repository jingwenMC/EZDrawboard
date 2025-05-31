package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketInLogin;
import top.jwmc.kuri.ezdrawboard.networking.auth.PacketInRegister;

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
            prop.store(new FileOutputStream("config.properties"), null);
        }
        prop.load(new FileInputStream("config.properties"));
        String port = prop.getProperty("server.port");
        prop.store(new FileOutputStream("config.properties"), null);
        System.out.println("Server starting on port "+port+"...");
        DatabaseAccessor databaseAccessor = new MemoryImpl();
        PacketInLogin.setDatabaseAccessor(databaseAccessor);
        PacketInRegister.setDatabaseAccessor(databaseAccessor);
        new Server(Integer.parseInt(port));
    }
}
