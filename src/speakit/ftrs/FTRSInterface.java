package speakit.ftrs;

import java.util.ArrayList;

import speakit.documentstorage.DocumentList;
import speakit.ftrs.index.InversedList;

public interface FTRSInterface {
	/**
	 * Devuelve un listado con los documentos q contienen la palabra pasada como parametro
	 * 
	 * @param word palabra presente en le consulta
	 * @return DocumentList
	 */
	public DocumentList search(String word) ;
	
	public InversedList getRankedList(ArrayList<InversedList> inversedLists);
}	
