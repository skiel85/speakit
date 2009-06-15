package speakit.compression.lzp;

import java.util.HashMap;

import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class LzpProbabilityTable {
	//protected ProbabilityTable probabilityTable;
	protected HashMap<Symbol, Integer> symbols;
	
	public LzpProbabilityTable() {
		symbols = new HashMap<Symbol, Integer>();
	}

	public ProbabilityTable getProbabilityTable() {
		ProbabilityTable table = ensambleTable(); 
		return table;
	}

	private ProbabilityTable ensambleTable() {
		ProbabilityTable table = new ProbabilityTable();
		table.initAllSymbols();
		if (symbols.isEmpty()) {
			Iterable<Symbol> keySet = symbols.keySet();
			for (Symbol symbol : keySet) {
				table.increment(symbol, symbols.get(symbol));
			}
		}
		return table;
	}

	public void increment(Symbol actualSymbol) {
		Integer value = 0;
		if (symbols.containsKey(actualSymbol))
			value = symbols.get(actualSymbol);
		symbols.put(actualSymbol, value + 1);
	}	
	
	
}
