package kosa.team2.gcs.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kosa.team2.gcs.main.GcsMain;
import kosa.team2.gcs.main.GcsMainController;
import kosa.team2.gcs.network.NetworkConfig;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Report {
    static MqttClient mqttClient;
    String pubTopic;
    String subTopic;
    public static List<ReportItem> reports = new ArrayList<ReportItem>();
    private static Logger logger = LoggerFactory.getLogger(Report.class);
    private static Stage stage;
    private static ReportController controller;
    private static GcsMainController gcsMainController;


    public Report() { }

    public void mqttConnect(String pubTopic, String subTopic){
        this.pubTopic = pubTopic;
        this.subTopic = subTopic;

            try{
                mqttClient = new MqttClient("tcp://106.253.56.124:1882", MqttClient.generateClientId(),null);
                MqttConnectOptions option = new MqttConnectOptions();
                mqttClient.connect(option);

                logger.info("Report MQTT Connected: " + "tcp://106.253.56.124:1882");

            }catch(Exception e){
                e.printStackTrace();
                try { mqttClient.close(); } catch (Exception e1) {}
                try { Thread.sleep(1000); } catch (InterruptedException e1) {}
            }
        mqttReceiveFromWeb();
    }
    private void mqttReceiveFromWeb() {
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                logger.info("Arrived Somthing");
                byte[] arr = mqttMessage.getPayload();
                String json = new String(arr);
                logger.info(json);
                JSONObject obj = new JSONObject(json);
                logger.info("test oid: " + obj.get("oid"));
                logger.info("test datetime: " + obj.get("datetime"));
                ReportItem reportItem = new ReportItem();
                reportItem.setOid(obj.getInt("oid"));
                reportItem.setLat(obj.getDouble("lat"));
                reportItem.setLng(obj.getDouble("lng"));
                reportItem.setTime(obj.getString("datetime"));
                reports.add(0, reportItem);
                GcsMain.instance.controller.flightMap.controller.showInfoLabel("새로운 배송요청이 접수되었습니다.");
                dialogRefresh();
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }
        });

        try {
            mqttClient.subscribe(subTopic);
            logger.info("Magnet MQTT subscribed: " + subTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void show(){
        try{
            stage = new Stage(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(GcsMain.instance.primaryStage);

            FXMLLoader fxmlLoader = new FXMLLoader(Report.class.getResource("Report.fxml"));
            BorderPane pane = fxmlLoader.load();
            controller = fxmlLoader.getController();
            FXMLLoader fxmlGcsLoader = new FXMLLoader(Report.class.getResource("GcsMain.fxml"));
            gcsMainController = fxmlGcsLoader.getController();

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(GcsMain.class.getResource("style_dark.css").toExternalForm());

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e){e.printStackTrace();}
    }

    public static void close(){
        stage.close();
    }

    public void dialogRefresh() {
        if(controller == null) return;
        controller.reportUpdate();
    }

    public static void resultSend() throws MqttException {
        JSONArray jsonArray = GcsMain.instance.controller.flightMap.controller.getMissionItems();
        String byteJson = jsonArray.toString();
        byte[] json = byteJson.getBytes();

        System.out.println(jsonArray);
        mqttClient.publish(NetworkConfig.getInstance().droneTopic + "/chicken/delivery/publish",json, 0, false);
    }

    public void accidentSend(String result) throws MqttException {
        byte[] json = result.getBytes();
        mqttClient.publish(NetworkConfig.getInstance().droneTopic + "/chicken/delivery/publish",json, 0, false);
    }
}
