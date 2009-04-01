package speakit.dictionary.serialization;

public class ByteArrayConverter {
	/**
	 * Convierte un array de bytes a un int. Cada byte del array formará parte
	 * del int, el 1er byte será el que ocupe los primeros 8 bits del int, el
	 * segundo ocupará los proximos 8 y así sucesivamente.
	 * 
	 * @param b
	 * @return
	 */

	/**
	 * Convierte un array de bytes a long. Lo hace en base a toInt.
	 * 
	 * @param b
	 * @return
	 */

	public static byte[] toByta(int data) {
		return new byte[]{(byte) ((data >> 24) & 0xff), (byte) ((data >> 16) & 0xff), (byte) ((data >> 8) & 0xff), (byte) ((data >> 0) & 0xff),};
	}

	public static byte[] toByta(long data) {
		return new byte[]{(byte) ((data >> 56) & 0xff), (byte) ((data >> 48) & 0xff), (byte) ((data >> 40) & 0xff), (byte) ((data >> 32) & 0xff), (byte) ((data >> 24) & 0xff),
				(byte) ((data >> 16) & 0xff), (byte) ((data >> 8) & 0xff), (byte) ((data >> 0) & 0xff),};
	}
	
	public static int toInt(byte[] data) {
		if (data == null || data.length != 4)
			return 0x0;
		// ----------
		return (int) (
		(0xff & data[0]) << 24 | (0xff & data[1]) << 16 | (0xff & data[2]) << 8 | (0xff & data[3]) << 0);
	}


	public static long toLong(byte[] data) {
		if (data == null || data.length != 8)
			return 0x0;
		// ----------
		return (long) (
		(long) (0xff & data[0]) << 56 | (long) (0xff & data[1]) << 48 | (long) (0xff & data[2]) << 40 | (long) (0xff & data[3]) << 32 | (long) (0xff & data[4]) << 24
				| (long) (0xff & data[5]) << 16 | (long) (0xff & data[6]) << 8 | (long) (0xff & data[7]) << 0);
	}

	public static String toString(byte[] data) {
		return (data == null) ? null : new String(data);
	}
}
