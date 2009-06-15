package speakit.compression.ppmc;

import java.io.IOException;

import speakit.compression.arithmetic.ProbabilityTable;

public interface Emitter {
	boolean emitSymbol(ProbabilityTable table, SymbolWrapper sym) throws IOException;
}
