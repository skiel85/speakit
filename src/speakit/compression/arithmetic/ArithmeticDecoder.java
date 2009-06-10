package speakit.compression.arithmetic;

import java.io.IOException;
import java.io.Reader;

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
		System.out.println("Decoding...  ");		
		// esto mueve la ventana que inspecciona el archivo comprimido
		slideWindow();
		System.out.println("*Prev window: " + this.currentWindow.getBits() + "(" + this.currentWindow.getNumber() + ")");

//		double probability = range.getProbabilityFor(currentWindow.getNumber());
//		Symbol decodedSymbol = table.getSymbolFor(probability);
		Symbol decodedSymbol=table.getSymbolFor(currentWindow.getNumber(),range.getNumericFloor(),range.getRangeSize());
		System.out.println("--gotcha: " + decodedSymbol + "\n");
		
		range.zoomIn(table.getProbabilityUntil(decodedSymbol), table.getProbability(decodedSymbol));
		currentBuffer = range.emissionBuffer;
		previousFlush = range.flush();

		System.out.println("*Pos window:  " + this.currentWindow.getBits() + "(" + this.currentWindow.getNumber() + ")");
		System.out.println("decoded: " + decodedSymbol + "\n");
		return decodedSymbol;
	}

	public String	currentBuffer	= "";
	private String	previousFlush	= "";

	private void slideWindow() throws IOException {
		if (previousFlush.length() > 0) {
			// muevo la ventana para quitar los bits de overflow
			// como la ventana ya se movió cuando hubo underflow, debo
			// desplazarme un poco menos
			int shiftSize = previousFlush.length() - this.previousUnderflow;
			currentWindow = currentWindow.shiftLeft(shiftSize, 0, this.input);
			previousUnderflow = 0;
		}
		if (range.getUnderflowCount() > 0) {
			// elimino de la ventana los bits de underflow
			previousUnderflow = range.getUnderflowCount();
			currentWindow = currentWindow.shiftLeft(previousUnderflow, 1, this.input);
		}
	}

}
