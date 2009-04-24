package speakit.ftrs;

import java.util.ArrayList;

import speakit.TextDocument;
import speakit.documentstorage.DocumentList;
import speakit.documentstorage.DocumentRepository;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.IndexRecord;
import speakit.ftrs.index.InvertedList;

public class FTRSImpl implements FTRS {

	protected DocumentRepository repository;
	protected Index index;

	@Override
	public DocumentList search(String word) {
		Index index = getIndex();
		if (index.exists(word)) {
			InvertedList invertedList = index.getInversedList(word);
			DocumentRepository repository = getDocumentRepository();
			DocumentList result = repository.getDocumentList(invertedList);
			return result;
		} else {
			return null;
		}

	}

	private DocumentRepository getDocumentRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	private Index getIndex() {
		if (this.index == null)
			this.index = new Index();
		return index;
	}

	@Override
	public InvertedList getRankedList(ArrayList<InvertedList> invertedLists) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void indexDocuments(ArrayList<TextDocument> documentList) {
		//getIndex().
	}

	@Override
	public void indexDocuments(TextDocument textDocument) {
		IndexRecordGenerator generator = new IndexRecordGenerator();
		//a modo de prueba, agrego un document, este metodo seguramente no tenga sentido
		generator.addSingleDocument(textDocument);
		ArrayList<IndexRecord> records = generator.generateNewRegisters();
		getIndex().updateRecords(records);
	}

}
