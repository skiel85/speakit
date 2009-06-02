package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class ArithmeticEncoderTest {
	private ArithmeticEncoder	sut;
	private MockBitWriter	bitWriter;
	@Before
	public void setUp() throws Exception {
		bitWriter = new MockBitWriter();
		sut = new ArithmeticEncoder(bitWriter,8);
	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test
	public void testEncode() throws IOException {
		ProbabilityTable table=new ProbabilityTable();
		table.add(new Symbol('C'), 2);
		table.add(new Symbol('D'), 2);
		table.add(new Symbol('B'), 1);
		table.add(new Symbol('A'), 2);
		table.add(getEof(), 1);
		
		sut.encode(new Symbol('C'), table);
		sut.encode(new Symbol('D'), table);
		sut.encode(new Symbol('A'), table);
		sut.encode(new Symbol('B'), table);
		sut.encode(new Symbol('A'), table);
		sut.encode(new Symbol('D'), table);
		sut.encode(new Symbol('C'), table);
		sut.encode(getEof(), table);
		Assert.assertEquals("10110011101011000000000000",bitWriter.getWritten());
	}

	private Symbol getEof() {
		return ArithmeticEncoder.CreateEof();
	}
	
	@Test
	public void testEncodeWithEof() throws IOException {
		ProbabilityTable table = new ProbabilityTable();
		table.add(new Symbol('N'), 2);
		table.add(new Symbol('E'), 2);
		table.add(new Symbol('Q'), 1);
		table.add(new Symbol('U'), 2);
		table.add(getEof(), 1);
		
		sut.encode(new Symbol('N'), table);
		sut.encode(new Symbol('E'), table);
		sut.encode(new Symbol('U'), table);
		sut.encode(new Symbol('Q'), table);
		sut.encode(new Symbol('U'), table);
		sut.encode(new Symbol('E'), table);
		sut.encode(new Symbol('N'), table);
		sut.encode(getEof(), table);
		Assert.assertEquals("01110110111001110000000000",bitWriter.getWritten());
	}
}