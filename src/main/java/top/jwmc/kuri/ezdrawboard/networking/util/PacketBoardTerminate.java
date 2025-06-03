package top.jwmc.kuri.ezdrawboard.networking.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import top.jwmc.kuri.ezdrawboard.networking.ServerContextualPacket;
import top.jwmc.kuri.ezdrawboard.server.AgentThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketBoardTerminate extends ServerContextualPacket {
    public PacketBoardTerminate(AgentThread context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PacketBoardTerminate";
    }

    @Override
    public void handlePacketIn(DataOutputStream out, DataInputStream in) throws IOException {
        if(agent==null) Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("房主已下线");
            alert.setContentText("房主与服务器的连接丢失，画板已销毁，现在退出程序……");
            alert.showAndWait();
            System.exit(0);
        });
    }

    @Override
    public void handlePacketOut(DataOutputStream out) throws IOException {

    }
}
