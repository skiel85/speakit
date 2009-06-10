package speakit.compression.arithmetic;

import java.io.IOException;

public abstract class BitReader {

	private boolean	isEof	= false;

	public boolean isEOF() {
		return isEof;
	}

	public Bit[] readBits(int length) throws IOException {
		if (this.isEof) {
			throw new IOException("Eof reached");
		}
		Bit[] result = new Bit[length];
		for (int i = 0; i < length; i++) {
			Bit readBit = readBit();
			if (readBit != null) {
				result[i] = readBit;
			} else {
				isEof = true;
				return result;
			}
		}
		return result;
	}

	public String readToEnd() throws IOException {
		StringBuffer buffer = new StringBuffer();
		while (this.hashNext()) {
			buffer.append(this.readBit());
		}
		return buffer.toString();
	}

	public abstract Bit readBit() throws IOException;

	public abstract void reset() throws IOException;

	public abstract boolean hashNext() throws IOException;

}
