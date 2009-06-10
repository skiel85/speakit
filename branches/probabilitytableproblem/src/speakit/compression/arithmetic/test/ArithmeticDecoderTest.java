package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ArithmeticDecoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.StringBitReader;
import speakit.compression.arithmetic.Symbol;

public class ArithmeticDecoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testDecodeStatic() throws IOException {
		ArithmeticDecoder	sut;
		String string = "1010001111101101011100000000000000";
		sut = new ArithmeticDecoder(new StringBitReader(string), 8);
		ProbabilityTable table = new ProbabilityTable();
		table.increment(new Symbol('C'), 2);
		table.increment(new Symbol('D'), 2);
		table.increment(new Symbol('B'), 1);
		table.increment(new Symbol('A'), 2);
		table.sort();
 
		Assert.assertEquals(new Symbol('C'), sut.decode(table));
		Assert.assertEquals(new Symbol('D'), sut.decode(table));
		Assert.assertEquals(new Symbol('A'), sut.decode(table));
		Assert.assertEquals(new Symbol('B'), sut.decode(table));
		Assert.assertEquals(new Symbol('A'), sut.decode(table));
		Assert.assertEquals(new Symbol('D'), sut.decode(table));
		Assert.assertEquals(new Symbol('C'), sut.decode(table));
	}
	 
	@Test
	public void testDecodeWithEofDynamic() throws IOException { 
		ProbabilityTable table = new ProbabilityTable(); 
		table.increment(new Symbol('N'), 2);
		table.increment(new Symbol('E'), 2);
		table.increment(new Symbol('Q'), 1);
		table.increment(new Symbol('U'), 2);
		table.increment(Symbol.getEof(), 1);
		table.sort();
		System.out.println(table.toString());
		String input = "011001110000000";
		//está probado a mano que input es la compresion de NENUQ(EOF)
		Assert.assertEquals("011001110000000",input);

		ArithmeticDecoder decoder = new ArithmeticDecoder(new StringBitReader(input), 8);
		Assert.assertEquals(new Symbol('N'), decoder.decode(table));
		table.increment(new Symbol('N'));
		System.out.println(table.toString());
//		Assert.assertEquals("",decoder.currentBuffer);
		Assert.assertEquals(new Symbol('E'), decoder.decode(table));
		table.increment(new Symbol('E'));
		System.out.println(table.toString());
////		Assert.assertEquals("011",decoder.currentBuffer); 
//		Assert.assertEquals(new Symbol('U'), decoder.decode(table));
//		table.increment(new Symbol('U'));
////		Assert.assertEquals("101",decoder.currentBuffer);
//		Assert.assertEquals(new Symbol('Q'), decoder.decode(table));
//		table.increment(new Symbol('Q'));
////		Assert.assertEquals("101",decoder.currentBuffer); 
		Assert.assertEquals(Symbol.getEof(), decoder.decode(table));
		table.increment(Symbol.getEof());
////		Assert.assertEquals("000",decoder.currentBuffer);
	}

}
