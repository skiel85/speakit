package speakit.compression.arithmetic.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class ProbabilityTableFullFilledTest {

	private ProbabilityTable sut;

	@Before
	public void setUp() throws Exception {
		// JERRYRICE
		sut = new ProbabilityTable();
		sut.initAllSymbols();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testAddIncrmentsFrecuency() {
		Symbol symbol = new Symbol('a');
		double firstProbability = sut.getProbability(symbol);
		sut.increment(symbol);
		double newProbability = sut.getProbability(symbol);
		Assert.assertTrue("Deberia ser menor: expectedLower: " + firstProbability + ", expectedHigher: " + newProbability, firstProbability < newProbability);
	}

	@Test
	public void testAddGetIncrementAddGet() {
		ProbabilityTable encodeTable = new ProbabilityTable();
		encodeTable.initAllSymbols();
		// encodeTable.increment(new Symbol('N'));
		// encodeTable.increment(new Symbol('E'));
		// encodeTable.increment(new Symbol('U'));
		// encodeTable.increment(new Symbol('Q'));

		Symbol firstSymbol = new Symbol('N');
		double firstProbability = encodeTable.getProbabilityUntil(firstSymbol);
		encodeTable.increment(firstSymbol);

		Symbol secondSymbol = new Symbol('E');
		double secondProbability = encodeTable.getProbabilityUntil(secondSymbol);
		encodeTable.increment(secondSymbol);

		Symbol thirdSymbol = new Symbol('U');
		double thirdProbability = encodeTable.getProbabilityUntil(thirdSymbol);
		encodeTable.increment(thirdSymbol);

		Symbol fourthSymbol = new Symbol('Q');
		double fourthProbability = encodeTable.getProbabilityUntil(fourthSymbol);
		encodeTable.increment(fourthSymbol);

		ProbabilityTable decodeTable = new ProbabilityTable();
		decodeTable.initAllSymbols();
		// decodeTable.increment(new Symbol('N'));
		// decodeTable.increment(new Symbol('E'));
		// decodeTable.increment(new Symbol('U'));
		// decodeTable.increment(new Symbol('Q'));

		Assert.assertEquals(firstSymbol, decodeTable.getSymbolFor(firstProbability));
		decodeTable.increment(firstSymbol);
		Assert.assertEquals(secondSymbol, decodeTable.getSymbolFor(secondProbability));
		decodeTable.increment(secondSymbol);
		Assert.assertEquals(thirdSymbol, decodeTable.getSymbolFor(thirdProbability));
		decodeTable.increment(thirdSymbol);
		Assert.assertEquals(fourthSymbol, decodeTable.getSymbolFor(fourthProbability));
		decodeTable.increment(fourthSymbol);
	}

	@Test
	@Ignore
	public void testAddGetIncrementIterativelyForEverySymbol() {
		for (int i = 0; i < Math.pow(2, 8); i++) {
			// System.out.println(i);
			sut = new ProbabilityTable();
			sut.initAllSymbols();
			Symbol symbol = new Symbol(i);

			double probability = 0;
			for (int p = 0; p < 2; p++) {
				// System.out.println(i + ", p = " + p);
				probability = sut.getProbabilityUntil(symbol);
				Symbol symbolFor = sut.getSymbolFor(probability);
				Assert.assertEquals("Se esperaba simbolo numero " + i + " pero vino simbolo numero " + symbolFor.getNumber() + ", frecuencia: " + p + ". Expected: " + symbol + ", but was:" + symbolFor, symbol, symbolFor);
				sut.increment(symbol);
			}
		}
	}

	// @Test
	// public void testDistributionLowerThan1() {
	//		 
	// for (int i = 0; i < (Math.pow(2, 16)-1); i++) {
	// System.out.println(i);
	// // if(i==17){
	// Assert.assertTrue("El caracter ("+i+")=" + (char)i
	// +" no deberia tener probabilidad 1 ", sut.getDistribution(new
	// Symbol(i))<1);
	// // }
	//			
	// }
	// for (int i = 0; i < (Math.pow(2,16))-1; i++) {
	// Assert.assertTrue("El caracter ("+i+")=" + (char)i
	// +" no deberia tener probabilidad 1 ", sut.getDistribution(new
	// Symbol(i))<1);
	// }
	// }

}
