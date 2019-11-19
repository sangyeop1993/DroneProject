package syk.drone.device;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class PCF8591 {
    //Field
    public static final int AIN_0 = 0b01000000;
    public static final int AIN_1 = 0b01000001;
    public static final int AIN_2 = 0b01000010;
    public static final int AIN_3 = 0b01000011;

    private int i2cAddress;
    private int ainNo;
    private int analogVal;

    //Constructor
    public PCF8591(int i2cAddress, int ainNo) {
        this.i2cAddress = i2cAddress;
        this.ainNo = ainNo;
    }

    //Method
    //return 0~255
    public int analogRead() throws Exception {
        I2CBus i2CBus = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2CDevice = i2CBus.getDevice(this.i2cAddress);
        analogVal = i2CDevice.read(this.ainNo);
        return analogVal;
    }
}
