package speakit.compression.arithmetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProbabilityTableOld {

	/**
	 * @uml.property name="symbolOccurrence"
	 */
	private Map<Symbol, Integer> symbolFrequences = new HashMap<Symbol, Integer>();

	/**
		 */
	protected int getFrequence(Symbol symbol) {
		return this.symbolFrequences.get(symbol);
	}

	protected List<Symbol> getSymbols() {
		List<Symbol> result = new ArrayList<Symbol>(this.symbolFrequences.keySet());
		Collections.sort(result);
		return result;
	}

	/**
		 */
	public ProbabilityTableOld exclude(ProbabilityTable table) {
		Map<Symbol, Integer> newFreqs = new HashMap<Symbol, Integer>();

		for (Symbol sym : this.getSymbols()) {
			if (!table.contains(sym)) {
				newFreqs.put(sym, this.symbolFrequences.get(sym));
			}
		}

		ProbabilityTableOld res = new ProbabilityTableOld();
		res.symbolFrequences = newFreqs;
		return res;
	}

	public void increment(Symbol symbol, int times) {
		for (int i = 0; i < times; i++) {
			increment(symbol);
		}
	}

	public void increment(Symbol symbol) {
		Integer frequency = this.symbolFrequences.get(symbol);
		if (frequency != null) {
			frequency++;
		} else {
			frequency = 1;
		}
		this.symbolFrequences.put(symbol, frequency);
	}

	protected int getTotalFrecuence() {
		int accum = 0;
		for (Symbol sym : this.getSymbols()) {
			accum += this.symbolFrequences.get(sym);
		}
		return accum;
	}

	/**
		 */
	public double getProbability(Symbol symbol) {
		if (this.symbolFrequences.containsKey(symbol)) {
			return this.symbolFrequences.get(symbol) / (double) this.getTotalFrecuence();
		} else {
			return 0.0;
		}
	}

	public double getProbabilityUntil(Symbol symbol) {
		return this.getFrecuencyUntil(symbol, false) / (double) this.getTotalFrecuence();
		// return this.getDistribution(symbol) - this.getProbability(symbol);
	}

	public double getDistribution(Symbol symbol) {
		return this.getFrecuencyUntil(symbol, true) / (double) this.getTotalFrecuence();
		// Iterator<Symbol> it = this.getSymbols().iterator();
		// int accum = 0;
		//
		// while (it.hasNext()) {
		// Symbol sym = it.next();
		// accum += this.symbolFrequences.get(sym);
		// if (symbol.compareTo(sym) <= 0) {
		// return accum / (double) this.getTotalFrecuence();
		// }
		// }
		// return 1.0;
	}

	private int getFrecuencyUntil(Symbol symbol, boolean includeSymbol) {
		Iterator<Symbol> it = this.getSymbols().iterator();
		int accum = 0;
		boolean found = false;

		while (it.hasNext() && !found) {
			Symbol sym = it.next();
			accum += this.symbolFrequences.get(sym);
			if (symbol.compareTo(sym) <= 0) {
				found = true;
			}
		}
		if (!includeSymbol) {
			accum -= this.symbolFrequences.get(symbol);
		}
		return accum;
	}

	/**
		 */
	public Symbol getSymbolFor(double probabity) {
		int equivFreq = (int) (probabity * this.getTotalFrecuence());
		Iterator<Symbol> it = this.getSymbols().iterator();
		int accum = 0;
		Symbol sym = null;

		while (it.hasNext()) {
			sym = it.next();
			accum += this.symbolFrequences.get(sym);
			if (accum > equivFreq) {
				return sym;
			}
		}
		return sym;
	}

	/**
		 */
	public boolean contains(Symbol symbol) {
		return this.symbolFrequences.containsKey(symbol);
	}

}
