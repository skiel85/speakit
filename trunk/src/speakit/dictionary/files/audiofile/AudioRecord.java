package speakit.dictionary.files.audiofile;

import speakit.dictionary.files.Record;
import speakit.dictionary.serialization.ByteArrayField;
import speakit.dictionary.serialization.LongField;

/**
 * Registro de audio.
 */
public class AudioRecord extends Record<LongField> {

	private LongField offset = new LongField();
	private ByteArrayField audio = new ByteArrayField();

	/**
	 * Construye un nuevo registro de audio.
	 */
	public AudioRecord() {
		this.setKey(this.offset);
		this.addField(this.audio);
	}

	@Override
	public void notifyOffsetChanged(long offset) {
		this.offset.setLong(offset);
	}

	/**
	 * Construye un nuevo registro de audio a partir de un arreglo de bytes.
	 * 
	 * @param audio
	 *            Arreglo de bytes que contiene al audio.
	 */
	public AudioRecord(byte[] audio) {
		this();
		this.audio.setBytes(audio);
	}

	/**
	 * Obtiene la posición del audio.
	 * 
	 * @return
	 */
	public long getOffset() {
		return this.offset.getLong();
	}

	/**
	 * Establece la posición del audio.
	 * 
	 * @param audio
	 */
	public void setOffset(long offset) {
		this.offset.setLong(offset);
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

}
