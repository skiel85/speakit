package speakit.compression.arithmetic.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.ProbabilityTableOld;
import speakit.compression.arithmetic.Symbol;

public class ProbabilityTableComparisionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompareResultsNEUQUEN() {
		ProbabilityTable newTable = new ProbabilityTable();
		ProbabilityTableOld oldTable = new ProbabilityTableOld();

		Symbol N = new Symbol('N');
		Symbol E = new Symbol('E');
		Symbol U = new Symbol('U');
		Symbol Q = new Symbol('Q');

		newTable.increment(N);
		newTable.increment(E);
		newTable.increment(U);
		newTable.increment(Q);
		newTable.increment(U);
		newTable.increment(E);
		newTable.increment(N);
		newTable.sort();

		oldTable.increment(N);
		oldTable.increment(E);
		oldTable.increment(U);
		oldTable.increment(Q);
		oldTable.increment(U);
		oldTable.increment(E);
		oldTable.increment(N);

		Assert.assertEquals(newTable.getProbability(N), oldTable.getProbability(N));
		Assert.assertEquals(newTable.getProbability(E), oldTable.getProbability(E));
		Assert.assertEquals(newTable.getProbability(U), oldTable.getProbability(U));
		Assert.assertEquals(newTable.getProbability(Q), oldTable.getProbability(Q));

		Assert.assertEquals(newTable.getDistribution(N), oldTable.getDistribution(N));
		Assert.assertEquals(newTable.getDistribution(E), oldTable.getDistribution(E));
		Assert.assertEquals(newTable.getDistribution(U), oldTable.getDistribution(U));
		Assert.assertEquals(newTable.getDistribution(Q), oldTable.getDistribution(Q));

		Assert.assertEquals(newTable.getProbabilityUntil(N), oldTable.getProbabilityUntil(N));
		Assert.assertEquals(newTable.getProbabilityUntil(E), oldTable.getProbabilityUntil(E));
		Assert.assertEquals(newTable.getProbabilityUntil(U), oldTable.getProbabilityUntil(U));
		Assert.assertEquals(newTable.getProbabilityUntil(Q), oldTable.getProbabilityUntil(Q));
	}

	@Test
	public void testCompareResultsCATAMARCA() {
		ProbabilityTable newTable = new ProbabilityTable();
		ProbabilityTableOld oldTable = new ProbabilityTableOld();

		Symbol C = new Symbol('C');
		Symbol A = new Symbol('A');
		Symbol T = new Symbol('T');
		Symbol M = new Symbol('M');
		Symbol R = new Symbol('R');

		newTable.increment(C);
		newTable.increment(A);
		newTable.increment(T);
		newTable.increment(A);
		newTable.increment(M);
		newTable.increment(A);
		newTable.increment(R);
		newTable.increment(C);
		newTable.increment(A);
		newTable.sort();

		oldTable.increment(C);
		oldTable.increment(A);
		oldTable.increment(T);
		oldTable.increment(A);
		oldTable.increment(M);
		oldTable.increment(A);
		oldTable.increment(R);
		oldTable.increment(C);
		oldTable.increment(A);

		Assert.assertEquals(newTable.getProbability(C), oldTable.getProbability(C));
		Assert.assertEquals(newTable.getProbability(A), oldTable.getProbability(A));
		Assert.assertEquals(newTable.getProbability(T), oldTable.getProbability(T));
		Assert.assertEquals(newTable.getProbability(M), oldTable.getProbability(M));
		Assert.assertEquals(newTable.getProbability(R), oldTable.getProbability(R));

		Assert.assertEquals(newTable.getDistribution(C), oldTable.getDistribution(C));
		Assert.assertEquals(newTable.getDistribution(A), oldTable.getDistribution(A));
		Assert.assertEquals(newTable.getDistribution(T), oldTable.getDistribution(T));
		Assert.assertEquals(newTable.getDistribution(M), oldTable.getDistribution(M));
		Assert.assertEquals(newTable.getDistribution(R), oldTable.getDistribution(R));

		Assert.assertEquals(newTable.getProbabilityUntil(C), oldTable.getProbabilityUntil(C));
		Assert.assertEquals(newTable.getProbabilityUntil(A), oldTable.getProbabilityUntil(A));
		Assert.assertEquals(newTable.getProbabilityUntil(T), oldTable.getProbabilityUntil(T));
		Assert.assertEquals(newTable.getProbabilityUntil(M), oldTable.getProbabilityUntil(M));
		Assert.assertEquals(newTable.getProbabilityUntil(R), oldTable.getProbabilityUntil(R));
	}

	public void initAllSymbols(ProbabilityTableOld table) {
		Symbol currentSymbol = Symbol.first();
		table.increment(currentSymbol);
		currentSymbol = currentSymbol.next().next();
		while (currentSymbol.compareTo(Symbol.last()) != 0) {
			table.increment(currentSymbol);
			currentSymbol = currentSymbol.next();
		}
	}

	@Test
	public void testCompareResultsCATAMARCAWithFullFilledTable() {
		ProbabilityTable newTable = new ProbabilityTable();
		newTable.initAllSymbols();

		ProbabilityTableOld oldTable = new ProbabilityTableOld();
		initAllSymbols(oldTable);

		List<Symbol> symbols = new ArrayList<Symbol>();
		Symbol C = new Symbol('C');
		Symbol A = new Symbol('A');
		Symbol T = new Symbol('T');
		Symbol M = new Symbol('M');
		Symbol R = new Symbol('R');
		Symbol X = new Symbol('X');
		Symbol EOF = Symbol.getEof();
		symbols.add(C);
		symbols.add(A);
		symbols.add(T);
		symbols.add(M);
		symbols.add(R);
		symbols.add(X);
		symbols.add(EOF);

		newTable.increment(C);
		newTable.increment(A);
		newTable.increment(T);
		newTable.increment(A);
		newTable.increment(M);
		newTable.increment(A);
		newTable.increment(R);
		newTable.increment(C);
		newTable.increment(A);
		newTable.sort();

		oldTable.increment(C);
		oldTable.increment(A);
		oldTable.increment(T);
		oldTable.increment(A);
		oldTable.increment(M);
		oldTable.increment(A);
		oldTable.increment(R);
		oldTable.increment(C);
		oldTable.increment(A);

		for (Symbol symbol : symbols) {
			Assert.assertEquals(newTable.getProbability(symbol), oldTable.getProbability(symbol));
			Assert.assertEquals(newTable.getDistribution(symbol), oldTable.getDistribution(symbol));
			Assert.assertEquals(newTable.getProbabilityUntil(symbol), oldTable.getProbabilityUntil(symbol));
		}
	}

	public void incrementAndTest(List<Symbol> symbols, Symbol symbolToIncrement, ProbabilityTableOld oldTable, ProbabilityTable newTable) {
		oldTable.increment(symbolToIncrement);
		newTable.increment(symbolToIncrement);
		newTable.sort();
		for (Symbol symbol : symbols) {
			Assert.assertEquals(newTable.getProbability(symbol), oldTable.getProbability(symbol));
			Assert.assertEquals(newTable.getDistribution(symbol), oldTable.getDistribution(symbol));
			Assert.assertEquals(newTable.getProbabilityUntil(symbol), oldTable.getProbabilityUntil(symbol));

			double probabilityUntil = newTable.getProbabilityUntil(symbol);
			Assert.assertEquals(oldTable.getSymbolFor(probabilityUntil), newTable.getSymbolFor(probabilityUntil));
			Assert.assertEquals(symbol, newTable.getSymbolFor(probabilityUntil));
			Assert.assertEquals(symbol, oldTable.getSymbolFor(probabilityUntil));
		}
	}

	@Test
	public void testCompareResultsCATAMARCAFilledDinamically() {
		ProbabilityTable newTable = new ProbabilityTable();
		newTable.initAllSymbols();

		ProbabilityTableOld oldTable = new ProbabilityTableOld();
		initAllSymbols(oldTable);

		List<Symbol> symbols = new ArrayList<Symbol>();
		Symbol C = new Symbol('C');
		Symbol A = new Symbol('A');
		Symbol T = new Symbol('T');
		Symbol M = new Symbol('M');
		Symbol R = new Symbol('R');
		symbols.add(C);
		symbols.add(A);
		symbols.add(T);
		symbols.add(M);
		symbols.add(R);

		incrementAndTest(symbols, C, oldTable, newTable);
		incrementAndTest(symbols, A, oldTable, newTable);
		incrementAndTest(symbols, T, oldTable, newTable);
		incrementAndTest(symbols, A, oldTable, newTable);
		incrementAndTest(symbols, M, oldTable, newTable);
		incrementAndTest(symbols, A, oldTable, newTable);
		incrementAndTest(symbols, R, oldTable, newTable);
		incrementAndTest(symbols, C, oldTable, newTable);
		incrementAndTest(symbols, A, oldTable, newTable);
	}

	@Test
	public void testCompareResultsNEUQUENFilledDinamically() {
		ProbabilityTable newTable = new ProbabilityTable();
		newTable.initAllSymbols();

		ProbabilityTableOld oldTable = new ProbabilityTableOld();
		initAllSymbols(oldTable);

		List<Symbol> symbols = new ArrayList<Symbol>();
		Symbol N = new Symbol('N');
		Symbol E = new Symbol('E');
		Symbol U = new Symbol('U');
		Symbol Q = new Symbol('Q');
		symbols.add(N);
		symbols.add(E);
		symbols.add(U);
		symbols.add(Q);

		incrementAndTest(symbols, N, oldTable, newTable);
		incrementAndTest(symbols, E, oldTable, newTable);
		incrementAndTest(symbols, U, oldTable, newTable);
		incrementAndTest(symbols, Q, oldTable, newTable);
		incrementAndTest(symbols, U, oldTable, newTable);
		incrementAndTest(symbols, E, oldTable, newTable);
		incrementAndTest(symbols, N, oldTable, newTable);
	}

}
