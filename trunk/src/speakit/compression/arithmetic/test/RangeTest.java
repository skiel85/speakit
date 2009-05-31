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
public class RangeTest {
	
	private Range	sut;
	private String	initialRoof;
	private String	initialFloor;

	private static final char	_1	= '1';
	private static final char	_0	= '0';

	@Before
	public void setUp() throws Exception {
		initialRoof = "1111";
		initialFloor = "0000";
		sut = new Range((byte) 4);
	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test
	public void testInitialation() {
		Assert.assertEquals(initialFloor, sut.getFloor());
		Assert.assertEquals(initialRoof, sut.getRoof());
		Assert.assertEquals("",sut.flush());
	}
	
	@Test
	public void testGetSetRoofFloor() {
		sut.setRoof(initialRoof);
		sut.setFloor(initialFloor);
		Assert.assertEquals(initialRoof, sut.getRoof());
		Assert.assertEquals(initialFloor, sut.getFloor());
	}

	@Test
	public void testGetterDoesNotGiveAReference() {
		String currentRoof = sut.getRoof();
		Assert.assertEquals(_1, sut.getRoof().charAt(1));
		currentRoof="asdasd";
		Assert.assertEquals(_1, sut.getRoof().charAt(1));
		
		String currentfloor = sut.getFloor();
		Assert.assertEquals(_0, sut.getFloor().charAt(1));
		currentfloor="asdasd";
		Assert.assertEquals(_0, sut.getFloor().charAt(1));
	}
	
	@Test
	public void testOverflow1() {
		String overflowRoof = "0011" ;
		sut.setRoof(overflowRoof);//aca deberia simplificarse el overflow
		sut.simplify();
		Assert.assertEquals(initialRoof, sut.getRoof());//deberia quedar igual que antes
		Assert.assertEquals("00", sut.flush());		
	}
	
	@Test
	public void testEmitEnding() {
		Assert.assertEquals("", sut.flush()); 
		sut.emitEnding();
		Assert.assertEquals(initialFloor, sut.flush());
	} 
	
	@Test
	public void testHasBuffer() {
		Assert.assertEquals("", sut.flush());
		sut.setRoof("0111");
		sut.setFloor("0101");
		sut.simplify();
		//overflow = "01"
		//floor = "0100"
		Assert.assertEquals("1111", sut.getRoof());
		Assert.assertEquals("0100", sut.getFloor());
		sut.emitEnding();
		Assert.assertEquals("01"+"0100", sut.flush());
	} 
	
	@Test
	public void testFlushClearsBuffer() {
		String overflowRoof = "0011" ;
		sut.setRoof(overflowRoof);
		sut.simplify();//aca deberia resolverse el overflow
		Assert.assertEquals(initialRoof, sut.getRoof());//deberia quedar igual que antes
		Assert.assertEquals("00", sut.flush());
		Assert.assertEquals("", sut.flush());	
	}
	
	@Test
	public void testUnderflow(){
		sut.setRoof( "1010");
		sut.setFloor("0101");
		sut.simplify();
		Assert.assertEquals("1101", sut.getRoof());
		Assert.assertEquals("0010", sut.getFloor());
		Assert.assertEquals("", sut.flush());
	}
	
	@Test
	public void testUnderflowEmision(){
		sut.setRoof( "1010");
		sut.setFloor("0101");
		sut.simplify();//esto provoca un underflow de 1
		sut.setRoof( "0011");
		sut.setFloor("0000");
		sut.simplify();//esto provoca un overflow de 00 		
		Assert.assertEquals("010", sut.flush());//emite el overflow y el underflow
		sut.setRoof( "0011");
		sut.setFloor("0000");
		sut.simplify();//esto provoca un overflow de 00 sin emision de underflow
		Assert.assertEquals("00", sut.flush());
	}
	
	@Ignore
	@Test
	public void testWithRemainingUnderflowClose() { 
	}
	
	//simplifyTwice
	//varios overflows
	//mezclar overflow y underflow
}
