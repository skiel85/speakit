package speakit.compression.arithmetic;

import java.io.IOException;

import speakit.SpeakitLogger;

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
		SpeakitLogger.Log("Codif: " + symbol);
		if(range ==null){
			range = new Range((byte) precision);
		}
		range.zoomIn(table.getProbabilityUntil(symbol), table.getProbability(symbol));
		if (symbol.equals(Symbol.getEof())) {
			range.emitEnding();// emito el piso del rango
		}
		String flush = range.flush();
		output.write(flush);
	} 
}