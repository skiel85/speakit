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
	
	@Test
	public void testExclusion() {
		ProbabilityTable tableWithSymbolsToExclude = new ProbabilityTable();
		tableWithSymbolsToExclude.add(Symbol.getEscape(), 4);
		tableWithSymbolsToExclude.add(new Symbol('I'), 2);
		tableWithSymbolsToExclude.add(new Symbol('J'), 1);
		
		ProbabilityTable excludedTable = this.sut.exclude(tableWithSymbolsToExclude);
		
		Assert.assertEquals(0.0000, excludedTable.getProbability(Symbol.getEscape()), 0);
		Assert.assertEquals(0.1429, excludedTable.getProbability(new Symbol('C')), 0.00005);
		Assert.assertEquals(0.2857, excludedTable.getProbability(new Symbol('E')), 0.00005);
		Assert.assertEquals(0.0000, excludedTable.getProbability(new Symbol('I')), 0.00005);
		Assert.assertEquals(0.0000, excludedTable.getProbability(new Symbol('J')), 0.00005);
		Assert.assertEquals(0.4286, excludedTable.getProbability(new Symbol('R')), 0.00005);
		Assert.assertEquals(0.1429, excludedTable.getProbability(new Symbol('Y')), 0.00005);
		
		Assert.assertEquals(0.1429, excludedTable.getDistribution(new Symbol('C')), 0.00005);
		Assert.assertEquals(0.4286, excludedTable.getDistribution(new Symbol('E')), 0.00005);
		Assert.assertEquals(0.8571, excludedTable.getDistribution(new Symbol('R')), 0.00005);
		Assert.assertEquals(1.0000, excludedTable.getDistribution(new Symbol('Y')), 0.00005);
	}
	
	@Test
	public void getSymbolFor() {
		// ESC: 0.0000 - 0.4375
		// C:   0.4375 - 0.5000
		// E:   0.5000 - 0.6250 
		// I:   0.6250 - 0.6875
		// J:   0.6875 - 0.7500
		// R:   0.7500 - 0.9375
		// Y:   0.9375 - 1.0000
		
		Assert.assertEquals(Symbol.getEscape().toString(), sut.getSymbolFor(0.4000).toString());
		Assert.assertEquals(new Symbol('C').toString(), sut.getSymbolFor(0.4990).toString());
		Assert.assertEquals(new Symbol('E').toString(), sut.getSymbolFor(0.6000).toString());
		Assert.assertEquals(new Symbol('I').toString(), sut.getSymbolFor(0.6255).toString());
		Assert.assertEquals(new Symbol('J').toString(), sut.getSymbolFor(0.7000).toString());
		Assert.assertEquals(new Symbol('R').toString(), sut.getSymbolFor(0.9000).toString());
		Assert.assertEquals(new Symbol('Y').toString(), sut.getSymbolFor(0.9900).toString());
		
	}
}
