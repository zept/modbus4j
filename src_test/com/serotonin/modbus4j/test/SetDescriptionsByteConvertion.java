package com.serotonin.modbus4j.test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SetDescriptionsByteConvertion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		createByteArray(3, 23, 6, 43, 1000, "test", "test");
	}
	
    static byte[] createByteArray(int functionCode, int address, int unit, int format, int scaling, String tagName, String description) {

   
//		TODO: Validate input.
    	
    	byte[] tag = tagName.getBytes(StandardCharsets.US_ASCII);
    	byte[] desc = description.getBytes(StandardCharsets.US_ASCII);

    	ByteBuffer buffer = ByteBuffer.allocate(253);
    	buffer.put((byte) functionCode); 	// 1-byte
    	buffer.putShort((short) address);	// 2-byte
    	buffer.put((byte) unit);			// 1-byte
    	buffer.put((byte) format);			// 1-byte
    	buffer.putShort((short) scaling);	// 2-byte
    	buffer.put(tag);					// variable length
    	buffer.put(desc);					// variable length
    	
    	// Trim, rewind and copy array.
    	byte[] data = new byte[buffer.position()];
    	buffer.rewind();
    	buffer.get(data);
    	
    	return data;
    }
    

}
