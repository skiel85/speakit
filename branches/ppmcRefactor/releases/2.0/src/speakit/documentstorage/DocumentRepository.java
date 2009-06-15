package speakit.documentstorage;

import java.io.IOException;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.io.File;
import speakit.io.record.LongField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.OffsetRecordFile;

//TODO implementar esta clase
public class DocumentRepository implements File, RecordFactory {
	private OffsetRecordFile<DocumentRecord> recordFile;

	@Override
	public void load(FileManager fileManager, Configuration conf) throws IOException {
		this.recordFile = new OffsetRecordFile<DocumentRecord>(fileManager.openFile("DocumentRepository.dat"), this);
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
			return new TextDocument(record.getOffset(),record.getText());
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
	public void store(TextDocument doc) throws IOException {
		DocumentRecord record = new DocumentRecord(doc.getText());
		recordFile.insertRecord(record);
		doc.setId(record.getOffset());
	}

	@Override
	public DocumentRecord createRecord() {
		return new DocumentRecord();
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		this.load(filemanager, conf);
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return true;
	}
}
