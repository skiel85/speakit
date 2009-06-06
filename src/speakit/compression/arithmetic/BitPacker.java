package speakit.compression.arithmetic;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class BitPacker {
	private List<Byte> packedBytes = new ArrayList<Byte>();
	private byte lastByteBitCount = 0;
	private byte firstByteBitCount = 0;

	public BitPacker() {
		packedBytes.add(new Byte((byte) 0x00));
	}

	private void incrementBitCount() {
		lastByteBitCount++;
		if (packedBytes.size() == 1) {
			firstByteBitCount++;
		}
	}

	private void decrementBitCount() {
		firstByteBitCount--;
		if (packedBytes.size() == 1) {
			lastByteBitCount--;
		}
	}

	private byte getFirstByte() {
		return packedBytes.get(0).byteValue();
	}

	private byte getLastByte() {
		return packedBytes.get(packedBytes.size() - 1).byteValue();
	}

	private void setLastByte(byte binary) {
		packedBytes.set(packedBytes.size() - 1, new Byte(binary));
	}

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
		addByteIfLastIsFull();
		byte lastByte = getLastByte();
		setLastByte(setBit(lastByte, 7 - lastByteBitCount, bit));
		incrementBitCount();
	}

	private void addByteIfLastIsFull() {
		if (lastByteBitCount == 8 || packedBytes.size()==0) {
			packedBytes.add(new Byte((byte) 0x00));
			lastByteBitCount = 0;
		}
	}

	private void removeFirstByteIfEmpty() {
		if (firstByteBitCount == 0) {
			packedBytes.remove(0);
			firstByteBitCount = 8;
		}
	}

	public boolean unpack() {
		byte firstByte = getFirstByte();
		decrementBitCount();
		boolean bit = getBit(firstByte, firstByteBitCount);
		removeFirstByteIfEmpty();
		return bit;
	}

	public void end() {
		this.pack(true);
		for (int i = lastByteBitCount; i < 8; i++) {
			this.pack(false);
		}
	}

//	private void alignLeft() {
//		byte currentByte = getFirstByte();
//		for (int byteCounter = 0; byteCounter < packedBytes.size(); byteCounter++) {
//			byte nextByte = packedBytes.get(byteCounter).byteValue();
//			for (byte bitNumber = 0; bitNumber < firstByteBitCount; bitNumber++) {
//				nextByte = setBit(nextByte, bitNumber, false);
//			}
//			for (byte bitNumber = firstByteBitCount; bitNumber < 8; bitNumber++) {
//				currentByte = setBit(currentByte, bitNumber, false);
//			}
//			currentByte |= nextByte;
//			packedBytes.set(byteCounter, currentByte);
//			
//			lastByteBitCount -= (8-firstByteBitCount);
//			
//		}
//
//	}

	public List<Byte> flush() {
		List<Byte> result = packedBytes;
		packedBytes = new ArrayList<Byte>();
		return result;
	}

	private boolean getBit(byte binary, int bitNumber) {
		int posMask = (int) Math.pow(2, bitNumber);
		return ((binary & posMask) == posMask);
	}

	private byte setBit(byte binary, int bitNumber, boolean value) {
		int posMask = (int) Math.pow(2, bitNumber);
		if (value) {
			return (byte) (binary | posMask);
		} else {
			return (byte) (binary & ~posMask);
		}
	}
}
