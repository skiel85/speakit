package speakit.documentstorage;

import java.io.File;
import java.io.IOException;

import speakit.TextDocument;
import speakit.io.record.LongField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.SecuentialRawRecordFile;

//TODO implementar esta clase
public class DocumentRepository implements RecordFactory<DocumentRecord> {
	private SecuentialRawRecordFile<DocumentRecord> recordFile;

	public void load(File file) throws IOException {
		this.recordFile = new SecuentialRawRecordFile<DocumentRecord>(file, this);
	}

	/**
	 * Obtiene un documento a partir de su id
	 * 
	 * @param docId
	 * @return
	 * @throws IOException
	 */
	public TextDocument getById(Long docId) throws IOException {
		DocumentRecord record = recordFile.getRecord(new LongField(docId));
		if (record == null) {
			return null;
		} else {
			return new TextDocument(record.getText());
		}
	}

	/**
	 * Almacena un documento
	 * 
	 * @param doc
	 * @return
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public Long store(TextDocument doc) throws IOException {
		DocumentRecord record = new DocumentRecord(doc.getText());
		recordFile.insertRecord(record);
		return record.getOffset();
	}

	@Override
	public DocumentRecord createRecord() {
		return new DocumentRecord();
	}

}
