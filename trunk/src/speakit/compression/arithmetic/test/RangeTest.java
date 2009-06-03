package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.Range;
public class RangeTest {

	private Range				sut;
	private String				initialRoof;
	private String				initialFloor;

	private static final char	_1	= '1';
	private static final char	_0	= '0';

	@Before
	public void setUp() throws Exception {
		initialRoof = "1111";
		initialFloor = "0000";
		sut = new Range(4);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testInitialation() {
		Assert.assertEquals(initialFloor, sut.getFloor());
		Assert.assertEquals(initialRoof, sut.getRoof());
		Assert.assertEquals("", sut.flush());
	}

	@Test
	public void testGetSetRoofFloor() throws IOException {
		sut.setBounds(initialFloor, initialRoof);
		Assert.assertEquals(initialRoof, sut.getRoof());
		Assert.assertEquals(initialFloor, sut.getFloor());
	}

	@Test
	public void testGetterDoesNotGiveAReference() {
		String currentRoof = sut.getRoof();
		Assert.assertEquals(_1, sut.getRoof().charAt(1));
		currentRoof = "asdasd";
		Assert.assertEquals(_1, sut.getRoof().charAt(1));

		String currentfloor = sut.getFloor();
		Assert.assertEquals(_0, sut.getFloor().charAt(1));
		currentfloor = "asdasd";
		Assert.assertEquals(_0, sut.getFloor().charAt(1));
	}

	@Test
	public void testOverflow1() throws IOException {
		String overflowRoof = "0011";
		sut.setBounds(initialFloor, overflowRoof);// aca deberia simplificarse
													// el overflow
		Assert.assertEquals(initialRoof, sut.getRoof());// deberia quedar igual
														// que antes
		Assert.assertEquals("00", sut.flush());
	}

	@Test
	public void testEmitEnding() {
		Assert.assertEquals("", sut.flush());
		sut.emitEnding();
		Assert.assertEquals(initialFloor, sut.flush());
	}

	@Test
	public void testHasBuffer() throws IOException {
		Assert.assertEquals("", sut.flush());
		sut.setBounds("0101", "0111");// overflow = "01", floor = "0100"
		Assert.assertEquals("1111", sut.getRoof());
		Assert.assertEquals("0100", sut.getFloor());
		sut.emitEnding();
		Assert.assertEquals("01" + "0100", sut.flush());
	}

	@Test
	public void testFlushClearsBuffer() throws IOException {
		String overflowRoof = "0011";
		sut.setBounds(initialFloor, overflowRoof);// aca deberia resolverse el
													// overflow
		Assert.assertEquals(initialRoof, sut.getRoof());// deberia quedar igual
														// que antes
		Assert.assertEquals("00", sut.flush());
		Assert.assertEquals("", sut.flush());
	}

	@Test
	public void testUnderflow() throws IOException {
		sut.setBounds("0101", "1010");
		Assert.assertEquals("1101", sut.getRoof());
		Assert.assertEquals("0010", sut.getFloor());
		Assert.assertEquals("", sut.flush());
	}

	@Test
	public void testUnderflowEmision() throws IOException {
		sut.setBounds("0101", "1010");// esto provoca un underflow de 1
		sut.setBounds("0000", "0011");// esto provoca un overflow de 00
		Assert.assertEquals("010", sut.flush());// emite el overflow y el
												// underflow
		sut.setBounds("0000", "0011");// esto provoca un overflow de 00 sin
										// emision de underflow
		Assert.assertEquals("00", sut.flush());
	}

	@Test
	public void testFlushOnlyClearUnderflowCountWhenFlushesAtLeastOneBit() throws IOException {
		sut.setBounds("0101", "1010");// esto provoca un underflow de 1
		Assert.assertEquals(1, sut.getUnderflowCount());
		Assert.assertEquals("", sut.flush());// emite el overflow y el underflow
		Assert.assertEquals(1, sut.getUnderflowCount());
		sut.setBounds("0000", "0011");// esto provoca un overflow de 00
		sut.flush();// deberia limpiar el contador de underflow
		Assert.assertEquals(0, sut.getUnderflowCount());// deberia estar limpio
	}

	@Test
	public void testCloseWithRemainingUnderflow() throws IOException {
		sut.setBounds("0101", "1010");// esto provoca un underflow de 1
		Assert.assertEquals(1, sut.getUnderflowCount());
		Assert.assertEquals("", sut.flush());// emite el overflow y el underflow
		Assert.assertEquals("0010", sut.getFloor());// emite el overflow y el
													// underflow
		sut.emitEnding();
		Assert.assertEquals("01010", sut.flush());
	}

	@Test
	public void testVariousOverflowsAndUnderflows() throws IOException {
		sut.setBounds("0000", "0011");// esto provoca un overflow de 00
		sut.setBounds("0101", "1010");// esto provoca un underflow de 1
		sut.setBounds("0101", "1010");// esto provoca un underflow de 1
		sut.setBounds("1000", "1011");// esto provoca un overflow de 10, aca el
										// contador de underflow queda en 0
		sut.setBounds("1000", "1011");// esto provoca un overflow de 10
		Assert.assertEquals("00100010", sut.flush());// la emision deberia ser
														// 00-1(00)0-10. El (00)
														// es del underflow
	}

	@Test
	public void testZoomIn() throws IOException {
		sut.setBounds("0000", "1111");
		sut.zoomIn(4 / 17.0, 2 / 17.0);
		// nuevo piso = 0+4/17 * 16=4
		// nuevo techo= 4 - 1 + 16*2/17=5
		Assert.assertEquals("010", sut.flush());
		Assert.assertEquals("0000", sut.getFloor());
		Assert.assertEquals("1111", sut.getRoof());
	}

	@Test
	public void testSetBoundsShorterThanPrecicion() throws IOException {
		sut.setBounds("00", "11");
		Assert.assertEquals("0000", sut.getFloor());
		Assert.assertEquals("1111", sut.getRoof());
		Assert.assertEquals("00", sut.flush());
	}

}
