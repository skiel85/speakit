package speakit.compression.arithmetic;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProbabilityTable {

	private class SymbolFrequency implements Comparable<SymbolFrequency> {
		private Symbol symbol;
		private int frequency;

		public SymbolFrequency(Symbol symbol, int frequency) {
			this.symbol = symbol;
			this.frequency = frequency;
		}

		public int getFrequency() {
			return this.frequency;
		}

		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}

		public Symbol getSymbol() {
			return this.symbol;
		}

		public void setSymbol(Symbol symbol) {
			this.symbol = symbol;
		}

		@Override
		public int compareTo(SymbolFrequency o) {
			return this.symbol.compareTo(o.symbol);
		}

		@Override
		public String toString() {
			return this.symbol.toString() + "(" + this.frequency + ")";
		}
	}

	/**
	 * @uml.property name="symbolOccurrence"
	 */
	private List<SymbolFrequency> symbolFrequencies = new ArrayList<SymbolFrequency>();

	protected int getFrequency(Symbol symbol) {
		int symbolIndex = getSymbolIndex(symbol);
		if (symbolIndex >= 0) {
			return this.symbolFrequencies.get(symbolIndex).getFrequency();
		} else {
			throw new InvalidParameterException("El símbolo no existe en la tabla.");
		}
	}

	protected List<Symbol> getSymbols() {
		List<Symbol> result = new ArrayList<Symbol>();
		for (SymbolFrequency symbolFrequency : symbolFrequencies) {
			result.add(symbolFrequency.getSymbol());
		}
		Collections.sort(result);
		return result;
	}

	public ProbabilityTable exclude(ProbabilityTable table) {
		List<SymbolFrequency> newFreqs = new ArrayList<SymbolFrequency>(this.symbolFrequencies);
		Iterator<SymbolFrequency> iterator = newFreqs.iterator();

		while (iterator.hasNext()) {
			SymbolFrequency symbolFrequency = iterator.next();
			for (SymbolFrequency excludeSymbol : table.symbolFrequencies) {
				if (symbolFrequency.getSymbol().compareTo(excludeSymbol.getSymbol()) == 0) {
					iterator.remove();
				}
			}
		}

		ProbabilityTable res = new ProbabilityTable();
		res.symbolFrequencies = newFreqs;
		return res;
	}

	public void increment(Symbol symbol, int times) {
		for (int i = 0; i < times; i++) {
			increment(symbol);
		}
	}

	private int getSymbolIndex(Symbol symbol) {
		for (int i = 0; i < this.symbolFrequencies.size(); i++) {
			if (this.symbolFrequencies.get(i).getSymbol().compareTo(symbol) == 0) {
				return i;
			}
		}
		return -1;
	}

	public void increment(Symbol symbol) {
		int symbolIndex = getSymbolIndex(symbol);
		if (symbolIndex >= 0) {
			SymbolFrequency symbolFrequency = this.symbolFrequencies.get(symbolIndex);
			if(symbolFrequency.symbol.compareTo(symbol)!=0){
				throw new RuntimeException("No se pudo incrementar la frecuencia al simbolo " + symbol);
			}
			symbolFrequency.setFrequency(symbolFrequency.getFrequency() + 1);
		} else {
			this.symbolFrequencies.add(new SymbolFrequency(symbol, 1));
		}
	}

	protected int getTotalFrecuency() {
		int accum = 0;
		for (SymbolFrequency symbolFrequency : this.symbolFrequencies) {
			accum += symbolFrequency.getFrequency();
		}
		return accum;
	}

	public double getProbability(Symbol symbol) {
		return this.getFrequency(symbol) / (double) this.getTotalFrecuency();
	}

	private int getFrequenceUntil(Symbol symbol, boolean includeSymbol) {
		int symbolIndex = getSymbolIndex(symbol);
		if (symbolIndex >= 0) {
			int accum = 0;
			for (int i = 0; i < symbolIndex || (includeSymbol && (i == symbolIndex)); i++) {
				accum += this.symbolFrequencies.get(i).getFrequency();
			}
			return accum;
		} else {
			throw new InvalidParameterException("El símbolo no existe en la tabla.");
		}
	}

	public double getProbabilityUntil(Symbol symbol) {
		//return this.getDistribution(symbol) - this.getProbability(symbol);
		return this.getFrequenceUntil(symbol, false) / (double) this.getTotalFrecuency();
	}

	public double getDistribution(Symbol symbol) {
		return this.getFrequenceUntil(symbol, true) / (double) this.getTotalFrecuency();
	}

	public Symbol getSymbolFor(double probability) {
		// int equivFreq = (int) (probability * this.getTotalFrecuency());
		int totalFreq = this.getTotalFrecuency();
		int accum = 0;

		for (SymbolFrequency symbolFrequency : this.symbolFrequencies) {
			accum += symbolFrequency.getFrequency();
			if (accum / (double) totalFreq > probability) {
				return symbolFrequency.getSymbol();
			}
		}
		return this.symbolFrequencies.get(this.symbolFrequencies.size() - 1).getSymbol();
	}

	public boolean contains(Symbol symbol) {
		return (this.getSymbolIndex(symbol) >= 0);
	}

	public void initAllSymbols() {
		this.symbolFrequencies.clear();
		Symbol currentSymbol = Symbol.first();
		this.symbolFrequencies.add(new SymbolFrequency(currentSymbol, 1));
		currentSymbol = currentSymbol.next().next();
		while (currentSymbol.compareTo(Symbol.last()) != 0) {
			this.symbolFrequencies.add(new SymbolFrequency(currentSymbol, 1));
			currentSymbol = currentSymbol.next();
		}
	}

	public void sort() {
		Collections.sort(this.symbolFrequencies);
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (SymbolFrequency symbolFrequency : this.symbolFrequencies) {
			if (symbolFrequency.symbol.isInteresting()) {
				result.append(symbolFrequency.toString() + " ");
				if (counter == 10) {
					result.append("\n");
					counter = 0;
				}
				counter++;
			}
		}
		return result.toString();
	}
}
