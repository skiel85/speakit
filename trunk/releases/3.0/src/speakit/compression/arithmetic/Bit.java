package speakit.compression.arithmetic;

public class Bit {
	private final boolean	value;
	private char	b;
	public Bit(char b) {
		this.b = b;
		value = (b == '1') ? true : false;
		if (b != '0' && b != '1') {
			throw new IllegalArgumentException("Se esperaba 0 o 1 pero vino " + b);
		}
	}

	public Bit(boolean bit) {
		this.b = toChar(bit);
		this.value = bit;
	}

	public boolean getBit() {
		return this.value;
	}

	public char getChar() {
		return b;
	}

	private char toChar(boolean bit) {
		return (bit) ? '1' : '0';
	}

	public Bit not() {
		return new Bit(!this.value);
	}

	@Override
	public String toString() {
		return "" + getChar();
	}
	
	public static char[] toCharArray(Bit[] bits){
		char[] result = new char[bits.length];
		for (int i = 0; i < bits.length; i++) {
			result[i]=bits[i].getChar();
		}
		return result;
	}
}
