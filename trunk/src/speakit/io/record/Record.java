package speakit.io.record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un registro. Un objeto serializable y comparable. Para extender
 * Record es necesario sobreescribir el metodo getKey y getFields.
 * 
 */
public abstract class Record<KEYTYPE extends Field> implements Comparable<Record<KEYTYPE>> {

	/**
	 * @return el campo que hará de clave. El campo devuelto no se serializará a
	 *         menos que haya sido incluido en la lista de getFields().
	 */
	public abstract KEYTYPE getKey();

	/**
	 * @return el arreglo de campos que se quiere sean serializables. Si el
	 *         campo clave tambien se quiere serializar se debe incluir en esta
	 *         lista.
	 */
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
	 * Serializa el registro y devuelve la serialización en varias partes de tamaño partSize, tantas como necesite.
	 * 
	 * @param partSize tamaño de cada parte expresado en bytes. Debe ser >0
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public List<byte[]> serializeInParts(int partSize) throws RecordSerializationException, IOException {
		if(!(partSize>0)){
			throw new IllegalArgumentException("PartSize debe ser > 0.");
		}
		List<byte[]> parts = new ArrayList<byte[]>();
		ByteArrayInputStream stream = new ByteArrayInputStream(this.serialize());
		while (stream.available() > 0) {
			byte[] serializationPart = new byte[partSize];
			stream.read(serializationPart, 0, partSize);
			parts.add(serializationPart);
		}
		return parts;
	}
	
	/**
	 * Deserializa el registro a partir de un array de partes de serializacion. Primero concatena los bytes de todas las partes y despues delega la deserialización al metodo deserialize 
	 * @param serializationParts
	 * @throws IOException
	 */
	public void deserializeFromParts(List<byte[]> serializationParts) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		for (byte[] bytes : serializationParts) {
			stream.write(bytes);
		}
		this.deserialize(stream.toByteArray());
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

	public final byte[] serialize() throws IOException, RecordSerializationException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.serialize(out);
		return out.toByteArray();
	}

	public final void deserialize(byte[] data) throws IOException, RecordSerializationException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		this.deserialize(in);
	}
	
	@Override
	public String toString() {
		String stringRepresention = this.getStringRepresentation();
		int i=0;
		if(stringRepresention.length()==0){
			String res = "Record (" +this.getClass().getSimpleName() +"): {";
			for (Field field : this.getFields()) {
				res+= (i!=0?",":"") + field.toString();
				i++;
			}
			res+= "}";
			return res;
		}else{
			return stringRepresention;
		}
		
	}

	protected abstract String getStringRepresentation();
}
