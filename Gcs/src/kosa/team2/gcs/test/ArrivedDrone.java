package kosa.team2.gcs.test;

import kosa.team2.gcs.main.GcsMain;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import syk.common.MavJsonMessage;
import syk.gcs.network.FlightController;

public class ArrivedDrone {
    private static Logger log = LoggerFactory.getLogger(ArrivedDrone.class);

    private MqttClient client;
    String pubTopic;
    String subTopic;
    FlightController fc;
    ElectricMagnet em;

    //==================================================================================
//    public ArrivedDrone() {
//        RTLSuccessProtocol rsp = new RTLSuccessProtocol();
//        try {  } catch(Exception e) { }
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
            em = new ElectricMagnet();
            log.info("ArrivedDrone 객체 생성 완료");
            log.info("Report MQTT Connected: " + "tcp://106.253.56.124:1882");
        } catch(Exception e) {
            e.printStackTrace();
            try { client.close(); } catch (Exception e1) {}
            try { Thread.sleep(1000); } catch (InterruptedException e1) {}
        }
    }
    //==================================================================================
    public void arrivedDroneEnd() throws Exception {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage message) throws Exception {
                //-------------------------------------------------------------------------------------------------
                GcsMain.instance.controller.flightMap.controller.showInfoLabel("배송이 완료되었습니다");
                Thread.sleep(1000);
                GcsMain.instance.controller.flightMap.controller.showInfoLabel("복귀경로를 탐색중입니다.");
                Thread.sleep(3000);

                log.info("사용자가 드론을 가시범위에서 확인하였습니다");
                String result = new String(String.valueOf(message));
                log.info("result: " + result);

                //확인시 돌아가는 경로를 업로드 한뒤 트루일경우 자석으로 떨구고 리턴 false 의 경우엔 떨구지 않고 리턴
                if(result.equals("true")) {
                    em.magnetOff();
                }
                //-------------------------------------------------------------------------------------------------
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
                try {
                    GcsMain.instance.controller.flightMap.controller.missionClear();
                    GcsMain.instance.controller.flightMap.controller.setMissionItems2(newWay);
                    GcsMain.instance.controller.drone.flightController.sendMissionUpload(newWay);
                    Thread.sleep(3000);
                    GcsMain.instance.controller.flightMap.controller.showInfoLabel("드론이 복귀합니다");
                    GcsMain.instance.controller.drone.flightController.sendMissionStart();
                } catch (Exception e) { }
                //-------------------------------------------------------------------------------------------------
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                log.info("Mqtt ArrivedDrone Topic: " + subTopic);
            }
        });
        client.subscribe(subTopic);
    }
    //==================================================================================
}
