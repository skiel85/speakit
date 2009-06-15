package speakit.compression.arithmetic;

import java.io.IOException;

/**
 * Provee funciones para trabajar convertir long a binarios y viceversa
 * 
 * @author Nahuel
 * 
 */
public class Binary {

	private final String	bits;
	private final int		precision;
	private long				number;

	public Binary(String bits, int precision) {
		for (int i = 0; i < bits.length(); i++) {
			if (bits.charAt(i) != '0' && bits.charAt(i) != '1') {
				throw new RuntimeException("Se esperaba un string con muchos 1 o 0, pero vino un:  " + bits.charAt(i));
			}
		}
		this.bits = Binary.alignRight(bits, precision);
		this.precision = this.bits.length();
		this.number = Binary.bitStringToNumber(bits);
	}

	public Binary(long number, int precision) {
		this.precision = precision;
		this.number = number;
		this.bits = Binary.alignRight(Binary.numberToBinary(number), precision);
	}

	public static Binary createFromReader(BitReader reader, int precision) throws IOException {
		return new Binary(new String(Bit.toCharArray(reader.readBits(precision))), precision);
	}

	public static String repeat(char charToRepeat, int count) {
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < count; j++) {
			buffer.append(charToRepeat);
		}
		return buffer.toString();
	}

	private static String alignRight(String num, int precision) {
		if (num.length() < precision) {
			return repeat('0', precision - num.length()) + num;
		} else {
			if (num.length() > precision) {
				throw new IllegalArgumentException("El " + num + " tiene " + num.length() + " bits, es mas grande que lo soportado por la precisión que es de " + precision
						+ " bits");
			} else {
				return num;
			}
		}

	}

	public static String numberToBinary(long num) {
		StringBuffer buffer = new StringBuffer();
		numberToBinary(buffer, num, 2);
		return buffer.toString();
	}

	private static void numberToBinary(StringBuffer output, long num, int base) {
		if (num > 0) {
			numberToBinary(output, num / base, base);
			output.append(num % base);
		}
	}

	public static long bitStringToNumber(String bits) {
		long result = 0;
		int pow = 0;
		for (int i = bits.length() - 1; i >= 0; i--) {
			char bit = bits.charAt(i);
			if (bit == '1') {
				result += Math.pow(2, pow);
			}
			pow++;
		}
		return result;
	}

	public long getNumber() {
		return number;
	}

	public Binary shiftLeft(int shiftSize, int startPos, BitReader reader) throws IOException {
		String shifted = "";
		for (int i = 0; i < this.bits.length(); i++) {
			if (i >= startPos) {
				if (i + shiftSize < bits.length()) {
					shifted += bits.charAt(i + shiftSize);
				} else {
					if (reader.hashNext()) {
						shifted += reader.readBit();
					}
					else {
						throw new RuntimeException("Fin de archivo: No deberian seguirse pidiendo bits.");
					}
				}
			} else {
				shifted += bits.charAt(i);
			}

		}
		return new Binary(shifted, this.precision);
		
		//Otra implementación:
		//--------------------
//		StringBuffer shifted = new StringBuffer();
//		for (int i = 0; i < startPos; i++) {
//			shifted.append(bits.charAt(i));
//		}
//		for (int i = startPos + shiftSize; i < bits.length(); i++) {
//			shifted.append(bits.charAt(i));
//		}
//		for (int i = bits.length(); i < shiftSize && reader.hashNext(); i++) {
//			reader.readBit();
//		}
//		for (int i = shifted.length(); i < bits.length() && reader.hashNext(); i++) {
//			shifted.append(reader.readBit());
//		}
//		if (shifted.length() < bits.length()) {
//			throw new RuntimeException("Fin de archivo: No deberian seguirse pidiendo bits.");
//		}
//		return new Binary(shifted.toString(), this.precision);
	}

	public String getBits() {
		return this.bits;
	}
	
	@Override
	public String toString() {
		return getBits() + "(" + this.number +")";
	}

}
