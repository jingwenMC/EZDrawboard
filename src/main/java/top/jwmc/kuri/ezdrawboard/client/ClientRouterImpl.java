package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.networking.Packet;
import top.jwmc.kuri.ezdrawboard.networking.Router;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRouterImpl extends Router {
    public ClientRouterImpl(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void initiateRouterMap(ConcurrentHashMap<String, Packet> packets) {

    }
}
