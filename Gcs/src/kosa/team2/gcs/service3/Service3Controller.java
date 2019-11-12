package kosa.team2.gcs.service3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Service3Controller implements Initializable {

    @FXML private WebView webView;
    private WebEngine webEngine;
    @FXML private Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initWebView();
        btnCancel.setOnAction(btnCancelEventHandler);
    }
    public void initWebView() {
        webEngine = webView.getEngine();
        webEngine.load("http://106.253.56.124:8082/FinalWebProject/requestList");
    }

    private EventHandler<ActionEvent> btnCancelEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    };
}
