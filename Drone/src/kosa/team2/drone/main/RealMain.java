/*
java -Djava.library.path=/usr/lib/jni:/home/pi/opencv/opencv-3.4.5/build/lib -cp classes:lib/'*' companion.companion.RealMain
 */

package kosa.team2.drone.main;


import com.pi4j.io.gpio.RaspiPin;
import kosa.team2.drone.network.NetworkConfig;
import kosa.team2.drone.test.ConnectionTest;
import kosa.team2.drone.test.Electromagnet;
import syk.drone.device.Camera;
import syk.drone.device.FlightController;

public class RealMain {
    public static void main(String[] args) {
        NetworkConfig networkConfig = new NetworkConfig();

        FlightController flightController = new FlightController();
        flightController.mavlinkConnectRxTx("/dev/ttyAMA0");
        flightController.mqttConnect(
                networkConfig.mqttBrokerConnStr,
                networkConfig.droneTopic +"/fc/pub",
                networkConfig.droneTopic +"/fc/sub"
        );

        Camera camera0 = new Camera();
        camera0.cameraConnect(0, 320, 240, 180);
        camera0.mattConnect(
                networkConfig.mqttBrokerConnStr,
                networkConfig.droneTopic + "/cam0/pub",
                networkConfig.droneTopic + "/cam0/sub"
        );

        Camera camera1 = new Camera();
        camera1.cameraConnect(1, 240, 320, 270);
        camera1.mattConnect(
                networkConfig.mqttBrokerConnStr,
                networkConfig.droneTopic +"/cam1/pub",
                networkConfig.droneTopic +"/cam1/sub"
        );

        Electromagnet em = new Electromagnet(RaspiPin.GPIO_24, RaspiPin.GPIO_25);
        try {
            em.mattConnect(
                    networkConfig.mqttBrokerConnStr,
                    networkConfig.droneTopic + "/test/pub",
                    networkConfig.droneTopic + "/test/sub"
            );
        } catch (Exception e) {}

        try {
            ConnectionTest ct = new ConnectionTest();
            ct.sendMessage();
        }catch(Exception e) {
            System.out.println("Error");
        }
    }
}
