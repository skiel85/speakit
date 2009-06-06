package speakit.compression.arithmetic;

import java.io.IOException;
import java.io.InputStream;

public class StreamBitReader extends BitReader {
	private final InputStream is;
	private byte currentByte;
	private byte currentByteBitCount;

	public StreamBitReader(InputStream is) {
		this.is = is;
		this.currentByte = 0x00;
		this.currentByteBitCount = 0;
	}

	@Override
	public Bit readBit() throws IOException {
		if (this.currentByteBitCount == 0) {
			this.currentByte = (byte) is.read();
			this.currentByteBitCount = 8;
		}

		boolean result = ((this.currentByte & 0x80) != 0x00);
		this.currentByte = (byte) (this.currentByte << 1);
		this.currentByteBitCount--;
		return new Bit(result);
	}

	public String read(int bitCount) throws IOException {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < bitCount; i++) {
			if (readBit().getBit()) {
				result.append('1');
			} else {
				result.append('0');
			}
		}
		return result.toString();
	}

	@Override
	public boolean hashNext() throws IOException {
		if (this.currentByteBitCount > 0) {
			return true;
		} else {
			return (this.is.available() > 0);
		}
	}

	@Override
	public void reset() throws IOException {
		is.reset();
	}

}
