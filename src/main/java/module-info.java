module top.jwmc.kuri.ezdrawboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    exports top.jwmc.kuri.ezdrawboard.client;
    opens top.jwmc.kuri.ezdrawboard.client to javafx.fxml;
}