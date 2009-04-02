package speakit.dictionary.files.audiofile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.serialization.ByteArrayField;

/**
 * Registro de audio.
 */
public class AudioRecord implements Record {

	private ByteArrayField audio;

	/**
	 * Construye un nuevo registro de audio.
	 */
	public AudioRecord() {
		this.audio = new ByteArrayField();
	}

	/**
	 * Construye un nuevo registro de audio a partir de un arreglo de bytes.
	 * 
	 * @param audio
	 *            Arreglo de bytes que contiene al audio.
	 */
	public AudioRecord(byte[] audio) {
		this.audio = new ByteArrayField(audio);
	}

	/**
	 * Obtiene los bytes del audio.
	 * 
	 * @return
	 */
	public byte[] getAudio() {
		return this.audio.getBytes();
	}

	/**
	 * Establece los bytes del audio.
	 * 
	 * @param audio
	 */
	public void setAudio(byte[] audio) {
		this.audio.setBytes(audio);
	}

	/**
	 * Deserializa un registro de audio.
	 */
	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		try {
			return this.audio.deserialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
	}

	/**
	 * Serializa un registro de audio.
	 */
	@Override
	public long serialize(OutputStream stream) throws RecordSerializationException {
		try {
			return this.audio.serialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
	}

	/**
	 * Compara un registro de audio con otro.
	 */
	@Override
	public int compareTo(Record o) {
		if (o instanceof AudioRecord) {
			AudioRecord other = (AudioRecord) o;
			return this.audio.compareTo(other.audio);
		} else {
			return this.getClass().toString().compareTo(o.getClass().toString());
		}
	}

}
