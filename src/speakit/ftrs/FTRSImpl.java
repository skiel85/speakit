package speakit.ftrs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import speakit.Configuration;
import speakit.FileManager;
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
		repository=new DocumentRepository();
	}

	@Override
	public TextDocumentList search(TextDocument searchText) throws IOException {
		
		 RankedSearchEngine searchEngine = new RankedSearchEngine(this.index, 10, 0); 
		 List<Long> documentIds = searchEngine.search(searchText);
		 
		 TextDocumentList result = new TextDocumentList(); 
		 for (Long docId : documentIds) { result.add(repository.getById(docId)); } 
		 
		 return result;
		 
	}

	public DocumentRepository getDocumentRepository() {
		return repository;
	}


	private TextDocument applyFilters(TextDocument textDocument){
		return textDocument;
	}
	
	private Index getIndex() {
		if (this.index == null)
			this.index = new Index();
		return index;
	}

	@Override
	public void indexDocuments(TextDocumentList documentList) throws IOException {
		for(TextDocument doc:documentList){
			indexDocuments(doc);
		}
	}

	@Override
	public void indexDocuments(TextDocument textDocument) throws IOException {
		this.repository.store(textDocument);
		IndexRecordGenerator generator = new IndexRecordGenerator();
		// a modo de prueba, agrego un document, este metodo seguramente no
		// tenga sentido
		TextDocument cleanDocument = applyFilters(textDocument);
		generator.addSingleDocument(cleanDocument);
		ArrayList<IndexRecord> records = generator.generateNewRegisters();
		//TODO faltaría mergear los index records preexistentes con los nuevos index records 
		getIndex().updateRecords(records);
	} 

	@Override
	public void load(FileManager filemanager) throws IOException {
		this.index.load(filemanager);
		this.repository.load(filemanager);
	} 

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		this.repository.install(filemanager, conf);
		this.index.install(filemanager,conf);
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return this.index.isInstalled(filemanager) &  this.repository.isInstalled(filemanager);
	}

}
