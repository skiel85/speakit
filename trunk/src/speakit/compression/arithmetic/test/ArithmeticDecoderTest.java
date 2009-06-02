package speakit.compression.arithmetic.test;

import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ArithmeticDecoder;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class ArithmeticDecoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testDecode() throws IOException {
		ArithmeticDecoder	sut;
		String string = "1010001111101101011100000000000000";
		sut = new ArithmeticDecoder(new StringReader(string), 8);
		ProbabilityTable table = new ProbabilityTable();
		table.add(new Symbol('C'), 2);
		table.add(new Symbol('D'), 2);
		table.add(new Symbol('B'), 1);
		table.add(new Symbol('A'), 2);

		// sut.encode(new Symbol('C'), table);
		// sut.encode(new Symbol('D'), table);
		// sut.encode(new Symbol('A'), table);
		// sut.encode(new Symbol('B'), table);
		// sut.encode(new Symbol('A'), table);
		// sut.encode(new Symbol('D'), table);
		// sut.encode(new Symbol('C'), table);
		// sut.encode(Symbol.getEof(), table);
		Assert.assertEquals(new Symbol('C'), sut.decode(table));
		Assert.assertEquals(new Symbol('D'), sut.decode(table));
		Assert.assertEquals(new Symbol('A'), sut.decode(table));
		Assert.assertEquals(new Symbol('B'), sut.decode(table));
		Assert.assertEquals(new Symbol('A'), sut.decode(table));
		Assert.assertEquals(new Symbol('D'), sut.decode(table));
		Assert.assertEquals(new Symbol('C'), sut.decode(table));
	}

	@Test
	public void testDecodeWithEof() throws IOException {
		

		ProbabilityTable table = new ProbabilityTable();
		table.add(new Symbol('N'), 2);
		table.add(new Symbol('E'), 2);
		table.add(new Symbol('Q'), 1);
		table.add(new Symbol('U'), 2);
		table.add(new Symbol('F'), 1);

		MockBitWriter bitWriter = new MockBitWriter();
		ArithmeticEncoder encoder = new ArithmeticEncoder(bitWriter,8);
		encoder.encode(new Symbol('N'), table);
		encoder.encode(new Symbol('E'), table);
		encoder.encode(new Symbol('U'), table);
		encoder.encode(new Symbol('Q'), table);
		encoder.encode(new Symbol('U'), table);
		encoder.encode(new Symbol('E'), table);
		encoder.encode(new Symbol('N'), table);
		encoder.encode(new Symbol('F'), table);

		String input = bitWriter.getWritten();

		ArithmeticDecoder decoder = new ArithmeticDecoder(new StringReader(input), 8);
		Assert.assertEquals(new Symbol('N'), decoder.decode(table));
		Assert.assertEquals(new Symbol('E'), decoder.decode(table));
		Assert.assertEquals(new Symbol('U'), decoder.decode(table));
		Assert.assertEquals(new Symbol('Q'), decoder.decode(table));
		Assert.assertEquals(new Symbol('U'), decoder.decode(table));
		Assert.assertEquals(new Symbol('E'), decoder.decode(table));
		Assert.assertEquals(new Symbol('N'), decoder.decode(table));
		Assert.assertEquals(new Symbol('F'), decoder.decode(table));
	}

}
