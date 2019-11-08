package kosa.team2.drone.test;

import com.pi4j.io.gpio.*;

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
}
