package speakit.compression.ppmc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import speakit.SpeakitLogger;
import speakit.compression.arithmetic.ArithmeticDecoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class DecoderEmitter implements Emitter {
	
	private ArithmeticDecoder decoder;
	OutputStreamWriter writer;

	@Override
	public boolean emitSymbol(ProbabilityTable table, SymbolWrapper sym)
			throws IOException {
		SpeakitLogger.activate();
		Symbol decodedSymbol=decoder.decode(table);
		SpeakitLogger.deactivate();
		boolean foundInTable=false;
		
		if (!decodedSymbol.equals(Symbol.getEscape())){
			if (!decodedSymbol.equals(Symbol.getEof())) {
				writer.write(decodedSymbol.getChar());
				//originalDocument.append(decodedSymbol.getChar());
			}
			foundInTable=true;
		} 
		
		return foundInTable;
	}
	
	public DecoderEmitter(ArithmeticDecoder decoder, OutputStream outStream )
	{
		this.writer= new OutputStreamWriter(outStream);
		this.decoder=decoder;
	}

}
