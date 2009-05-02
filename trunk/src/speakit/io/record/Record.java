package speakit.io.record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Representa un registro. Un objeto serializable y comparable.
 * 
 */
public abstract class Record<KEYTYPE extends Field> implements Comparable<Record<KEYTYPE>> {
	protected abstract KEYTYPE getKey();

	protected abstract Field[] getFields();

	/**
	 * Serializa el registro escribiéndolo en un stream de salida.
	 * 
	 * @param stream
	 *            Stream de salida.
	 * @return Cantidad de bytes escrito.
	 * @throws RecordSerializationException
	 */
	public long serialize(OutputStream stream) throws RecordSerializationException {
		long byteCount = 0;
		try {
			for (Field field : this.getFields()) {
				byteCount += field.serialize(stream);
			}
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
		return byteCount;
	}

	/**
	 * Deserializa el registro a partir de un stream de entrada.
	 * 
	 * @param stream
	 *            Stream de entrada.
	 * @return Cantidad de bytes leido.
	 * @throws RecordSerializationException
	 */
	public long deserialize(InputStream stream) throws RecordSerializationException {
		long byteCount = 0;
		try {
			for (Field field : this.getFields()) {
				byteCount += field.deserialize(stream);
			}
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
		return byteCount;
	}

	/**
	 * Compara un registro con otro.
	 */
	@Override
	public int compareTo(Record<KEYTYPE> o) {
		return this.getKey().compareTo(o.getKey());
	}

	/**
	 * Compara la clave de un registro con un campo.
	 */
	public int compareToKey(KEYTYPE key) {
		return this.getKey().compareTo(key);
	}

	public byte[] serialize() throws IOException, RecordSerializationException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.serialize(out);
		return out.toByteArray();
	}

	public void deserialize(byte[] data) throws IOException, RecordSerializationException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		this.deserialize(in);
	}

	protected Field[] JoinFields(Field[] f1, Field[] f2) {
		Field[] f = new Field[f1.length + f2.length];
		System.arraycopy(f1, 0, f, 0, f1.length);
		System.arraycopy(f2, 0, f, f1.length, f2.length);
		return f;
	}
}
