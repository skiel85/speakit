package speakit.dictionary.audioindexfile;

import speakit.io.record.Field;
import speakit.io.record.LongField;
import speakit.io.record.Record;
import speakit.io.record.StringField;

/**
 * Registro de índice del archivo de registros de audio.
 */
public class AudioIndexRecord extends Record<StringField> {

	private StringField word = new StringField();
	private LongField offset = new LongField();

	/**
	 * Construye un nuevo registro de audio.
	 */
	public AudioIndexRecord() {
		// Dejado intencionalmente en blanco.
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
		this();
		this.word.setString(word);
		this.offset.setLong(offset);
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

	@Override
	protected Field[] getFields() {
		return new Field[] { this.word, this.offset };
	}

	@Override
	protected StringField getKey() {
		return this.word;
	}

}
