package speakit.compression.arithmetic;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Provee funciones para trabajar convertir ints a binarios y viceversa
 * 
 * @author Nahuel
 * 
 */
public class Binary {

	private final String	bits;
	private final int		precision;
	private int				number;

	public Binary(String bits, int precision) {

		this.bits = Binary.alignRight(bits, precision);
		this.precision = this.bits.length();
		this.number = Binary.bitStringToInt(bits);
	}

	public Binary(int number, int precision) {
		this.precision = precision;
		this.number = number;
		this.bits = Binary.alignRight(Binary.integerToBinary(number), precision);
	}

	public static Binary createFromReader(StringReader reader, int precision) throws IOException {
		char[] buffer = new char[precision];
		reader.read(buffer, 0, precision);
		return new Binary(new String(buffer), precision);
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
				throw new IllegalArgumentException("El " + num + " es mas grande que lo soportado por la precicion que es de " + precision + " bits");
			} else {
				return num;
			}
		}

	}

	public static String integerToBinary(int num) {
		StringBuffer buffer = new StringBuffer();
		integerToBinary(buffer, num, 2);
		return buffer.toString();
	}

	private static void integerToBinary(StringBuffer output, int num, int base) {
		if (num > 0) { // Check to make sure integer is positive.
			integerToBinary(output, num / base, base); // These two lines do all
			// the work - the actual
			// recursion
			output.append(num % base);
		}
	}

	public static int bitStringToInt(String bits) {
		int result = 0;
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

	public int getNumber() {
		return number;
	}

	// /**
	// * mueve todos los bits a la izquierda y agrega al final los bits deseados
	// *
	// * @param array
	// * @param shiftSize
	// * @param completionBit
	// * @return
	// */
	// private String shiftLeft(String array, int shiftSize, int startPos,
	// String completionBit) {
	// String shifted = "";
	// for (int i = 0; i < array.length(); i++) {
	// if (i >= startPos) {
	// if (i + shiftSize < array.length()) {
	// shifted += array.charAt(i + shiftSize);
	// } else {
	// shifted += completionBit;
	// }
	// } else {
	// shifted += array.charAt(i);
	// }
	//
	// }
	// return shifted;
	// }

	public Binary shiftLeft(int shiftSize, int startPos, Reader reader) throws IOException {
		String shifted = "";
		for (int i = 0; i < this.bits.length(); i++) {
			if (i >= startPos) {
				if (i + shiftSize < bits.length()) {
					shifted += bits.charAt(i + shiftSize);
				} else {
					shifted += (char) reader.read();
				}
			} else {
				shifted += bits.charAt(i);
			}

		}
		return new Binary(shifted, this.precision);
	}

	public String getBits() {
		return this.bits;
	}

}
