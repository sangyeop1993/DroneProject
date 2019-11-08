package kosa.team2.gcs.test;

import kosa.team2.gcs.network.NetworkConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONObject;

public class ElectricMagnet {
    //Field
    private MqttClient client;

    //Constructor
    public ElectricMagnet() throws Exception {
        client = new MqttClient("tcp://localhost:1882", MqttClient.generateClientId(), null);
        client.connect();
    }

    //Method
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
