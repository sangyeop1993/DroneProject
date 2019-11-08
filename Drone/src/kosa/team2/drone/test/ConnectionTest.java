package kosa.team2.drone.test;

import kosa.team2.drone.network.NetworkConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONObject;

public class ConnectionTest {
    //Field
    private MqttClient client;

    //Constructor
    public ConnectionTest() throws Exception {
        client = new MqttClient("tcp://106.253.56.124:1882", MqttClient.generateClientId(), null);
        client.connect();
        System.out.println("test connect");
    }

    //Method
    public void sendMessage() throws Exception {
        JSONObject obj = new JSONObject();
        String json = obj.toString();
        System.out.println(json);
        client.publish(NetworkConfig.droneTopic + "/test/pub", json.getBytes(), 0, false);
    }
}
