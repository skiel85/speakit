package speakit.ftrs;

import java.io.IOException;
import java.util.ArrayList;

import speakit.TextDocument;
import speakit.documentstorage.DocumentRepository;
import speakit.documentstorage.TextDocumentList;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.IndexRecord;

public class FTRSImpl implements FTRS {

	protected DocumentRepository repository;
	protected Index index; 

	public FTRSImpl() {
		index = new Index(); 
	}

	@Override
	public TextDocumentList search(TextDocument searchText) throws IOException {
		/*RankedSearchEngine searchEngine = new RankedSearchEngine(this.index,
				10, 0);
		List<Long> documentIds = searchEngine.search(this.applyFilters(searchText));

		TextDocumentList result = new TextDocumentList();
		for (Long docId : documentIds) {
			result.add(repository.getById(docId));
		}
		return result;
		*/
		return null;
	}

	public DocumentRepository getDocumentRepository() {
		return repository;
	}
	
	/**
	 * Aplica filtros al documento, tales como eliminar stopwords
	 */
	private TextDocument applyFilters(TextDocument searchText) {
		return searchText;
	}

	private Index getIndex() {
		if (this.index == null)
			this.index = new Index();
		return index;
	} 

	@Override
	public void indexDocuments(TextDocumentList documentList) {
		//getIndex().
	}

	@Override
	public void indexDocuments(TextDocument textDocument) {
		IndexRecordGenerator generator = new IndexRecordGenerator();
		//a modo de prueba, agrego un document, este metodo seguramente no tenga sentido
		TextDocument cleanDocument = applyFilters(textDocument);
		generator.addSingleDocument(cleanDocument);
		ArrayList<IndexRecord> records = generator.generateNewRegisters();
		getIndex().updateRecords(records);
	}

}
