package speakit.compression.arithmetic;

import java.io.IOException;

public class ArithmeticEncoder {
	private final BitWriter	bitWriter;
	private Range			range;
	private final int		precicion;
	public ArithmeticEncoder(BitWriter bitWriter, int precicion) {
		this.bitWriter = bitWriter;
		this.precicion = precicion;
	}

	public void encode(Symbol symbol, ProbabilityTable table) throws IOException {
		if(range ==null){
			range = new Range((byte) precicion);
		}
		range.zoomIn(table.getProbabilityUntil(symbol), table.getProbability(symbol));
		if (symbol.equals(CreateEof())) {//TODO: corregir \n por EOF
			range.emitEnding();// emito el piso del rango
		}
		bitWriter.write(range.flush());
	}

	public static Symbol CreateEof() {
//		return new Symbol(ArithmeticEncoder.EOF);
		return Symbol.getEof();
	}

}