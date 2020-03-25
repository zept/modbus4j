/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;
import com.serotonin.modbus4j.locator.NumericLocator;
import com.serotonin.modbus4j.locator.StringLocator;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadResponse;


/**
 * @author Matthew Lohbihler
 */
public class MasterTest3 {
	
    public static void main(String[] args) throws Exception {
    	
        IpParameters ipParameters = new IpParameters();
        ipParameters.setHost("localhost");
        ipParameters.setPort(502);
        ipParameters.setEncapsulated(false);

        ModbusFactory modbusFactory = new ModbusFactory();
        // ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, true);
        ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, false);
        master.setTimeout(8000);
        master.setRetries(0);
        master.init();

        //NumericLocator el = new NumericLocator(1, RegisterRange.REGISTER_DESCRIPTION, 2, DataType.VARIABLE_LENGTH_DESCRIPTION);
        //NumericLocator el = new NumericLocator(1,RegisterRange.REGISTER_DESCRIPTION, 2, DataType.VARIABLE_LENGTH_DESCRIPTION);
        
        printData(master.getSlaveDescription(1, 1));
        printData(master.getSlaveDescription(1, 2));
        printData(master.getSlaveDescription(1, 3));
        printData(master.getSlaveDescription(1, 4));

        //mr.createModbusResponse(queue);
        //ReadHoldingRegistersRequest test = new ReadHoldingRegistersRequest(1, 12, 1);
        //System.out.println(test.toString());
        //System.out.println("el: " + master.getValue());
        //System.out.println("el: " + master.testSlaveNode(1));
        master.destroy();
    }
    
    static void printData(ModbusResponse response) {
    	ReadResponse message = (ReadResponse) response;
        System.out.println("length is: " + message.getData().length);

        StringBuilder sb = new StringBuilder();
        for (byte b : message.getData()) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb.toString());
    }
}
