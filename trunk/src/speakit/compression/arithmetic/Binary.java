package speakit.compression.arithmetic;

/**
 * Provee funciones para trabajar convertir ints a binarios y viceversa
 * @author Nahuel
 *
 */
public class Binary {
	public static String integerToBinary(int num) {
		StringBuffer buffer = new StringBuffer();
		integerToBinary(buffer, num, 2);
		return buffer.toString();
	}

	public static void integerToBinary(StringBuffer output, int num, int base) {
		if (num > 0) { // Check to make sure integer is positive.
			integerToBinary(output, num / base, base); // These two lines do all
			// the work - the actual
			// recursion
			output.append(num % base);
		}
	}

	public static int bitStringToInt(String bits) {
		int result = 0;
		int pow=0;
		for (int i = bits.length()-1; i >= 0; i--) {
			char bit = bits.charAt(i);
			if (bit == '1') {
				result += Math.pow(2, pow);
			}
			pow++;
		}
		return result;
	}

}
