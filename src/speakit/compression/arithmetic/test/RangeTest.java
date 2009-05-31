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
		sut.setRoof(overflowRoof);//aca deberia simplificarse el overflow
		sut.simplify();
		Assert.assertEquals(initialRoof, sut.getRoof());//deberia quedar igual que antes
		Assert.assertEquals("00", sut.flush());
		Assert.assertEquals("", sut.flush());	
	}
	
	@Ignore
	@Test
	public void testUnderflow() { 
	}
	
	@Ignore
	@Test
	public void testWithRemainingUnderflowClose() { 
	} 
}
