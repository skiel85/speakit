package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Field implements Comparable<Field> {

	/**
	 * Tamaño de un byte expresado en bits
	 */
	protected static final int BYTE_SIZE = 8;

	/**
	 * Lee tantos bytes como la cantidad de bytes necesarios para serializarse
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	protected byte[] readBytes(InputStream in) throws IOException {
		return readBytes(in, this.getSerializationSize());
	}

	/**
	 * Lee una cantidad específica de bytes de un inputStream
	 * 
	 * @param in
	 * @param count
	 * @return
	 * @throws IOException
	 */
	protected byte[] readBytes(InputStream in, int length) throws IOException {
		byte[] data = new byte[length];
		in.read(data);
		return data;
	}

	/**
	 * Delega al metodo actuallyDeserialize
	 * 
	 * @param in
	 * @return cantidad de bytes leidos desde in
	 * @throws IOException
	 */
	public long deserialize(InputStream in) throws IOException {
		this.actuallyDeserialize(in);
		return (long) this.getSerializationSize();
	}

	/**
	 * Delega al metodo actuallySerialize
	 * 
	 * @param out
	 * @return cantidad de bytes escritos en out
	 * @throws IOException
	 */
	public long serialize(OutputStream out) throws IOException {
		this.actuallySerialize(out);
		return (long) this.getSerializationSize();
	}

	/**
	 * @return la cantidad de bytes que ocupa la serializacion de su valor
	 * @throws IOException 
	 * @throws RecordSerializationException 
	 */
	public abstract int getSerializationSize() throws RecordSerializationException, IOException;

	/**
	 * Hidrata el objeto
	 * 
	 * @param in
	 * @throws IOException
	 */
	protected abstract void actuallyDeserialize(InputStream in) throws IOException;

	/**
	 * Persiste el objeto como bytes
	 * 
	 * @param out
	 * @throws IOException
	 */
	protected abstract void actuallySerialize(OutputStream out) throws IOException;

	@Override
	public int compareTo(Field o) {
		if (this.getClass() == o.getClass()) {
			return compareToSameClass(o);
		} else {
			return this.getClass().toString().compareTo(o.getClass().toString());
		}
	}

	protected abstract int compareToSameClass(Field o);
	
	public static Field[] JoinFields(Field[] f1, Field[] f2) {
		Field[] f = new Field[f1.length + f2.length];
		System.arraycopy(f1, 0, f, 0, f1.length);
		System.arraycopy(f2, 0, f, f1.length, f2.length);
		return f;
	}
	
	@Override
	public String toString() {
		return this.getStringRepresentation();
	}
	
	protected abstract String getStringRepresentation();
}