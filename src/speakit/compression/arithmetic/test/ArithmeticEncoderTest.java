package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.SpeakitLogger;
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
//		SpeakitLogger.activate();
//		SpeakitLogger.deactivate();
	}

	@After
	public void tearDown() throws Exception {
		
	}
	


	@Test
	public void testEncode() throws IOException {
		ProbabilityTable table=new ProbabilityTable();
		table.increment(new Symbol('C'), 2);
		table.increment(new Symbol('D'), 2);
		table.increment(new Symbol('B'), 1);
		table.increment(new Symbol('A'), 2);
		table.increment(Symbol.getEof(), 1);
		table.sort();
		sut.encode(new Symbol('C'), table);
		sut.encode(new Symbol('D'), table);
		sut.encode(new Symbol('A'), table);
		sut.encode(new Symbol('B'), table);
		sut.encode(new Symbol('A'), table);
		sut.encode(new Symbol('D'), table);
		sut.encode(new Symbol('C'), table);
		sut.encode(Symbol.getEof(), table);
		
		//Esto puede fallar si la tabla de probabilidades se ordena distinto,
		//por ejemplo si en lugar de usar el simbolo EOF usamos \0 o algo as�
		Assert.assertEquals("10110011101011000000000000",bitWriter.getWritten());
	}
	
//	@Test
//	public void testEncode32Bits() throws IOException {
//
//		bitWriter = new MockBitWriter();
//		sut = new ArithmeticEncoder(bitWriter,32);
//		testEncode();
//	}
	
	
	@Test
	public void testEncodeNEUQWithEofDynamic() throws IOException {
		ProbabilityTable table = new ProbabilityTable(); 
		table.increment(new Symbol('N'), 2);
		table.increment(new Symbol('E'), 2);
		table.increment(new Symbol('Q'), 1);
		table.increment(new Symbol('U'), 2);
		table.increment(Symbol.getEof(), 1);
		table.sort();
		SpeakitLogger.Log(table);
		sut.encode(new Symbol('N'), table);
		table.increment(new Symbol('N'));
		SpeakitLogger.Log(table.toString());
//		Assert.assertEquals("",bitWriter.getWritten());
		sut.encode(new Symbol('E'), table);
		table.increment(new Symbol('E'));
		SpeakitLogger.Log(table.toString());
////		Assert.assertEquals("011",bitWriter.getWritten());
//		sut.encode(new Symbol('U'), table);
//		table.increment(new Symbol('U'));
////		Assert.assertEquals("011101",bitWriter.getWritten());
//		sut.encode(new Symbol('Q'), table);
//		table.increment(new Symbol('Q'));
////		Assert.assertEquals("011101101",bitWriter.getWritten());
		sut.encode(Symbol.getEof(), table); 
		SpeakitLogger.Log(table.toString());
		//Esto puede fallar si la tabla de probabilidades se ordena distinto,
		//por ejemplo si en lugar de usar el simbolo EOF usamos \0 o algo as�
		Assert.assertEquals("011001110000000",bitWriter.getWritten()); 
	} 
	
}