package speakit.compression.arithmetic;

import java.io.IOException;
import java.io.StringReader;

public class ArithmeticDecoder {

	private Range		range;
	private Binary		currentWindow;
	private final int	precision;

	public ArithmeticDecoder(StringReader input, int precision) {
		this.precision = precision;
		this.range = new Range(precision);
		this.input = input;
	}

	private StringReader	input;
	int						previousUnderflow	= 0;

	/**
	 * 
	 * @param table
	 * @return
	 * @throws IOException
	 */
	public Symbol decode(ProbabilityTable table) throws IOException {
		if (this.currentWindow == null) {
			currentWindow = Binary.createFromReader(this.input, precision);
		}
		double probability = range.getProbabilityFor(currentWindow.getNumber());
		Symbol decodedSymbol = table.getSymbolFor(probability);
		range.zoomIn(table.getProbabilityUntil(decodedSymbol), table.getProbability(decodedSymbol));

		if (range.getUnderflowCount() > 0) {
			// elimino de la ventana los bits de underflow
			previousUnderflow = range.getUnderflowCount();
			currentWindow = currentWindow.shiftLeft(previousUnderflow, 1, this.input);
		} else {
			String flush = range.flush();
			if (flush.length() > 0) {
				// muevo la ventana para quitar los bits de overflow
				// como la ventana ya se movió cuando hubo underflow, debo
				// desplazarme un poco menos
				int shiftSize = flush.length() - this.previousUnderflow;
				currentWindow = currentWindow.shiftLeft(shiftSize, 0, this.input);
				previousUnderflow = 0;
			}
		}
		return decodedSymbol;
	}

}
