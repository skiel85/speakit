/**
 * 
 */
package speakit.compression.arithmetic.test;

import java.io.IOException;

import speakit.compression.arithmetic.BitWriter;

public class MockBitWriter implements BitWriter{
	 StringBuffer buffer=new StringBuffer();
	 
	@Override
	public void write(String bit) throws IOException {
		buffer.append(bit);
	}
	
	public String getWritten(){
		return buffer.toString();
	}
	
}