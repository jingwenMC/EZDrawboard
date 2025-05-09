module top.jwmc.kuri.ezdrawboard {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens top.jwmc.kuri.ezdrawboard to javafx.fxml;
    exports top.jwmc.kuri.ezdrawboard;
}