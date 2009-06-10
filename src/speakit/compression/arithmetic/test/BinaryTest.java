package speakit.compression.arithmetic.test;


import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import speakit.compression.arithmetic.Binary;
import speakit.compression.arithmetic.StringBitReader;


public class BinaryTest { 

 	@Test
	public void testConvertToInt() {
		Assert.assertEquals(163, Binary.bitStringToNumber("10100011"));
	}
 	
 	@Test
	public void testShift() throws IOException {
 		StringBitReader reader = new StringBitReader("101000111110110000000");
		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("00011111", sut.shiftLeft(3, 0, reader).getBits());
	} 
}

