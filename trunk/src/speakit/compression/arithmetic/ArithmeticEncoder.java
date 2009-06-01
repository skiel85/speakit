package speakit.compression.arithmetic;

import java.io.IOException;

public class ArithmeticEncoder {

	private final BitWriter	bitWriter;
	private Range	range;
	private final int	precicion;

	public ArithmeticEncoder(BitWriter bitWriter,int precicion) {
		this.bitWriter = bitWriter;
		this.precicion = precicion; 
		range = new Range((byte) precicion);
	}

	public void encode(Symbol symbol, ProbabilityTable table) throws IOException {
		if(symbol.equals(Symbol.getEof())){
			range.emitEnding();//emito el piso del rango
		}else{
			range.zoomIn(table.getProbabilityUntil(symbol), table.getProbability(symbol));
		}
		bitWriter.write(range.flush());
	} 

	public Symbol decode(ProbabilityTable table) {
		return null;
	}

}