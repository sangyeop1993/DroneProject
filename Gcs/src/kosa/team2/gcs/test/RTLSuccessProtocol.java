package kosa.team2.gcs.test;

import kosa.team2.gcs.main.GcsMain;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RTLSuccessProtocol {
    private static final Logger log = LoggerFactory.getLogger(RTLSuccessProtocol.class);

    private MqttClient client;

    public RTLSuccessProtocol() {
        try {
            client.connect();
            droneMissionSuccess();
        } catch (Exception e) { }
    }

    public void droneMissionSuccess() throws Exception {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage message) throws Exception {
                byte[] arr = message.getPayload();
                JSONObject obj = new JSONObject(arr);
                if(obj.getString("msgid").equals("HEARTBEAT")) {
                    if (obj.getString("mode").equals("RTL")) {
                        GcsMain.instance.controller.flightMap.controller.missionClear();
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        client.subscribe("/drone/fc/pub");
    }
}
