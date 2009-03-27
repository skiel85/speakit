package speakit.dictionary.serialization;

import java.io.IOException;
import java.io.InputStream;

public class PrimitiveTypeReader {
	InputStream in = null;

	public PrimitiveTypeReader(InputStream in) {
		this.in = in;
	}

	private byte[] readBytes(int count) throws IOException {
		byte[] data = new byte[count];
		in.read(data);
		return data;
	}

	public int readInt() throws IOException {
		return ByteArrayConverter.toInt(readBytes(Integer.SIZE / 4));
	}

	public long readLong() throws IOException {
		return ByteArrayConverter.toLong(readBytes(Long.SIZE / 4));
	}

	public double readDouble() throws IOException {
		return ByteArrayConverter.toDouble(readBytes(Double.SIZE / 4));
	}

	public float readFloat() throws IOException {
		return ByteArrayConverter.toFloat(readBytes(Float.SIZE / 4));
	}

	public byte readByte() throws IOException {
		return ByteArrayConverter.toByte(readBytes(Byte.SIZE / 4));
	}

	// public String readString() throws IOException{
	// return Converter.toString(readBytes(Byte.SIZE/4));
	// }

}
