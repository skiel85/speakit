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
		// JERRYRICE
		sut = new ProbabilityTable();
		sut.add(Symbol.getEscape(), 7);
		sut.add(new Symbol('C'), 1);
		sut.add(new Symbol('E'), 2);
		sut.add(new Symbol('I'), 1);
		sut.add(new Symbol('J'), 1);
		sut.add(new Symbol('R'), 3);
		sut.add(new Symbol('Y'), 1);

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetProbability() {
		Assert.assertEquals(0.4375, sut.getProbability(Symbol.getEscape()), 0);
		Assert.assertEquals(0.0625, sut.getProbability(new Symbol('C')), 0);
		Assert.assertEquals(0.1250, sut.getProbability(new Symbol('E')), 0);
		Assert.assertEquals(0.0625, sut.getProbability(new Symbol('I')), 0);
		Assert.assertEquals(0.0625, sut.getProbability(new Symbol('J')), 0);
		Assert.assertEquals(0.1875, sut.getProbability(new Symbol('R')), 0);
		Assert.assertEquals(0.0625, sut.getProbability(new Symbol('Y')), 0);

	}

	@Test
	public void testGetDistribution() {
		Assert.assertEquals(0.4375, sut.getDistribution(Symbol.getEscape()), 0);
		Assert.assertEquals(0.5000, sut.getDistribution(new Symbol('C')), 0);
		Assert.assertEquals(0.6250, sut.getDistribution(new Symbol('E')), 0);
		Assert.assertEquals(0.6875, sut.getDistribution(new Symbol('I')), 0);
		Assert.assertEquals(0.7500, sut.getDistribution(new Symbol('J')), 0);
		Assert.assertEquals(0.9375, sut.getDistribution(new Symbol('R')), 0);
		Assert.assertEquals(1.0000, sut.getDistribution(new Symbol('Y')), 0);
	}
}
