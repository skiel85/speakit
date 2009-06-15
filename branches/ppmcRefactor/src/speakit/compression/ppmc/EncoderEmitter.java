package speakit.compression.ppmc;

import java.io.IOException;

import speakit.SpeakitLogger;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class EncoderEmitter implements Emitter {

	private final ArithmeticEncoder encoder;

	@Override
	public boolean emitSymbol(ProbabilityTable table, Symbol sym) throws IOException {
		boolean foundInTable = false;
		if (table.contains(sym)) {
			// Emito el caracter y actualizo la probabilidad del
			// caracter en este contexto
			foundInTable = true;
		} else {
			// Emito un escape
			sym = Symbol.getEscape();
		}
		SpeakitLogger.Log(sym.toString() + "[" + table.getProbability(sym) + "]");
		SpeakitLogger.activate();
		encoder.encode(sym, table);
		SpeakitLogger.deactivate();
		return foundInTable;
	}
	
	public EncoderEmitter(ArithmeticEncoder encoder) {
		this.encoder = encoder;
	}
}
