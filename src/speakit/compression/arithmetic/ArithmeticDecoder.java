package speakit.compression.arithmetic;

import java.io.IOException;

import speakit.SpeakitLogger;

public class ArithmeticDecoder {

	private Range		range;
	private Binary		currentWindow;
	private final int	precision;

	public ArithmeticDecoder(BitReader input, int precision) {
		this.precision = precision;
		this.input = input;
	}

	private BitReader	input;
	int					previousUnderflow	= 0;

	/**
	 * 
	 * @param table
	 * @return
	 * @throws IOException
	 */

	public Symbol decode(ProbabilityTable table) throws IOException {
		if (this.range == null) {
			this.range = new Range(precision);
		}
		if (this.currentWindow == null) {
			currentWindow = Binary.createFromReader(this.input, precision);
		}
		SpeakitLogger.Log("Decoding...  ");		
		// esto mueve la ventana que inspecciona el archivo comprimido
		
		SpeakitLogger.Log("*Prev window: " + this.currentWindow.getBits() + "(" + this.currentWindow.getNumber() + ")");
		SpeakitLogger.Log("previousFlush: " + this.previousFlush);
		SpeakitLogger.Log("previousUnderflow: " + this.previousUnderflow);
		slideWindow();
		SpeakitLogger.Log("*Pos window:  " + this.currentWindow.getBits() + "(" + this.currentWindow.getNumber() + ")");
//		
		
		Symbol decodedSymbol=table.getSymbolFor(currentWindow.getNumber(),range.getNumericFloor(),range.getRangeSize());
		SpeakitLogger.Log("--gotcha: " + decodedSymbol + "\n");
		
		range.zoomIn(table.getProbabilityUntil(decodedSymbol), table.getProbability(decodedSymbol));
		previousFlush = range.flush();

		
		SpeakitLogger.Log("decoded: " + decodedSymbol + "\n");
		return decodedSymbol;
	}
 
	private String	previousFlush	= "";
	private int bitsDiscardedByUnderflow = 0;

	private void slideWindow() throws IOException {
		if (previousFlush.length() > 0) {
			// muevo la ventana para quitar los bits de overflow
			// como la ventana ya se movió cuando hubo underflow, debo
			// desplazarme un poco menos
			int shiftSize = previousFlush.length() - this.previousUnderflow;
			
			Emitter discardedBitsEmitter = new Emitter();
			discardedBitsEmitter.setUnderflowCount(previousUnderflow);
			discardedBitsEmitter.emitOverflow(currentWindow.getBits().substring(0,shiftSize));
			String discardedBits = discardedBitsEmitter.flush(); 
			
			currentWindow = currentWindow.shiftLeft(shiftSize, 0, this.input);
			
			if (!previousFlush.equals(discardedBits)) {
				int a = 1;
				a++;
			}
			
//			if (previousUnderflow == 0) {
//				
//				Assert.assertEquals(previousFlush, currentWindow.getBits().substring(0, previousFlush.length()));
//			} else {
//				//Assert.assertEquals(previousFlush., currentWindow.getBits().substring(0, previousFlush.length()-previousUnderflow));
//			}
			bitsDiscardedByUnderflow=0;
			previousUnderflow = 0;
		}
		if (range.getUnderflowCount() > 0) {
			// elimino de la ventana los bits de underflow
			previousUnderflow = range.getUnderflowCount();
			currentWindow = currentWindow.shiftLeft(previousUnderflow-bitsDiscardedByUnderflow, 1, this.input);
			bitsDiscardedByUnderflow+=previousUnderflow;
		}
	}

}
