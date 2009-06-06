package speakit.compression.arithmetic;

import java.io.IOException;
import java.io.OutputStream;

public class BitWriterImpl {
	private final OutputStream os;
	private byte currentByte;
	private byte currentByteBitCount;

	public BitWriterImpl(OutputStream os) {
		this.os = os;
		this.currentByte = 0x00;
		this.currentByteBitCount = 0;
	}

	public void write(boolean bit) throws IOException {
		this.currentByte = (byte) (this.currentByte << 1);
		if (bit) {
			this.currentByte = (byte) (this.currentByte | 0x01);
		}
		this.currentByteBitCount++;
		
		if (this.currentByteBitCount == 8) {
			os.write(new byte[] { this.currentByte });
			this.currentByte = 0x00;
			this.currentByteBitCount = 0;
		}
	}

	public void write(String bits) throws IOException {
		for (int i = 0; i < bits.length(); i++) {
			if (bits.charAt(i) == '0') {
				this.write(false);
			} else {
				this.write(true);
			}
		}
	}

	public void close() throws IOException {
		this.write(true);
		for (int i = this.currentByteBitCount; i < 8; i++) {
			this.write(false);
		}
	}
}
