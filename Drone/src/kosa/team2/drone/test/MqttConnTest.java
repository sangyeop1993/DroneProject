package kosa.team2.drone.test;

import com.pi4j.io.gpio.RaspiPin;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

public class MqttConnTest  {
    //Field
    private MqttClient client;
    private Electromagnet electromagnet;

    //Constructor
    public MqttConnTest() throws Exception {
        //자석 생성
        Electromagnet electromagnet = new Electromagnet(RaspiPin.GPIO_24, RaspiPin.GPIO_25);
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
                if(action.equals("on")) {
                    while(action.equals("on")) {
                        electromagnet.on();
                    }
                } else {
                    while(action.equals("off")) {
                        electromagnet.off();
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        client.subscribe("/test/pub");
    }
}
