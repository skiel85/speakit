package speakit.dictionary.audiofile;

import speakit.io.record.ByteArrayField;
import speakit.io.record.Field;
import speakit.io.record.OffsetRecord;

/**
 * Registro de audio.
 */
public class AudioRecord extends OffsetRecord {

	private ByteArrayField audio = new ByteArrayField();

	/**
	 * Construye un nuevo registro de audio
	 */
	public AudioRecord() {
		// Dejado intencionalmente en blanco.
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

	@Override
	protected Field[] getFields() {
		return new Field[] {this.audio}; 
	}

}
