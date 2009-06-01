package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.BitWriter;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class AritmeticEncoderTest {
	 public class MockBitWriter implements BitWriter{
		 StringBuffer buffer=new StringBuffer();
		 
		@Override
		public void write(String bit) throws IOException {
			buffer.append(bit);
		}
		
		public String getWritten(){
			return buffer.toString();
		}
		
	}
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
		
		sut.encode(new Symbol('C'), table);
		sut.encode(new Symbol('D'), table);
		sut.encode(new Symbol('A'), table);
		sut.encode(new Symbol('B'), table);
		sut.encode(new Symbol('A'), table);
		sut.encode(new Symbol('D'), table);
		sut.encode(new Symbol('C'), table);
		sut.encode(Symbol.getEof(), table);
		Assert.assertEquals("101000111110110000000",bitWriter.getWritten());
	}
}