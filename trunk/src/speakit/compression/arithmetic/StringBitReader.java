package speakit.compression.arithmetic;

import java.io.IOException;


public class StringBitReader extends BitReader {

	private String	input;
	private int		index	= -1;
	public StringBitReader(String input) {
		this.input = input;
		reset();
	}

	@Override
	public Bit readBit() {
		index++;
		return new Bit(input.charAt(index));
	}

	@Override
	public void reset() {
		index=-1;
	}

	@Override
	public boolean hashNext() throws IOException {
		return index<input.length()-1;
	}

}
