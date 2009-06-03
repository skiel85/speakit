package speakit.compression.arithmetic.test;


import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import speakit.compression.arithmetic.Binary;


public class BinaryTest { 

 	@Test
	public void testConvertToInt() {
		Assert.assertEquals(163, Binary.bitStringToInt("10100011"));
	}
 	
 	@Test
	public void testShift() throws IOException {
 		StringReader reader = new StringReader("101000111110110000000");
 		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("00011111", sut.shiftLeft(3, 0, reader).getBits());
	} 
}

