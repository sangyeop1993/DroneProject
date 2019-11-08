package kosa.team2.drone.test;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

public class MqttConnTest  {
    //Field
    private MqttClient client;

    //Constructor
    public MqttConnTest() throws Exception {
        //Mqtt Conn
        client = new MqttClient("tcp://localhost:1882", MqttClient.generateClientId(), null);
        client.connect();
        System.out.println("Mqtt Connected");
    }
    //Method
    public void receiveJSON() throws Exception {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage message) throws Exception {
                byte[] arr = message.getPayload();
                String json = new String(arr);
                JSONObject obj = new JSONObject(json);
                String action = obj.getString("action");
                if(action.equals("ON")) {

                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        client.subscribe("/test/pub");
    }
}
