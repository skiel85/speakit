package speakit.compression.ppmc;

import java.io.IOException;

import speakit.SpeakitLogger;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class EncoderEmitter implements Emitter {

	private final ArithmeticEncoder encoder;

	@Override
	public boolean emitSymbol(ProbabilityTable table, SymbolWrapper sym) throws IOException {
		boolean foundInTable = false;
		Symbol emitSym = sym.getSymbol();
		if (table.contains(emitSym)) {
			// Emito el caracter y actualizo la probabilidad del
			// caracter en este contexto
			foundInTable = true;
		} else {
			// Emito un escape
			emitSym = Symbol.getEscape();
		}
		//SpeakitLogger.activate();
		SpeakitLogger.Log(emitSym.toString() + "[" + table.getProbability(emitSym) + "]");
		//SpeakitLogger.deactivate();
		//SpeakitLogger.activate();
		encoder.encode(emitSym, table);
		//SpeakitLogger.deactivate();
		return foundInTable;
	}
	
	public EncoderEmitter(ArithmeticEncoder encoder) {
		this.encoder = encoder;
	}
}
