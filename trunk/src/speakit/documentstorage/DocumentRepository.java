package speakit.documentstorage;

import java.io.File;
import java.io.IOException;

import speakit.TextDocument;
import speakit.dictionary.files.RecordFactory;
import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.files.SecuentialRecordFile;
import speakit.dictionary.serialization.LongField;

//TODO implementar esta clase
public class DocumentRepository implements RecordFactory<DocumentRecord>{
	private SecuentialRecordFile<DocumentRecord, LongField> recordFile;
	
	/**
	 * Crea un archivo de registros de documentos.
	 * 
	 * @param file
	 *            Archivo.
	 * @throws IOException
	 */
	public DocumentRepository() throws IOException {
	}
	
	public void load(File file) throws IOException {
		this.recordFile = new SecuentialRecordFile<DocumentRecord, LongField>(file, this);
	}

	/**
	 * Obtiene un documento a partir de su id
	 * @param docId
	 * @return
	 * @throws IOException
	 */
	public TextDocument getById(Long docId) throws IOException{
		DocumentRecord record = recordFile.getRecord(new LongField(docId));
		if (record==null){
			return null;
		}else{
			return new TextDocument( record.getText());	
		}
	}
	
	/**
	 * Almacena un documento
	 * @param doc
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public Long store(TextDocument doc) throws IOException{
		DocumentRecord record = new DocumentRecord(doc.getText());
		recordFile.insertRecord(record);
		return record.getOffset();
	}


	@Override
	public DocumentRecord createRecord() {
		return new DocumentRecord();
	}


}
