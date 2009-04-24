package speakit.ftrs;

import java.util.ArrayList;

import speakit.TextDocument;
import speakit.documentstorage.Document;
import speakit.documentstorage.DocumentList;
import speakit.ftrs.index.InvertedList;

public interface FTRS {
	/**
	 * Devuelve un listado con los documentos q contienen la palabra pasada como
	 * parametro
	 * 
	 * @param word
	 *            palabra presente en le consulta
	 * @return DocumentList
	 */
	public DocumentList search(String word);
	
	public InvertedList getRankedList(ArrayList<InvertedList> invertedLists);
	
	/**
	 * Indexa una lista de documentos
	 * @param documentList
	 */
	public void indexDocuments(ArrayList<TextDocument> documentList);
	
	public void indexDocuments(TextDocument textDocument);
}
