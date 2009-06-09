package speakit.compression.arithmetic;

public class SymbolRange implements Comparable<SymbolRange> {
	private final Symbol firstSymbol;
	private final Symbol lastSymbol;

	public SymbolRange(Symbol firstSymbol, Symbol lastSymbol) {
		this.firstSymbol = firstSymbol;
		this.lastSymbol = lastSymbol;
	}

	public SymbolRange[] split(Symbol symbol) {
		if (firstSymbol.equals(lastSymbol)) {
			return new SymbolRange[] { new SymbolRange(firstSymbol, lastSymbol) };
		} else if (firstSymbol.next().equals(lastSymbol)) {
			return new SymbolRange[] { new SymbolRange(firstSymbol, firstSymbol), new SymbolRange(lastSymbol, lastSymbol) };
		} else {
			return new SymbolRange[] { new SymbolRange(firstSymbol, symbol.previous()), new SymbolRange(symbol, symbol), new SymbolRange(symbol.next(), lastSymbol) };
		}
	}

	@Override
	public int compareTo(SymbolRange o) {
		return (firstSymbol.compareTo(o.firstSymbol));
	}

}
