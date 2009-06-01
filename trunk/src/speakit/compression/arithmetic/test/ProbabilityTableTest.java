package speakit.compression.arithmetic.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class ProbabilityTableTest {

	private ProbabilityTable sut;

	@Before
	public void setUp() throws Exception {
		sut = new ProbabilityTable();
		sut.add(Symbol.getEscape(), 8);
		sut.add(new Symbol('a'), 3);
		sut.add(new Symbol('b'), 5);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetProbability() {
		Assert.assertEquals(0.5, sut.getProbability(Symbol.getEscape()), 0);
		Assert.assertEquals(0.1875, sut.getProbability(new Symbol('a')), 0);
		Assert.assertEquals(0.3125, sut.getProbability(new Symbol('b')), 0);
	}
}
