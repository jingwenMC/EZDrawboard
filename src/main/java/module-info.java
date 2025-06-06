module top.jwmc.kuri.ezdrawboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires java.sql;
    requires jdk.compiler;
    exports top.jwmc.kuri.ezdrawboard.client;
    exports top.jwmc.kuri.ezdrawboard.server;
    exports top.jwmc.kuri.ezdrawboard.networking;
    exports top.jwmc.kuri.ezdrawboard.data;
    opens top.jwmc.kuri.ezdrawboard.client to javafx.fxml;
}