package speakit.compression.arithmetic;

import java.io.IOException;


public class ConstantBitReader extends BitReader {

	private final boolean	alwaysZero;

	public ConstantBitReader(boolean alwaysZero) {
		this.alwaysZero = alwaysZero;
	}

	@Override
	public Bit readBit() {
		return new Bit(!alwaysZero);
	}

	@Override
	public void reset() throws IOException {
		//no hace nada	
	}

	@Override
	public boolean hashNext() {
		return true;
	}

}
