package speakit.compression.ppmc;

import java.io.IOException;

import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public interface Emitter {
	boolean emitSymbol(ProbabilityTable table, Symbol sym) throws IOException;
}
