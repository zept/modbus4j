package com.serotonin.modbus4j.test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SetDescriptionsByteConvertion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] b = createByteArray(3, 23, 43, 1000, "°C", "test", "test");
		for (byte b2 : b) {
			System.out.println(b2);
		}
	}
	
    static byte[] createByteArray(int functionCode, int address, int format, int scaling, String unit, String tagName, String description) {

   
//		TODO: Validate input. Currently sets characters not present in ASCII table to ?.
    	
    	byte[] unitByte = unit.getBytes(StandardCharsets.US_ASCII);
    	byte[] tagByte = tagName.getBytes(StandardCharsets.US_ASCII);
    	byte[] descByte = description.getBytes(StandardCharsets.US_ASCII);

    	ByteBuffer buffer = ByteBuffer.allocate(253);
    	buffer.put((byte) functionCode); 	// 1-byte
    	buffer.putShort((short) address);	// 2-byte
    	buffer.put((byte) format);			// 1-byte
    	buffer.putShort((short) scaling);	// 2-byte
    	buffer.put(unitByte);				// variable length
    	buffer.put((byte) 124);				// delimiter between tag-name and description
    	buffer.put(tagByte);				// variable length
    	buffer.put((byte) 124);				// delimiter between tag-name and description
    	buffer.put(descByte);				// variable length
    	
    	// Trim, rewind and copy array.
    	byte[] data = new byte[buffer.position()];
    	buffer.rewind();
    	buffer.get(data);
    	
    	return data;
    }
    

}
