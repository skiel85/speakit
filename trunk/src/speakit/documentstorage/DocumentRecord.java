package speakit.documentstorage;

import speakit.io.LongField;
import speakit.io.Record;
import speakit.io.StringField;

public class DocumentRecord extends Record<LongField> {

	private LongField offset = new LongField();
	private StringField text = new StringField();

	/**
	 * Construye un nuevo registro de audio.
	 */
	public DocumentRecord() {
		this.setKey(this.offset);
		this.addField(this.text);
	}

	@Override
	public void notifyOffsetChanged(long offset) {
		this.offset.setLong(offset);
	}

	/**
	 * Construye un nuevo registro de audio a partir del texto de un documento.
	 * 
	 * @param audio
	 *            Arreglo de bytes que contiene al audio.
	 */
	public DocumentRecord(String text) {
		this();
		this.text.setString(text);
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
	 * Obtiene el texto del documento.
	 * 
	 * @return
	 */
	public String getText() {
		return this.text.getString();
	}

	/**
	 * Establece el texto del documento.
	 * 
	 * @param audio
	 */
	public void setText(String text) {
		this.text.setString(text);
	}

}
