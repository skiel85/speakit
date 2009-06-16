package speakit.ftrs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.documentstorage.DocumentRepository;
import speakit.documentstorage.TextDocumentList;
import speakit.ftrs.index.InvertedIndex;
import speakit.ftrs.index.InvertedIndexRecord;
import speakit.ftrs.indexer.InvertedIndexRecordGenerator;
import speakit.wordreader.TextCleaner;

public class FTRSImpl implements FTRS {

	protected DocumentRepository repository;
	protected InvertedIndex index;
	protected StopWordsFilter stopWordsFilter;

	public FTRSImpl() {
		index = new InvertedIndex();
		repository = new DocumentRepository();
		stopWordsFilter = new StopWordsFilter();
	}

	@Override
	public TextDocumentList search(TextDocument searchText) throws IOException {
		TextDocument cleanSearch = this.applyFilters(searchText);
		
		RankedSearchEngine searchEngine = new RankedSearchEngine(this.index, 10, 0);
		List<Long> documentIds = searchEngine.search(cleanSearch);

		TextDocumentList result = new TextDocumentList();
		for (Long docId : documentIds) {
			result.add(repository.getByIdOfCompressedDocument(docId));
		}

		return result;

	}

	public DocumentRepository getDocumentRepository() {
		return repository;
	}

	/**
	 * Devuelve un documento con todas las palabras limpias y con los stop words eliminados
	 * @param textDocument
	 * @return
	 * @throws IOException 
	 */
	public TextDocument applyFilters(TextDocument textDocument) throws IOException {
		TextCleaner textCleaner = new TextCleaner();
		TextDocument cleanDocument = textCleaner.cleanDocument(textDocument);
		return stopWordsFilter.getRelevantWords(cleanDocument);
	}

	private InvertedIndex getIndex() {
		if (this.index == null)
			this.index = new InvertedIndex();
		return index;
	}

	@Override
	public void indexDocuments(TextDocumentList documentList) throws IOException {
		InvertedIndexRecordGenerator generator = new InvertedIndexRecordGenerator();
		for (TextDocument textDocument : documentList) {
			this.repository.store(textDocument);
			TextDocument cleanDocument = applyFilters(textDocument);
			generator.addSingleDocument(cleanDocument);
		}
		ArrayList<InvertedIndexRecord> records = generator.generateNewRegisters();
		// TODO faltaría mergear los index records preexistentes con los nuevos
		// index records
		getIndex().updateRecords(records);
	}

	@Override
	public void indexDocument(TextDocument textDocument) throws IOException {
		this.repository.store(textDocument);
		InvertedIndexRecordGenerator generator = new InvertedIndexRecordGenerator();
		// a modo de prueba, agrego un document, este metodo seguramente no
		// tenga sentido
		TextDocument cleanDocument = applyFilters(textDocument);
		generator.addSingleDocument(cleanDocument);
		ArrayList<InvertedIndexRecord> records = generator.generateNewRegisters();
		// TODO faltaría mergear los index records preexistentes con los nuevos
		// index records
		getIndex().updateRecords(records);
	}

	
	@Override
	public void indexDocuments(TextDocumentList documentList, int compressor) throws IOException {
		InvertedIndexRecordGenerator generator = new InvertedIndexRecordGenerator();
		for (TextDocument textDocument : documentList) {
			this.repository.store(textDocument, compressor);
			TextDocument cleanDocument = applyFilters(textDocument);
			generator.addSingleDocument(cleanDocument);
		}
		ArrayList<InvertedIndexRecord> records = generator.generateNewRegisters();
		// TODO faltaría mergear los index records preexistentes con los nuevos
		// index records
		getIndex().updateRecords(records);
	}

	@Override
	public void indexDocument(TextDocument textDocument, int compressor) throws IOException {
		this.repository.store(textDocument, compressor);
		InvertedIndexRecordGenerator generator = new InvertedIndexRecordGenerator();
		// a modo de prueba, agrego un document, este metodo seguramente no
		// tenga sentido
		TextDocument cleanDocument = applyFilters(textDocument);
		generator.addSingleDocument(cleanDocument);
		ArrayList<InvertedIndexRecord> records = generator.generateNewRegisters();
		// TODO faltaría mergear los index records preexistentes con los nuevos
		// index records
		getIndex().updateRecords(records);
	}
	
	
	@Override
	public void load(FileManager filemanager, Configuration conf) throws IOException {
		this.index.load(filemanager, conf);
		this.repository.load(filemanager, conf);
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		this.repository.install(filemanager, conf);
		this.index.install(filemanager, conf);
		this.stopWordsFilter.install(filemanager, conf);
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return this.index.isInstalled(filemanager) & this.repository.isInstalled(filemanager) && this.stopWordsFilter.isInstalled(filemanager);
	}

	@Override
	public String printIndexForDebug() {
		return index.toString();
	}

	@Override
	public ArrayList<String> getInvalidWordsForSearch(TextDocument consultation, FileManager fileManager, Configuration configuration) {
		ArrayList<String> ignored = new ArrayList<String>();
		for (String word : consultation) {
			if (stopWordsFilter.isStopWord(word))
				ignored.add(word);
		}
		return ignored;
	}

}
