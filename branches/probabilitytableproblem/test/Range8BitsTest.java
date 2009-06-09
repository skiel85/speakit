package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Range;
import speakit.compression.arithmetic.Symbol;

public class Range8BitsTest {

	private Range				sut;
	private String				initialRoof;
	private String				initialFloor;

	private static final char	_1	= '1';
	private static final char	_0	= '0';

	@Before
	public void setUp() throws Exception {
		sut = new Range((int) 8);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSetBoundsShorterThanPrecicion() throws IOException {
		sut.setBounds("0000", "1111");
		Assert.assertEquals("00000000", sut.getFloor());
		Assert.assertEquals("11111111", sut.getRoof());
		Assert.assertEquals("0000", sut.flush());
	}

	@Test
	public void testCompressFirstTwoCharsOfNEUQUENBySkiel() throws IOException {
		// E=2
		// N=2
		// Q=1
		// U=2
		// Zoom a la N
		sut.zoomIn(2 / 7.0, 2 / 7.0);
		Assert.assertEquals("", sut.flush());
		Assert.assertEquals(1, sut.getUnderflowCount());
		Assert.assertEquals("00010010", sut.getFloor());
		Assert.assertEquals("10100011", sut.getRoof());
		Assert.assertEquals(18, sut.getNumericFloor());
		Assert.assertEquals(163, sut.getNumericRoof());

		// Zoom a la E
		sut.zoomIn(0.0, 2 / 7.0);
		Assert.assertEquals("01001000", sut.getFloor());
		Assert.assertEquals("11101111", sut.getRoof());
		Assert.assertEquals(72, sut.getNumericFloor());
		Assert.assertEquals(239, sut.getNumericRoof());
		Assert.assertEquals("010", sut.flush());
		Assert.assertEquals(0, sut.getUnderflowCount());
	}

	@Test
	public void testZoomIn() throws IOException {
		ProbabilityTable table = new ProbabilityTable();
		table.increment(Symbol.getEof(), 1);
		table.increment(new Symbol('E'), 2);
		table.increment(new Symbol('N'), 2);
		table.increment(new Symbol('Q'), 1);
		table.increment(new Symbol('U'), 2);

		String floor = "01111110";
		String roof =  "11111101";
		
		sut.setBounds( floor, roof);

		Assert.assertEquals(floor, sut.getFloor());		
		Assert.assertEquals(roof, sut.getRoof());
		sut.zoomIn(table.getProbabilityUntil(new Symbol('Q')), table.getProbability(new Symbol('Q')));
		Assert.assertEquals("01110000", sut.getFloor());
		Assert.assertEquals("11101111", sut.getRoof()); 
	}
	
	@Test
	public void testZoomIn2() throws IOException {
		ProbabilityTable table = new ProbabilityTable();
		table.increment(Symbol.getEof(), 1);
		table.increment(new Symbol('E'), 2);
		table.increment(new Symbol('N'), 2);
		table.increment(new Symbol('Q'), 1);
		table.increment(new Symbol('U'), 2);

		String floor = "11001110";
		String roof =  "11011101";
		
		sut.setBounds( floor, roof);
		
		Assert.assertEquals("110", sut.flush());
		
		Assert.assertEquals("01110000", sut.getFloor());		
		Assert.assertEquals("11101111", sut.getRoof());
		sut.zoomIn(table.getProbabilityUntil(Symbol.getEof()), table.getProbability(Symbol.getEof()));
		Assert.assertEquals("00000000", sut.getFloor());
		Assert.assertEquals("11111111", sut.getRoof());
		
		sut.emitEnding();
		
		Assert.assertEquals("0111" + "00000000", sut.flush());
		
	}
	@Test
	public void testCompressNEUQUENByNahuel() throws IOException {
		// E=2
		// N=2
		// Q=1
		// U=2
		// Zoom a la N
		sut.zoomIn(3 / 7.0, 2 / 7.0);
		String totalEmision = "";
		totalEmision += sut.flush();
		Assert.assertEquals("", totalEmision);
		Assert.assertEquals(1, sut.getUnderflowCount());
		Assert.assertEquals("01011100", sut.getFloor());
		Assert.assertEquals(92, sut.getNumericFloor());
		Assert.assertEquals("11101101", sut.getRoof());
		Assert.assertEquals(237, sut.getNumericRoof());

		// Zoom a la E
		sut.zoomIn(5 / 7.0, 2 / 7.0);
		totalEmision += sut.flush();
		Assert.assertEquals("101", totalEmision);
		Assert.assertEquals(0, sut.getUnderflowCount());
		Assert.assertEquals("00010000", sut.getFloor());
		Assert.assertEquals(16, sut.getNumericFloor());
		Assert.assertEquals("10110111", sut.getRoof());
		Assert.assertEquals(183, sut.getNumericRoof());

		// Zoom a la U
		sut.zoomIn(0 / 7.0, 2 / 7.0);
		totalEmision += sut.flush();
		Assert.assertEquals("10100", totalEmision);
		Assert.assertEquals(0, sut.getUnderflowCount());
		Assert.assertEquals("01000000", sut.getFloor());
		Assert.assertEquals(64, sut.getNumericFloor());
		Assert.assertEquals("11111111", sut.getRoof());
		Assert.assertEquals(255, sut.getNumericRoof());

		// Zoom a la Q
		sut.zoomIn(2 / 7.0, 1 / 7.0);
		totalEmision += sut.flush();
		Assert.assertEquals("10100", totalEmision);
		Assert.assertEquals(2, sut.getUnderflowCount());
		Assert.assertEquals("01011100", sut.getFloor());
		Assert.assertEquals(92, sut.getNumericFloor());
		Assert.assertEquals("11000111", sut.getRoof());
		Assert.assertEquals(199, sut.getNumericRoof());

		// Zoom a la U
		sut.zoomIn(0 / 7.0, 2 / 7.0);
		totalEmision += sut.flush();
		Assert.assertEquals("101000111", totalEmision);
		Assert.assertEquals(0, sut.getUnderflowCount());
		Assert.assertEquals("01110000", sut.getFloor());
		Assert.assertEquals(112, sut.getNumericFloor());
		Assert.assertEquals("11101011", sut.getRoof());
		Assert.assertEquals(235, sut.getNumericRoof());

		// Zoom a la E
		sut.zoomIn(5 / 7.0, 2 / 7.0);
		totalEmision += sut.flush();
		Assert.assertEquals("10100011111", totalEmision);
		Assert.assertEquals(0, sut.getUnderflowCount());
		Assert.assertEquals("00100100", sut.getFloor());
		Assert.assertEquals(36, sut.getNumericFloor());
		Assert.assertEquals("10101111", sut.getRoof());
		Assert.assertEquals(175, sut.getNumericRoof());

		// Zoom a la N
		sut.zoomIn(3 / 7.0, 2 / 7.0);
		totalEmision += sut.flush();
		Assert.assertEquals("10100011111", totalEmision);
		Assert.assertEquals(2, sut.getUnderflowCount());
		Assert.assertEquals("00000000", sut.getFloor());
		Assert.assertEquals(0, sut.getNumericFloor());
		Assert.assertEquals("10011111", sut.getRoof());
		Assert.assertEquals(159, sut.getNumericRoof());

		// Emito el final del archivo
		sut.emitEnding();
		totalEmision += sut.flush();
		Assert.assertEquals("101000111110110000000", totalEmision);

	}
}
