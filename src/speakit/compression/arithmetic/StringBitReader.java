package speakit.compression.arithmetic;

import java.io.IOException;


public class StringBitReader extends BitReader {

	private String	input;
	private int		index	= 0;
	public StringBitReader(String input) {
		this.input = input;
	}

	@Override
	public Bit readBit() {
		return new Bit(input.charAt(index++));
	}

	@Override
	public void reset() {
		index=0;
	}

	@Override
	public boolean hashNext() throws IOException {
		return index<input.length();
	}

}
