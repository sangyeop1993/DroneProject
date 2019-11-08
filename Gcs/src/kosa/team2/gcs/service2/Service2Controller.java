package kosa.team2.gcs.service2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kosa.team2.gcs.test.ElectricMagnet;

import java.net.URL;
import java.util.ResourceBundle;

public class Service2Controller implements Initializable {
    //Field
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnMagnetOn;
    @FXML
    private Button btnMagnetOff;

    //Method
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCancel.setOnAction(btnCancelEventHandler);
        btnMagnetOn.setOnAction(btnMagnetOnEventHandler);
        btnMagnetOff.setOnAction(btnMagnetOffEventHandler);
    }

    private EventHandler<ActionEvent> btnCancelEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    };

    private EventHandler<ActionEvent> btnMagnetOnEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                ElectricMagnet em = new ElectricMagnet();
                em.magnetOn();
            } catch (Exception e) {}
        }
    };

    private EventHandler<ActionEvent> btnMagnetOffEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                ElectricMagnet em = new ElectricMagnet();
                em.magnetOff();
            } catch (Exception e) {}
        }
    };
}
