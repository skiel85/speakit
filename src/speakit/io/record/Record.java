package speakit.io.record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Representa un registro. Un objeto serializable y comparable.
 * 
 */
public class Record<KEYTYPE extends Field> implements Comparable<Record<KEYTYPE>> {
	private KEYTYPE key;
	private ArrayList<Field> fields = new ArrayList<Field>();

	/**
	 * Establece el campo clave.
	 * 
	 * @param key
	 *            Campo clave a establecer.
	 */
	protected void setKey(KEYTYPE key) {
		this.key = key;
	}

	/**
	 * Agrega un campo al registro.
	 * 
	 * @param field
	 *            Campo a agregar.
	 */
	protected void addField(Field field) {
		this.fields.add(field);
	}

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
			for (Field field : this.fields) {
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
			for (Field field : this.fields) {
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
		return this.key.compareTo(o.key);
	}

	/**
	 * Compara la clave de un registro con un campo.
	 */
	public int compareToKey(KEYTYPE key) {
		return this.key.compareTo(key);
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
}
