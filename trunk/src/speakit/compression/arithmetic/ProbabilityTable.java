package speakit.compression.arithmetic;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import speakit.SpeakitLogger;

public class ProbabilityTable {

	private class SymbolFrequency implements Comparable<SymbolFrequency> {
		private Symbol	symbol;
		private int		frequency;

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
	private List<SymbolFrequency>	symbolFrequencies	= new ArrayList<SymbolFrequency>();

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
		SpeakitLogger.Log("ProbabilityTable->exclude");
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
		SpeakitLogger.Log("ProbabilityTable->increment symbol:" + symbol + ", times:" + times);
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
		SpeakitLogger.Log("ProbabilityTable->Incrementando: " + symbol);
		int symbolIndex = getSymbolIndex(symbol);
		if (symbolIndex >= 0) {
			SymbolFrequency symbolFrequency = this.symbolFrequencies.get(symbolIndex);
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
		SpeakitLogger.Log("ProbabilityTable->getProbability symbol:" + symbol);
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
		SpeakitLogger.Log("ProbabilityTable->getProbabilityUntil: " + symbol);
		return this.getFrequenceUntil(symbol, false) / (double) this.getTotalFrecuency();
	}

	public double getDistribution(Symbol symbol) {
		SpeakitLogger.Log("ProbabilityTable->getDistribution: " + symbol);
		return this.getFrequenceUntil(symbol, true) / (double) this.getTotalFrecuency();
	}

	@Deprecated
	public Symbol getSymbolFor(double probability) {
		// int equivFreq = (int) (probability * this.getTotalFrecuency());
		SpeakitLogger.Log("ProbabilityTable->getSymbolFor: " + probability);
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

	public void zoomRangeIn(Symbol symbol, Range range) throws IOException {
		long[] newRange = getNewRange(symbol,range);		
		range.zoomIn(newRange[0],newRange[1]);
	}
	
	private long[] getNewRange(Symbol symbol, Range range){
		long totalFreq = this.getTotalFrecuency();
		long accumulatedfrecuency = this.getFrequenceUntil(symbol, false);
		long symbolFrequency = this.getFrequency(symbol);
		long previousFloor = range.getNumericFloor();
		long rangeSize = range.getRangeSize();
		return calculateRange(totalFreq, accumulatedfrecuency, symbolFrequency, previousFloor, rangeSize);
	}

	private long[] calculateRange(long totalFreq, long accumulatedfrecuency, long symbolFrequency, long previousFloor, long rangeSize) {
		long roof= calculateRoof(totalFreq, accumulatedfrecuency, previousFloor, rangeSize,symbolFrequency);
		long floor = calculateFloor(totalFreq, accumulatedfrecuency, previousFloor, rangeSize);
		return new long[]{floor,roof};
	}

	private long calculateFloor(long totalFreq, long accumulatedfrecuency, long previousFloor, long rangeSize) {
		return previousFloor + roundDouble(rangeSize * accumulatedfrecuency / totalFreq);
	}

	private long calculateRoof(long totalFreq, long accumulatedfrecuency, long previousFloor, long rangeSize,long symbolFrequency) {
		return previousFloor + roundDouble(rangeSize *  (accumulatedfrecuency + symbolFrequency) / totalFreq) -1;
	}

	public Symbol getSymbolFor(long number, Range range) {
		SpeakitLogger.Log("ProbabilityTable->getSymbolFor long: " + number + " ,initialFloor: " + range.getNumericFloor() + " ,rangeSize: " + range.getRangeSize());
		if (!(number >= range.getNumericFloor() && number <= range.getNumericRoof() + range.getRangeSize())) {
			throw new RuntimeException("El numero no va a caer dentro de ningun subintervalo del rango principal porque esta por afuera.");
		}
		int totalFreq = this.getTotalFrecuency(); 
		long accumulatedfrecuency = 0;
		for (SymbolFrequency symbolFrequency : this.symbolFrequencies) { 
			long[] newRange = calculateRange( totalFreq,accumulatedfrecuency,symbolFrequency.getFrequency(),range.getNumericFloor(),range.getRangeSize());			
			long floor = newRange[0];
			long roof= newRange[1];
			if (number >= floor && number <= roof) {
				SpeakitLogger.Log("Simbolo: (" + ((double)(number-floor)/(double)(roof-floor))*100+"%)" + symbolFrequency.getSymbol() + " " + floor + "-" + roof);
				return symbolFrequency.getSymbol();
			}
			accumulatedfrecuency += symbolFrequency.getFrequency();
		}
		SpeakitLogger.Log("ProbabilityTable->getSymbolFor long: " + number + " ,initialFloor: " + range.getFloor() + " ,rangeSize: " + range.getRangeSize());
		SpeakitLogger.Log("Acum frec: " + accumulatedfrecuency + ", acum proba: " + accumulatedfrecuency / totalFreq);
		SpeakitLogger.Log("Last floor: " + range.getFloor() + ", last roof: " + range.getRoof());
		throw new RuntimeException("Symbol not found");
	}

	public long roundDouble(double decimal) {
		BigDecimal bd = new BigDecimal(decimal);
		bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
		return bd.longValue();
	}

	public boolean contains(Symbol symbol) {
		SpeakitLogger.Log("ProbabilityTable->contains" + symbol);
		return (this.getSymbolIndex(symbol) >= 0);
	}

	public int getSymbolsQuantity() {
		return this.symbolFrequencies.size();
	}
 
	public String toString2() {
		String result = "";

		List<Symbol> symbols = this.getSymbols();
		for (Symbol symbol : symbols) {
			result += symbol.toString() + ":";
			result += this.getProbability(symbol) + "\n";
		}

		return result;
	}

	public void initAllSymbols() {
		SpeakitLogger.Log("ProbabilityTable->initAllSymbols");
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
		SpeakitLogger.Log("sort");
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
