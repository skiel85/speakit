package speakit.ftrs;

import speakit.TextDocument;
import speakit.documentstorage.TextDocumentList;

public interface FTRS {
	/**
	 * Devuelve un listado con los documentos a partir de una consulta
	 * @param searchText
	 * @return
	 */
	TextDocumentList search(TextDocument searchText);
	
	/**
	 * Indexa una lista de documentos
	 * @param documentList
	 */
	public void indexDocuments(TextDocumentList documentList);
	
	public void indexDocuments(TextDocument textDocument);
}
