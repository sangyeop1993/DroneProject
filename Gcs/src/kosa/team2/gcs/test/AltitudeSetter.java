package kosa.team2.gcs.test;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AltitudeSetter {
    private static final Logger log = LoggerFactory.getLogger(AltitudeSetter.class);

    private MqttClient client;

    public AltitudeSetter() {
        try {
            log.info("AltitudeSetter 객체 생성");
            client.connect();
            AltitudeSetting();
            log.info("AltitudeSetting method start");
        } catch(Exception e) { }
    }

    public void AltitudeSetting() throws MqttException {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                log.info("mqtt topic connect");
            }
        });
        client.subscribe("drone/fc/sub");
    }
}
