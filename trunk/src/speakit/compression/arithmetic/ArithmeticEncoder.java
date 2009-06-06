package speakit.compression.arithmetic;

import java.io.IOException;

public class ArithmeticEncoder {
	private final BitWriter	output;
	private Range			range;
	private final int		precision;
	
	public ArithmeticEncoder(BitWriter output, int precision) {
		this.output = output;
		this.precision = precision;
	}

	/**
	 * Codifica un caracter y escribe en el output cuando cree necesario
	 * @param symbol
	 * @param table
	 * @throws IOException
	 */
	public void encode(Symbol symbol, ProbabilityTable table) throws IOException {
		if(range ==null){
			range = new Range((byte) precision);
		}
		range.zoomIn(table.getProbabilityUntil(symbol), table.getProbability(symbol));
		if (symbol.equals(Symbol.getEof())) {//TODO: corregir \n por EOF
			range.emitEnding();// emito el piso del rango
		}
		output.write(range.flush());
	}

//	public static Symbol CreateEof() {
////		return new Symbol('F');
//		return Symbol.getEof();
//	}

}