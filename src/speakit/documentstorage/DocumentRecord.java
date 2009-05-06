package speakit.documentstorage;

import speakit.io.record.Field;
import speakit.io.record.OffsetRecord;
import speakit.io.record.StringField;

public class DocumentRecord extends OffsetRecord {

	private StringField text = new StringField();

	/**
	 * Construye un nuevo registro de audio.
	 */
	public DocumentRecord() {
		//Dejado intencionalmente en blanco.
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

	@Override
	protected Field[] getFields() {
		return new Field[] {this.text};
	}
	
	@Override
	protected String getStringRepresentation() {
		return "AudioRecord{id:"+this.getOffset()+",text:"+this.text.toString()+"}";
	}

}
