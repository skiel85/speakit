package speakit.documentstorage;

import java.io.ByteArrayOutputStream;

import speakit.io.record.ByteArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.OffsetRecord;
import speakit.io.record.StringField;

public class DocumentRecord extends OffsetRecord {

	private StringField text = new StringField();
	private IntegerField compressorType = new IntegerField();
	private ByteArrayField compressedDocument = new ByteArrayField();
	
	
	/**
	 * Obtiene el compresor usado.
	 * 
	 * @return
	 */
	public IntegerField getCompressor() {
		return compressorType;
	}


	/**
	 * Establece el compresor a usar.
	 * 
	 * @return
	 */
	public void setCompressor(IntegerField compressor) {
		this.compressorType = compressor;
	}


	/**
	 * Obtiene los bytes del documento comprimido.
	 * 
	 * @return
	 */
	public ByteArrayField getCompressedDocument() {
		return compressedDocument;
	}


	/**
	 * Establece los bytes del documento comprimido.
	 * 
	 * @return
	 */
	public void setCompressedDocument(ByteArrayField compressedDocument) {
		this.compressedDocument = compressedDocument;
	}

	
	
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
	 * Construye un nuevo registro de audio a partir de los bytes del documento
	 * y el compresor usado.
	 * 
	 * @param audio
	 *            Arreglo de bytes que contiene al audio.
	 */
	public DocumentRecord(byte[] compressedDocument, int compressor) {
		this();
		this.compressorType.setInteger(compressor);
		this.compressedDocument.setBytes(compressedDocument);
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
		return new Field[] {this.compressedDocument,this.compressorType};
	}
	
	@Override
	protected String getStringRepresentation() {
		return "AudioRecord{id:"+this.getOffset()+",text:"+this.text.toString()+"}";
	}

}
