package speakit.compression.ppmc;

import speakit.compression.arithmetic.Symbol;

public class SymbolWrapper {
	private Symbol symbol;

	public SymbolWrapper(Symbol sym) {
		this.symbol = sym;
	}

	public Symbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
}
