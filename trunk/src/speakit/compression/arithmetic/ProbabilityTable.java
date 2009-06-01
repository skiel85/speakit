package speakit.compression.arithmetic;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProbabilityTable {

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
	public ProbabilityTable exclude(ProbabilityTable table) {
		Map<Symbol, Integer> newFreqs = new HashMap<Symbol, Integer>();

		for (Symbol sym : this.getSymbols()) {
			if (!table.contains(sym)) {
				newFreqs.put(sym, this.symbolFrequences.get(sym));
			}
		}

		ProbabilityTable res = new ProbabilityTable();
		res.symbolFrequences = newFreqs;
		return res;
	}

	/**
		 */
	public void add(Symbol symbol, int frequence) {
		this.symbolFrequences.put(symbol, new Integer(frequence));
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
		return this.getDistribution(symbol) - this.getProbability(symbol);
	}

	/**
		 */
	public double getDistribution(Symbol symbol) {
		Iterator<Symbol> it = this.getSymbols().iterator();
		int accum = 0;

		while (it.hasNext()) {
			Symbol sym = it.next();
			accum += this.symbolFrequences.get(sym);
			if (symbol.compareTo(sym) <= 0) {
				return accum / (double) this.getTotalFrecuence();
			}
		}
		return 1.0;
	}

	/**
		 */
	public Symbol getSymbolFor(double probabity) {
		int equivFreq = (int) (probabity * this.getTotalFrecuence());
		int accum = 0;
		for (Symbol sym : this.getSymbols()) {
			accum += this.symbolFrequences.get(sym);
			if (accum >= equivFreq) {
				return sym;
			}
		}
		throw new InvalidParameterException("La probabilidad es mayor a 1.");
	}

	/**
		 */
	public boolean contains(Symbol symbol) {
		return this.symbolFrequences.containsKey(symbol);
	}

}
