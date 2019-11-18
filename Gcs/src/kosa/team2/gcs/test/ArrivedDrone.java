package kosa.team2.gcs.test;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kosa.team2.gcs.main.GcsMain;
import kosa.team2.gcs.network.Drone;
import kosa.team2.gcs.report.Report;
import kosa.team2.gcs.service2.Service2;
import kosa.team2.gcs.service3.Service3;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import syk.common.MavJsonMessage;
import syk.gcs.mapview.FlightMap;
import syk.gcs.network.FlightController;

import java.util.ArrayList;

public class ArrivedDrone {
    private static Logger log = LoggerFactory.getLogger(ArrivedDrone.class);

    private MqttClient client;
    String pubTopic;
    String subTopic;
    FlightController fc;


    //==================================================================================
//    public ArrivedDrone() {
//        try { } catch(Exception e) { }
//    }
    //==================================================================================
    public void mqttConnect(String pubTopic, String subTopic) {
        this.pubTopic = pubTopic;
        this.subTopic = subTopic;
        try {
            client = new MqttClient("tcp://106.253.56.124:1882", MqttClient.generateClientId(), null);
            MqttConnectOptions option = new MqttConnectOptions();
            client.connect(option);
            arrivedDroneEnd();
            fc = new FlightController();
            log.info("ArrivedDrone 객체 생성 완료");
            log.info("Report MQTT Connected: " + "tcp://106.253.56.124:1882");
        } catch(Exception e) {
            e.printStackTrace();
            try { client.close(); } catch (Exception e1) {}
            try { Thread.sleep(1000); } catch (InterruptedException e1) {}
        }
    }
    //==================================================================================
    public void arrivedDroneEnd() throws MqttException {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage message) throws Exception {
                GcsMain.instance.controller.flightMap.controller.showInfoLabel("배송이 완료되었습니다");
                log.info("사용자가 수취를 확인하였습니다");
                String result = new String(String.valueOf(message));
                log.info("result: " + result);
                JSONArray wayBackHome = GcsMain.instance.controller.flightMap.controller.getMissionItems();
                JSONArray newWay = new JSONArray();
                //home 의 X, Y 경로 저장
                double homePointX = wayBackHome.getJSONObject(0).getDouble("x");
                double homePointY = wayBackHome.getJSONObject(0).getDouble("y");
                newWay.put(wayBackHome.getJSONObject(0));
                for(int i=wayBackHome.length()-2; i>0; i--) {
                    int newSeq = wayBackHome.length() - wayBackHome.getJSONObject(i).getInt("seq") - 1;
                    JSONObject object = wayBackHome.getJSONObject(i);
                    object.put("seq", newSeq);
                    newWay.put(object);
                    if(i==1) {
                        object.put("seq", newSeq);
                        object.put("command", MavJsonMessage.MAVJSON_MISSION_COMMAND_RTL);
                        object.put("param1", 0);
                        object.put("param2", 0);
                        object.put("param3", 0);
                        object.put("param4", 0);
                        object.put("x", homePointX);
                        object.put("y", homePointY);
                        object.put("z", 0);
                    }
                }
                GcsMain.instance.controller.flightMap.controller.setMissionItems2(newWay);
                GcsMain.instance.controller.drone.flightController.sendMissionUpload(GcsMain.instance.controller.flightMap.controller.getMissionItems());
                System.out.println(GcsMain.instance.controller.flightMap.controller.getMissionItems());
                GcsMain.instance.controller.drone.flightController.sendMissionStart();
                //확인시 돌아가는 경로를 업로드 한뒤 트루일경우 자석으로 떨구고 리턴 false 의 경우엔 떨구지 않고 리턴
//                if(result.equals("true")) {
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            Service2 service2 = new Service2();
//                            service2.show();
//                        }
//                    });
//                } //else { return 시킬거임; }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                log.info("ArrivedDroneMqtt 받을준비 완료");
                log.info("Mqtt 배달완료 토픽: " + subTopic);
            }
        });
        client.subscribe(subTopic);
    }
    //==================================================================================

}
