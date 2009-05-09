package speakit.ftrs;

import java.io.IOException;

import speakit.TextDocument;
import speakit.documentstorage.TextDocumentList;
import speakit.io.File;

public interface FTRS extends File {
	/**
	 * Devuelve un listado con los documentos a partir de una consulta
	 * 
	 * @param searchText
	 * @return
	 * @throws IOException
	 */
	TextDocumentList search(TextDocument searchText) throws IOException;

	/**
	 * Indexa una lista de documentos
	 * 
	 * @param documentList
	 * @throws IOException
	 */
	public void indexDocuments(TextDocumentList documentList) throws IOException;

	public void indexDocument(TextDocument textDocument) throws IOException;

	public String printIndexForDebug();
	
}
