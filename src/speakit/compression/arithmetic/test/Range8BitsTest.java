package speakit.compression.arithmetic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.Range;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class Range8BitsTest {

	private Range				sut;
	private String				initialRoof;
	private String				initialFloor;

	private static final char	_1	= '1';
	private static final char	_0	= '0';

	@Before
	public void setUp() throws Exception {
		sut = new Range((byte) 8);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSetBoundsShorterThanPrecicion() {
		sut.setBounds("0000", "1111");
		Assert.assertEquals("00000000", sut.getFloor());
		Assert.assertEquals("11111111", sut.getRoof());
		Assert.assertEquals("0000", sut.flush());
	}

	@Test
	public void testCompressFirstTwoCharsOfNEUQUENBySkiel() {
		//E=2
		//N=2
		//Q=1
		//U=2
		//Zoom a la N
		sut.zoomIn( 2/7F, 2/7F);
		Assert.assertEquals("", sut.flush());
		Assert.assertEquals(1, sut.getUnderflowCount());
		Assert.assertEquals("00010010", sut.getFloor());
		Assert.assertEquals("10100011", sut.getRoof());
		Assert.assertEquals(18, sut.getNumericFloor());
		Assert.assertEquals(163, sut.getNumericRoof());
		
		//Zoom a la E
		sut.zoomIn( 0F, 2/7F);
		Assert.assertEquals("01001000", sut.getFloor());
		Assert.assertEquals("11101111", sut.getRoof());
		Assert.assertEquals(72, sut.getNumericFloor());
		Assert.assertEquals(239, sut.getNumericRoof());
		Assert.assertEquals("010", sut.flush());
		Assert.assertEquals(0, sut.getUnderflowCount());
	}
	
	@Test
	public void testCompressNEUQUENByNahuel() {
		//E=2
		//N=2
		//Q=1
		//U=2
		//Zoom a la N
		sut.zoomIn( 3/7F, 2/7F);
		Assert.assertEquals("", sut.flush());
		Assert.assertEquals(1, sut.getUnderflowCount());
		Assert.assertEquals("01011100", sut.getFloor());Assert.assertEquals(92, sut.getNumericFloor());
		Assert.assertEquals("11101101", sut.getRoof());Assert.assertEquals(237, sut.getNumericRoof());
		
		//Zoom a la E
		sut.zoomIn( 5/7F, 2/7F);
		Assert.assertEquals("101", sut.flush());
		Assert.assertEquals(0, sut.getUnderflowCount());		
		Assert.assertEquals("00010000", sut.getFloor());Assert.assertEquals(16, sut.getNumericFloor());
		Assert.assertEquals("10110111", sut.getRoof());Assert.assertEquals(183, sut.getNumericRoof());
		
		//Zoom a la U
		sut.zoomIn( 0/7F, 2/7F);
		Assert.assertEquals("00", sut.flush());
		Assert.assertEquals(0, sut.getUnderflowCount());		
		Assert.assertEquals("01000000", sut.getFloor());Assert.assertEquals(64, sut.getNumericFloor());
		Assert.assertEquals("11111111", sut.getRoof());Assert.assertEquals(255, sut.getNumericRoof());
		
		//Zoom a la Q
		sut.zoomIn( 2/7F, 1/7F);
		Assert.assertEquals("", sut.flush());
		Assert.assertEquals(2, sut.getUnderflowCount());		
		Assert.assertEquals("01011100", sut.getFloor());Assert.assertEquals(92, sut.getNumericFloor());
		Assert.assertEquals("11000111", sut.getRoof());Assert.assertEquals(199, sut.getNumericRoof());
		
		//Zoom a la U
		sut.zoomIn( 0/7F, 2/7F);
		Assert.assertEquals("0111", sut.flush());
		Assert.assertEquals(0, sut.getUnderflowCount());		
		Assert.assertEquals("01110000", sut.getFloor());Assert.assertEquals(112, sut.getNumericFloor());
		Assert.assertEquals("11101011", sut.getRoof());Assert.assertEquals(235, sut.getNumericRoof());
		
		//Zoom a la E
		sut.zoomIn( 5/7F, 2/7F);
		Assert.assertEquals("11", sut.flush());
		Assert.assertEquals(0, sut.getUnderflowCount());		
		Assert.assertEquals("00100100", sut.getFloor());Assert.assertEquals(36, sut.getNumericFloor());
		Assert.assertEquals("10101111", sut.getRoof());Assert.assertEquals(175, sut.getNumericRoof());
		
		//Zoom a la N
		sut.zoomIn( 3/7F, 2/7F);
		Assert.assertEquals("", sut.flush());
		Assert.assertEquals(2, sut.getUnderflowCount());		
		Assert.assertEquals("00000000", sut.getFloor());Assert.assertEquals(0, sut.getNumericFloor());
		Assert.assertEquals("10011111", sut.getRoof());Assert.assertEquals(159, sut.getNumericRoof());
		
		//Emito el final del archivo
		sut.emitEnding();
		Assert.assertEquals("0110000000", sut.flush());
		
		
	}
}
