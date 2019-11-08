package kosa.team2.drone.test;

import com.pi4j.io.gpio.*;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

public class Electromagnet {
    //Field
    private GpioController controller;
    private GpioPinDigitalOutput magnetPin1;
    private GpioPinDigitalOutput magnetPin2;

    //Constructor
    public Electromagnet(Pin magnet1, Pin magnet2) {
        controller = GpioFactory.getInstance();
        magnetPin1 = controller.provisionDigitalOutputPin(magnet1);
        magnetPin2 = controller.provisionDigitalOutputPin(magnet2);
    }

    //Method
    public void off() {
        magnetPin1.low();
        magnetPin2.low();
    }
    public void on() {
        magnetPin1.high();
        magnetPin2.high();
    }

    private MqttClient client;
    private String pubTopic;
    private String subTopic;

    public void mattConnect(String mqttBrokerConnStr, String pubTopic, String subTopic) throws Exception {
        this.pubTopic = pubTopic;
        this.subTopic = subTopic;
        while (true) {
            try {
                client = new MqttClient(mqttBrokerConnStr, MqttClient.generateClientId(), null);
                MqttConnectOptions options = new MqttConnectOptions();
                client.connect(options);
                break;
            } catch (Exception e) {}
        }
        receiveJSON();
    }

    public void receiveJSON() throws Exception {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage message) throws Exception {
                byte[] arr = message.getPayload();
                String json = new String(arr);
                System.out.println(json);
                JSONObject obj = new JSONObject(json);
                String action = obj.getString("action");
                if (action.equals("on")) {
                    on();
                } else {
                    off();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        client.subscribe(subTopic);
    }
}
