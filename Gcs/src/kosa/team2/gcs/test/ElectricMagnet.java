package kosa.team2.gcs.test;

import kosa.team2.gcs.network.NetworkConfig;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class ElectricMagnet {
    //Field
    private MqttClient client;

    //Constructor
    public ElectricMagnet() throws Exception {
        client = new MqttClient("tcp://localhost:1882", MqttClient.generateClientId(), null);
        client.connect();
        electronicMagnetStatus();
    }

    //Method
    public void electronicMagnetStatus() {
        System.out.println("연결이 되엇다");
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                JSONObject obj = new JSONObject(mqttMessage);
                System.out.println(obj);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        try {
            client.subscribe(NetworkConfig.getInstance().droneTopic+"/text/pub");
        } catch (Exception e) {}
    }
    //-------------------------------------------------------------------
    public void magnetOn() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("action", "on");
        String json = obj.toString();
        client.publish(NetworkConfig.getInstance().droneTopic + "/test/sub", json.getBytes(), 0, false);
    }

    public void magnetOff() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("action", "off");
        String json = obj.toString();
        client.publish(NetworkConfig.getInstance().droneTopic + "/test/sub", json.getBytes(), 0, false);
    }
}
