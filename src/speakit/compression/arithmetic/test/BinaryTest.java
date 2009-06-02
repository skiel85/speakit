package speakit.compression.arithmetic.test;


import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.Binary;
import speakit.compression.arithmetic.Range;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class BinaryTest { 

 	@Test
	public void testConvertToInt() {
		Assert.assertEquals(163, Binary.bitStringToInt("10100011"));
	} 
 	
 	//"101000111110110000000"
 	//currentWindow=new Binary("00011111");
	//currentWindow=currentWindow.shiftRight(flush.length(),0, reader);
 	
 	@Test
	public void testShift() throws IOException {
 		StringReader reader = new StringReader("101000111110110000000");
 		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("00011111", sut.shiftLeft(3, 0, reader).getBits());
	} 
}

