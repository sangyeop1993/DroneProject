package syk.drone.device;

import com.pi4j.io.gpio.RaspiPin;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThermistorSensor {
    //Field
    private PCF8591 pcf8591;
    private static Logger logger = LoggerFactory.getLogger(Camera.class);

    //Constructor
    public ThermistorSensor() {
        this.pcf8591 = new PCF8591(0x48, PCF8591.AIN_1);
        throwTemp();
    }
    //Method
    public double getVal() throws Exception {
        int analogVal = pcf8591.analogRead();
        double value = 5*(double) analogVal/255;
        value = 10000*value/(5-value);
        value = 1/(((Math.log(value/10000))/3950) + (1/(273.15+25)));
        value = value-273.15;
        return value;
    }
    private MqttClient mqttClient;
    private String pubTopic = "/drone/temperature/pub";
    private String subTopic = "/drone/temperature/sub";
    public void throwTemp() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        double temperature = getVal();
                        JSONObject jsonObject = new JSONObject();
                        int temperature_1 = (int)temperature;
                        jsonObject.put("msgid", "TEMPERATURE");
                        jsonObject.put("temperature", temperature_1);
                        String json = jsonObject.toString();
                        if(mqttClient != null && mqttClient.isConnected()) {
                            mqttClient.disconnect();
                            mqttClient.close();
                        }
                        //System.out.println(temperature_1);
                        mqttClient = new MqttClient("tcp://106.253.56.124:1882", MqttClient.generateClientId(), null);
                        mqttClient.connect();
                        mqttClient.publish(pubTopic, json.getBytes(), 0, false);
                        Thread.sleep(5000);
                    } catch(Exception e) {}
                }
            }
        };
        thread.start();
    }
}


