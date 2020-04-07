package com.serotonin.modbus4j.test;

import java.util.Random;

import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.ProcessImageListener;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.IllegalDataAddressException;
import com.serotonin.modbus4j.exception.ModbusInitException;

public class ListenerTest3 {
    static Random random = new Random();
    static float ir1Value = -100;

    public static void main(String[] args) throws Exception {

        ModbusFactory modbusFactory = new ModbusFactory();
        final ModbusSlaveSet listener = modbusFactory.createTcpSlave(false);

        // Add a few slave process images to the listener.
        listener.addProcessImage(getModscanProcessImage(1));
        listener.addProcessImage(getModscanProcessImage(2));
        listener.addProcessImage(getModscanProcessImage(3));
        listener.addProcessImage(getModscanProcessImage(5));
        listener.addProcessImage(getModscanProcessImage(9));

        // When the "listener" is started it will use the current thread to run. So, if an exception is not thrown
        // (and we hope it won't be), the method call will not return. Therefore, we start the listener in a separate
        // thread so that we can use this thread to modify the values.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.start();
                }
                catch (ModbusInitException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            synchronized (listener) {
                listener.wait(200);
            }

            for (ProcessImage processImage : listener.getProcessImages())
                updateProcessImage((BasicProcessImage) processImage);
        }
    }

    static void updateProcessImage(BasicProcessImage processImage) throws IllegalDataAddressException {
        processImage.setInput(10, !processImage.getInput(10));
        processImage.setInput(13, !processImage.getInput(13));

        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 20, DataType.FOUR_BYTE_FLOAT, ir1Value += 0.01);

        short hr1Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD)
                .shortValue();
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD, hr1Value + 1);
    }

    static class BasicProcessImageListener implements ProcessImageListener {
        @Override
        public void coilWrite(int offset, boolean oldValue, boolean newValue) {
            System.out.println("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
        }

        @Override
        public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
            // Add a small delay to the processing.
            //            try {
            //                Thread.sleep(500);
            //            }
            //            catch (InterruptedException e) {
            //                // no op
            //            }
            System.out.println("HR at " + offset + " was set from " + oldValue + " to " + newValue);
        }
    }

    static BasicProcessImage getModscanProcessImage(int slaveId) {
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        //processImage.setAllowInvalidAddress(true);
        processImage.setInvalidAddressValue(Short.MIN_VALUE);

        processImage.setRegisterDescription(1, SetDescriptionsByteConvertion.createByteArray(2, 3, 1, 1, "", "Z1_AL", "Protection"));
        processImage.setRegisterDescription(2, SetDescriptionsByteConvertion.createByteArray(2, 4, 1, 1, "", "Z1_A_AL", "Sum alarm"));
        processImage.setRegisterDescription(3, SetDescriptionsByteConvertion.createByteArray(2, 5, 1, 1, "", "Z1_B_AL", "Sum alarm"));
        processImage.setRegisterDescription(4, SetDescriptionsByteConvertion.createByteArray(2, 15, 1, 1, "", "Z1_PC0_CMD", "Digital output PC0"));
        processImage.setRegisterDescription(5, SetDescriptionsByteConvertion.createByteArray(2, 22, 1, 1, "", "Z1_C1_CMD", "Digital output compressor 1"));
        processImage.setRegisterDescription(6, SetDescriptionsByteConvertion.createByteArray(2, 23, 1, 1, "", "Z1_C2_CMD", "Digital output compressor 2"));
        processImage.setRegisterDescription(7, SetDescriptionsByteConvertion.createByteArray(4, 11, 20, -10, "C", "Z1_T0_PV", "T0 sensor"));
        processImage.setRegisterDescription(8, SetDescriptionsByteConvertion.createByteArray(4, 12, 20, -10, "C", "Z1_TL1_PV", "TL1 sensor"));
        processImage.setRegisterDescription(9, SetDescriptionsByteConvertion.createByteArray(4, 13, 20, -10, "C", "Z1_TL1_PV1", "TL1 sensor"));
        processImage.setRegisterDescription(10, SetDescriptionsByteConvertion.createByteArray(4, 17, 20, -10, "C", "Z1_TC0_PV", "TC0 sensor"));
        processImage.setRegisterDescription(11, SetDescriptionsByteConvertion.createByteArray(4, 18, 20, -10, "C", "Z1_TB0_PV", "TB0 sensor"));
        processImage.setRegisterDescription(12, SetDescriptionsByteConvertion.createByteArray(4, 19, 20, -10, "C", "Z1_TB1_PV", "TB1 sensor"));
        processImage.setRegisterDescription(13, SetDescriptionsByteConvertion.createByteArray(4, 23, 20, -10, "C", "Z1_T0_CSP", "T0 setpoint"));
        processImage.setRegisterDescription(14, SetDescriptionsByteConvertion.createByteArray(4, 24, 20, -10, "C", "Z1_T0_X1", "x-coordinate 1"));
        processImage.setRegisterDescription(15, SetDescriptionsByteConvertion.createByteArray(4, 25, 20, -10, "C", "Z1_T0_X2", "x-coordinate 2"));
        processImage.setRegisterDescription(16, SetDescriptionsByteConvertion.createByteArray(4, 26, 20, -10, "C", "Z1_T0_X3", "x-coordinate 3"));
        processImage.setRegisterDescription(17, SetDescriptionsByteConvertion.createByteArray(4, 27, 20, -10, "C", "Z1_T0_X4", "x-coordinate 4"));
        processImage.setRegisterDescription(18, SetDescriptionsByteConvertion.createByteArray(4, 28, 20, -10, "C", "Z1_T0_X5", "x-coordinate 5"));
        processImage.setRegisterDescription(19, SetDescriptionsByteConvertion.createByteArray(4, 29, 20, -10, "C", "Z1_T0_X6", "x-coordinate 6"));
        processImage.setRegisterDescription(20, SetDescriptionsByteConvertion.createByteArray(4, 30, 20, -10, "C", "Z1_T0_X7", "x-coordinate 7"));
        processImage.setRegisterDescription(21, SetDescriptionsByteConvertion.createByteArray(4, 31, 20, -10, "C", "Z1_T0_X8", "x-coordinate 8"));
        processImage.setRegisterDescription(22, SetDescriptionsByteConvertion.createByteArray(4, 32, 20, -10, "C", "Z1_T0_X9", "x-coordinate 9"));
        processImage.setRegisterDescription(23, SetDescriptionsByteConvertion.createByteArray(4, 33, 20, -10, "C", "Z1_T0_X10", "x-coordinate 10"));
        processImage.setRegisterDescription(24, SetDescriptionsByteConvertion.createByteArray(4, 34, 20, -10, "C", "Z1_T0_X11", "x-coordinate 11"));
        processImage.setRegisterDescription(25, SetDescriptionsByteConvertion.createByteArray(4, 35, 20, -10, "C", "Z1_T0_X12", "x-coordinate 12"));
        processImage.setRegisterDescription(26, SetDescriptionsByteConvertion.createByteArray(4, 79, 20, -10, "C", "Z1_TC2_PV", "TC2 sensor"));
        processImage.setRegisterDescription(27, SetDescriptionsByteConvertion.createByteArray(4, 38, 20, -10, "%", "Z1_EE0_OP", "Additional heat valve"));
        processImage.setRegisterDescription(28, SetDescriptionsByteConvertion.createByteArray(4, 151, 8, 1, "", "Z1_Alarm_1_AL", "Oper. error all PC1"));
        processImage.setRegisterDescription(29, SetDescriptionsByteConvertion.createByteArray(4, 152, 8, 1, "", "Z1_Alarm_2_AL", "Oper. error compr. and add. heat"));
        processImage.setRegisterDescription(30, SetDescriptionsByteConvertion.createByteArray(4, 153, 8, 1, "", "Z1_Alarm_3_AL", "Failure on sensor T0 and TC2"));
        processImage.setRegisterDescription(31, SetDescriptionsByteConvertion.createByteArray(4, 154, 8, 1, "", "Z1_Alarm_4_AL", "Sensor error TW4 DHW flowtemp"));
        processImage.setRegisterDescription(32, SetDescriptionsByteConvertion.createByteArray(4, 155, 8, 1, "", "Z1_Alarm_5_AL", "Failure PC4 Heating water pump"));
        processImage.setRegisterDescription(33, SetDescriptionsByteConvertion.createByteArray(4, 172, 8, 1, "", "Z1_C1_AL", "Oper. error compressor 1"));
        processImage.setRegisterDescription(34, SetDescriptionsByteConvertion.createByteArray(4, 173, 8, 1, "", "Z1_C2_AL", "Oper. error compressor 2"));
        processImage.setRegisterDescription(35, SetDescriptionsByteConvertion.createByteArray(4, 174, 8, 1, "", "Z1_PB3_AL", "Operating error PB3"));
        processImage.setRegisterDescription(36, SetDescriptionsByteConvertion.createByteArray(4, 176, 8, 1, "", "Z1_HP_AL", "Tripped high pressure switch"));
        processImage.setRegisterDescription(37, SetDescriptionsByteConvertion.createByteArray(4, 177, 9, 1, "", "Z1_PC0_AL", "Operating error PC0"));
        processImage.setRegisterDescription(38, SetDescriptionsByteConvertion.createByteArray(4, 180, 10, 1, "", "Z1_C1_AL1", "Compressor 1 does not start"));
        processImage.setRegisterDescription(39, SetDescriptionsByteConvertion.createByteArray(4, 181, 11, 1, "", "Z1_C2_AL1", "Compressor 2 does not start"));
        processImage.setRegisterDescription(40, SetDescriptionsByteConvertion.createByteArray(4, 182, 12, 1, "", "Z1_JR1_HAL", "High pressure JR1"));
        processImage.setRegisterDescription(41, SetDescriptionsByteConvertion.createByteArray(4, 183, 13, 1, "", "Z1_JR1_LAL", "Low pressure JR1"));
        processImage.setRegisterDescription(42, SetDescriptionsByteConvertion.createByteArray(4, 184, 14, 1, "", "Z1_TB0_LAL", "Low temperature TB0"));
        processImage.setRegisterDescription(43, SetDescriptionsByteConvertion.createByteArray(4, 185, 15, 1, "", "Z1_TB1_LAL", "Low temperature TB1"));
        processImage.setRegisterDescription(44, SetDescriptionsByteConvertion.createByteArray(4, 186, 7, 1, "", "Z1_TB0_HAL", "High temperature TB0"));
        processImage.setRegisterDescription(45, SetDescriptionsByteConvertion.createByteArray(4, 187, 6, 1, "", "Z1_TB1_HAL", "High temperature TB1"));
        processImage.setRegisterDescription(46, SetDescriptionsByteConvertion.createByteArray(4, 188, 5, 1, "", "Z1_TR6_HAL", "High temperature TR6"));
        processImage.setRegisterDescription(47, SetDescriptionsByteConvertion.createByteArray(4, 189, 4, 1, "", "Z1_TR7_HAL", "High temperature TR7"));
        processImage.setRegisterDescription(48, SetDescriptionsByteConvertion.createByteArray(4, 190, 3, 1, "", "Z1_TC1_HAL", "High temperature TC1"));
        processImage.setRegisterDescription(49, SetDescriptionsByteConvertion.createByteArray(4, 190, 2, 1, "", "Z1_TC0_HAL", "High temperature TC0"));
        processImage.setRegisterDescription(50, SetDescriptionsByteConvertion.createByteArray(4, 428, 20, -10, "%", "Z1_PC0_OP", "Current speed PC0"));
        processImage.setRegisterDescription(51, SetDescriptionsByteConvertion.createByteArray(4, 429, 20, -10, "%", "Z1_PB3_OP", "Current speed PB3"));
        processImage.setRegisterDescription(52, SetDescriptionsByteConvertion.createByteArray(4, 429, 20, -10, "C", "Z1_PB3_CMD", "Running indication PB3 (on OP)"));
        processImage.setRegisterDescription(53, SetDescriptionsByteConvertion.createByteArray(4, 79, 20, -10, "C", "Z1_TC3_PV", "TC3 sensor"));
        processImage.setRegisterDescription(54, SetDescriptionsByteConvertion.createByteArray(3, 5, 20, -10, "C", "Z1_T0_Y1", "y-coordinate 1"));
        processImage.setRegisterDescription(55, SetDescriptionsByteConvertion.createByteArray(3, 6, 20, -10, "C", "Z1_T0_Y2", "y-coordinate 2"));
        processImage.setRegisterDescription(56, SetDescriptionsByteConvertion.createByteArray(3, 7, 20, -10, "C", "Z1_T0_Y3", "y-coordinate 3"));
        processImage.setRegisterDescription(57, SetDescriptionsByteConvertion.createByteArray(3, 8, 20, -10, "C", "Z1_T0_Y4", "y-coordinate 4"));
        processImage.setRegisterDescription(58, SetDescriptionsByteConvertion.createByteArray(3, 9, 20, -10, "C", "Z1_T0_Y5", "y-coordinate 5"));
        processImage.setRegisterDescription(59, SetDescriptionsByteConvertion.createByteArray(3, 10, 20, -10, "C", "Z1_T0_Y6", "y-coordinate 6"));
        processImage.setRegisterDescription(60, SetDescriptionsByteConvertion.createByteArray(3, 11, 20, -10, "C", "Z1_T0_Y7", "y-coordinate 7"));
        processImage.setRegisterDescription(61, SetDescriptionsByteConvertion.createByteArray(3, 12, 20, -10, "C", "Z1_T0_Y8", "y-coordinate 8"));
        processImage.setRegisterDescription(62, SetDescriptionsByteConvertion.createByteArray(3, 13, 20, -10, "C", "Z1_T0_Y9", "y-coordinate 9"));
        processImage.setRegisterDescription(63, SetDescriptionsByteConvertion.createByteArray(3, 14, 20, -10, "C", "Z1_T0_Y10", "y-coordinate 10"));
        processImage.setRegisterDescription(64, SetDescriptionsByteConvertion.createByteArray(3, 15, 20, -10, "C", "Z1_T0_Y11", "y-coordinate 11"));
        processImage.setRegisterDescription(65, SetDescriptionsByteConvertion.createByteArray(3, 16, 20, -10, "C", "Z1_T0_Y12", "y-coordinate 12"));
        
        
        processImage.setCoil(10, true);
        processImage.setCoil(11, false);
        processImage.setCoil(12, true);
        processImage.setCoil(13, true);
        processImage.setCoil(14, false);

        processImage.setInput(10, false);
        processImage.setInput(11, false);
        processImage.setInput(12, true);
        processImage.setInput(13, false);
        processImage.setInput(14, true);

        processImage.setBinary(16, true);
        processImage.setBinary(10016, true);

        processImage.setHoldingRegister(10, (short) 1);
        processImage.setHoldingRegister(11, (short) 10);
        processImage.setHoldingRegister(12, (short) 100);
        processImage.setHoldingRegister(13, (short) 1000);
        processImage.setHoldingRegister(14, (short) 10000);

        processImage.setInputRegister(10, (short) 10000);
        processImage.setInputRegister(11, (short) 1000);
        processImage.setInputRegister(12, (short) 100);
        processImage.setInputRegister(13, (short) 10);
        processImage.setInputRegister(14, (short) 1);

        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 3, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 14, true);

        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 15, true);

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 16, DataType.TWO_BYTE_INT_SIGNED, new Integer(-1968));
        processImage
                .setNumeric(RegisterRange.HOLDING_REGISTER, 17, DataType.FOUR_BYTE_INT_SIGNED, new Long(-123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 19, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 21, DataType.FOUR_BYTE_FLOAT, new Float(1968.1968));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 23, DataType.EIGHT_BYTE_INT_SIGNED,
                new Long(-123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 27, DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT, new Double(1968.1968));

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD, new Short((short) 1234));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 81, DataType.FOUR_BYTE_BCD, new Integer(12345678));

        processImage.setString(RegisterRange.HOLDING_REGISTER, 100, DataType.VARCHAR, 20, "Serotonin Software");

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12288, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(
                1968.1968));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12290, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(
                -1968.1968));

        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 16, DataType.TWO_BYTE_INT_UNSIGNED, new Integer(0xfff0));
        processImage
                .setNumeric(RegisterRange.INPUT_REGISTER, 17, DataType.FOUR_BYTE_INT_UNSIGNED, new Long(-123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 19, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 21, DataType.FOUR_BYTE_FLOAT_SWAPPED,
                new Float(1968.1968));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 23, DataType.EIGHT_BYTE_INT_UNSIGNED,
                new Long(-123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 27, DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT_SWAPPED, new Double(
                1968.1968));

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 50, DataType.EIGHT_BYTE_INT_UNSIGNED, 0);

        processImage.setString(RegisterRange.INPUT_REGISTER, 100, DataType.CHAR, 15, "Software de la Serotonin");

        processImage.setExceptionStatus((byte) 151);

        // Add an image listener.
        processImage.addListener(new BasicProcessImageListener());

        return processImage;
    }
    
}
