package speakit.dictionary.files.audioindexfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.serialization.LongField;
import speakit.dictionary.serialization.StringField;

/**
 * Registro de índice del archivo de registros de audio.
 */
public class AudioIndexRecord implements Record {

	private LongField offset;
	private StringField word;

	/**
	 * Crea un nuevo registro de índice.
	 */
	public AudioIndexRecord() {
		this.word = new StringField();
		this.offset = new LongField();
	}

	/**
	 * Crea un nuevo registro de índice a partir de una palabra y un offset.
	 * 
	 * @param word
	 *            Palabra.
	 * @param offset
	 *            Offset donde se encuentra el audio de la palabra en el archivo
	 *            de registros de audio.
	 */
	public AudioIndexRecord(String word, long offset) {
		this.word = new StringField(word);
		this.offset = new LongField(offset);
	}

	/**
	 * Compara un registro de índice con otro.
	 */
	@Override
	public int compareTo(Record o) {
		return this.word.compareTo(((AudioIndexRecord) o).word);
	}

	/**
	 * Deserializa un registro de índice.
	 */
	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		long byteCount = 0;
		StringField word = new StringField();
		LongField offset = new LongField();
		try {
			byteCount += word.deserialize(stream);
			byteCount += offset.deserialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
		this.word.setString(word.getString());
		this.offset.setLong(offset.getLong());
		return byteCount;
	}

	/**
	 * Obtiene el offset.
	 * 
	 * @return
	 */
	public long getOffset() {
		return this.offset.getLong();
	}

	/**
	 * Obtiene la palabra.
	 * 
	 * @return
	 */
	public String getWord() {
		return this.word.getString();
	}

	/**
	 * Serializa un registro de índice.
	 */
	@Override
	public long serialize(OutputStream stream) throws RecordSerializationException {
		long byteCount = 0;
		try {
			byteCount += this.word.serialize(stream);
			byteCount += this.offset.serialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
		return byteCount;
	}

	/**
	 * Establece el offset.
	 * 
	 * @param offset
	 */
	public void setOffset(long offset) {
		this.offset.setLong(offset);
	}

	/**
	 * Establece la palabra.
	 * 
	 * @param word
	 */
	public void setWord(String word) {
		this.word.setString(word);
	}

}
