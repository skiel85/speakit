package speakit.compression.arithmetic;

import java.security.InvalidParameterException;
import java.util.HashMap;
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

	/**
		 */
	public ProbabilityTable exclude(ProbabilityTable table) {
		Map<Symbol, Integer> newFreqs = new HashMap<Symbol, Integer>();
		
		for (Symbol sym : this.symbolFrequences.keySet()) {
			if(!table.contains(sym)) {
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
		for (Symbol sym : this.symbolFrequences.keySet()) {
			accum += this.symbolFrequences.get(sym);
		}
		return accum;
	}

	/**
		 */
	public double getProbability(Symbol symbol) {
		return (double) this.symbolFrequences.get(symbol) / (double) this.getTotalFrecuence();
	}

	/**
		 */
	public double getProbabilityUntil(Symbol symbol) {
		int accum = 0;
		for (Symbol sym : this.symbolFrequences.keySet()) {
			accum += this.symbolFrequences.get(sym);
			if (symbol.equals(sym)) {
				return accum;
			}
		}
		throw new InvalidParameterException("El símbolo no existe en la tabla de probabilidades.");
	}

	/**
		 */
	public Symbol getSymbolFor(double probabity) {
		int equivFreq = (int) (probabity * this.getTotalFrecuence());
		int accum = 0;
		for (Symbol sym : this.symbolFrequences.keySet()) {
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
