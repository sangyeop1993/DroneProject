package kosa.team2.gcs.service1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kosa.team2.gcs.main.GcsMain;


public class Service1 {
    //Field
    private Stage stage;

    //Constructor
    public Service1() {
        try {
            stage = new Stage(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(GcsMain.instance.primaryStage);
            BorderPane pane = (BorderPane) FXMLLoader.load(getClass().getResource("Service1.fxml"));
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(GcsMain.class.getResource("style_dark.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
        } catch (Exception e){}
    }

    //Method
    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
