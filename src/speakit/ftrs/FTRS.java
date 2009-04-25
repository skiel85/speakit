package speakit.ftrs;

import java.io.IOException;

import speakit.TextDocument;
import speakit.documentstorage.TextDocumentList;

public interface FTRS {
	/**
	 * Devuelve un listado con los documentos a partir de una consulta
	 * @param searchText
	 * @return
	 * @throws IOException 
	 */
	TextDocumentList search(TextDocument searchText) throws IOException;
	
	/**
	 * Indexa una lista de documentos
	 * @param documentList
	 */
	public void indexDocuments(TextDocumentList documentList);
	
	public void indexDocuments(TextDocument textDocument);
}
