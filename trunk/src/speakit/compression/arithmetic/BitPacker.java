package speakit.compression.arithmetic;

import java.util.ArrayList;
import java.util.List;

public class BitPacker {
	private List<Byte> packedBytes = new ArrayList<Byte>();
	private byte currentByte = 0;
	private byte bitCount = 0;

	public void pack(String bits) {
		for (int i = 0; i < bits.length(); i++) {
			if (bits.charAt(i) == '0') {
				pack(false);
			} else {
				pack(true);
			}
		}
	}

	public void pack(boolean bit) {
		currentByte = (byte) ((0xff & currentByte) << 1);
		if (bit) {
			currentByte |= 0x01;
		}
		bitCount++;
		if (bitCount == 8) {
			packedBytes.add(new Byte(currentByte));
			bitCount = 0;
			currentByte &= 0x00;
		}
	}

	public void end() {
		this.pack(true);
		for (int i = bitCount; i < 8; i++) {
			this.pack(false);
		}
	}

	public List<Byte> flush() {
		List<Byte> result = packedBytes;
		packedBytes = new ArrayList<Byte>();
		return result;
	}
}
