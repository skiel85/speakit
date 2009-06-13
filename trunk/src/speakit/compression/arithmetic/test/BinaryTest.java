package speakit.compression.arithmetic.test;


import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import speakit.compression.arithmetic.Binary;
import speakit.compression.arithmetic.StringBitReader;


public class BinaryTest { 

 	@Test
	public void testConvertToLong() {
		Assert.assertEquals(163, Binary.bitStringToNumber("10100011"));
	}
 	
 	@Test
	public void testConvertToLongExtremeCase() {
		Assert.assertEquals(65580, Binary.bitStringToNumber("10000000000101100"));
	}
 	
 	@Test
	public void testConvertToBinaryExtremeCase() {
		Assert.assertEquals("10000000000101100", Binary.numberToBinary(65580));
	}
 	
 	@Test
	public void testShift() throws IOException {
 		StringBitReader reader = new StringBitReader("101000111110110000000");
		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("00011111", sut.shiftLeft(3, 0, reader).getBits());
	}
 	
 	@Ignore
 	@Test
	public void testShiftMoreBitsThanWindowLength() throws IOException {
 		StringBitReader reader = new StringBitReader("101000111110110000000");
		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("10110000", sut.shiftLeft(10, 0, reader).getBits());
	}
 	
 	@Ignore
 	@Test
	public void testShiftMoreBitsThanWindowLengthBiggerTest() throws IOException {
 		StringBitReader reader = new StringBitReader("101000111110110000000010010100111010010100101");
		Binary sut = Binary.createFromReader(reader, 16) ;
		Assert.assertEquals("1001010011101001", sut.shiftLeft(22, 0, reader).getBits());
	}
 	
 	@Test
	public void testShiftNotStartingAtBeginning() throws IOException {
 		StringBitReader reader = new StringBitReader("101000111110110000000");
		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("10111110", sut.shiftLeft(4, 2, reader).getBits());
	}
 	
 	@Test
	public void testShiftAllWindow() throws IOException {
 		StringBitReader reader = new StringBitReader("101000111110110000000");
		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("11101100", sut.shiftLeft(8, 0, reader).getBits());
	}
 	
 	@Test
	public void testShiftAllWindowFromMiddle() throws IOException {
 		StringBitReader reader = new StringBitReader("101000111110110000000");
		Binary sut = Binary.createFromReader(reader, 8) ;
		Assert.assertEquals("10101110", sut.shiftLeft(4, 4, reader).getBits());
	}
}

