package speakit.io;

public class ByteArrayConverter {
	/**
	 * Convierte un array de bytes a un int. Cada byte del array formará parte
	 * del int, el 1er byte será el que ocupe los primeros 8 bits del int, el
	 * segundo ocupará los proximos 8 y así sucesivamente.
	 * 
	 * Codificado en big-endian, pues en el byte[0] está el byte mas
	 * significativo
	 * 
	 * @param b
	 * @return
	 */
	public static int toInt(byte[] b, int offset) {
		if (b == null || b.length < 4) {
			return 0;
		}
		// el & 0xff es para limpiar posible basura en memoria
		return (0xff & (b[0 + offset])) << 24 | // XXXX XXXX|0000 0000|0000
				// 0000|0000 0000
				(0xff & (b[1 + offset])) << 16 | // 0000 0000|XXXX XXXX|0000
				// 0000|0000 0000
				(0xff & (b[2 + offset])) << 8 | // 0000 0000|0000 0000|XXXX
				// XXXX|0000 0000
				(0xff & (b[3 + offset])) << 0; // 0000 0000|0000 0000|0000
		// 0000|XXXX XXXX

	}

	/**
	 * Convierte un array de bytes en un int.
	 * 
	 * @param data
	 * @return
	 */
	public static int toInt(byte[] data) {
		return toInt(data, 0);
	}

	/**
	 * Convierte un array de bytes a long.
	 * 
	 * @param b
	 * @return
	 */
	public static long toLong(byte[] b) {
		if (b == null || b.length < 8) {
			return 0L;
		}

		return (long) (0xff & b[0]) << 56 | (long) (0xff & b[1]) << 48 | (long) (0xff & b[2]) << 40 | (long) (0xff & b[3]) << 32 | (long) (0xff & b[4]) << 24 | (long) (0xff & b[5]) << 16 | (long) (0xff & b[6]) << 8 | (long) (0xff & b[7]) << 0;
	}

	/**
	 * Convierte un int en un array de bytes
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] toByteArray(int data) {
		return new byte[] { (byte) ((data >> 24) & 0xff), (byte) ((data >> 16) & 0xff), (byte) ((data >> 8) & 0xff), (byte) ((data >> 0) & 0xff), };
	}

	/**
	 * Convierte un long en un array de bytes
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] toByteArray(long data) {
		return new byte[] { (byte) ((data >> 56) & 0xff), (byte) ((data >> 48) & 0xff), (byte) ((data >> 40) & 0xff), (byte) ((data >> 32) & 0xff), (byte) ((data >> 24) & 0xff), (byte) ((data >> 16) & 0xff), (byte) ((data >> 8) & 0xff), (byte) ((data >> 0) & 0xff), };
	}

	public static String toString(byte[] data) {
		return (data == null) ? null : new String(data);
	}

	public static boolean toBoolean(byte[] data) {
		return (data == null) ? false : (data[0] == 1);
	}

	public static byte[] toByteArray(boolean data) {
		return new byte[] { (byte) ((data == true) ? 1 : 0) };
	}
}
