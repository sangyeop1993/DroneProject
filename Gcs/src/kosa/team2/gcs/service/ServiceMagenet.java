package kosa.team2.gcs.service;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kosa.team2.gcs.main.GcsMain;

public class ServiceMagenet {
    //Field
    private Stage stage;

    //Constructor
    public ServiceMagenet() {
        stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(GcsMain.instance.primaryStage);
    }
}
