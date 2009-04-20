package speakit.ftrs;

import speakit.documentstorage.DocumentList;

public interface FTRSInterface {
	/**
	 * Devuelve un listado con los documentos q contienen la palabra pasada como parametro
	 * 
	 * @param word palabra presente en le consulta
	 * @return DocumentList
	 */
	public DocumentList getDocumentsFor(String word);
	
}
