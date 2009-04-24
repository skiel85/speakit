package speakit.ftrs;

import java.util.ArrayList;
import java.util.Iterator;

import speakit.TextDocument;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.InvertedList;

//TODO Terminar de implementar
public class RankedSearchEngine {

	private final Index index;
	private int documentsLimit;
	private int minTermFrecuency;
	
	public RankedSearchEngine(Index index, int documentsLimit,int minTermFrecuency){
		this.index = index;
		documentsLimit=10;
		minTermFrecuency=0;
	}	
	
	/**
	 * Ejecuta una busqueda rankeada
	 * */
	ArrayList<Long> search(TextDocument searchText){
		//Obtiene la lista de listas invertidas del índice para la busqueda actual
		ArrayList<InvertedList> InvertedLists=getInvertedLists(searchText);
		//Ordena la lista de listas invertidas segun importancia del término
		ArrayList<InvertedList> orderedInvertedLists = sortByTermImportance(InvertedLists);
		//Arma la lista rankeada
		ArrayList<Long> resultDocumentIDs = makeRankedList(orderedInvertedLists);
		return resultDocumentIDs;		
	}

	/**
	 * Arma una lista rankeada
	 * @param orderedInvertedLists
	 * @return
	 */
	private ArrayList<Long> makeRankedList( ArrayList<InvertedList> orderedInvertedLists) {
		ArrayList<Long> resultDocumentIDs=new ArrayList<Long>(); 
		Iterator<InvertedList> InvertedListIterator = orderedInvertedLists.iterator();
		
		//recorre todas las listas invertidas, corta cuando no hay mas o bien cuando la lista de documentos encontrados es igual al límite de documentos definido 
		while(InvertedListIterator.hasNext() && resultDocumentIDs.size()<this.documentsLimit ){
			InvertedList current=InvertedListIterator.next();
			//ordena los elementos de la lista por peso global
			current.sortByWeight();
			//elimina las apariciones de términos que aparezcan menos de una cierta cantidad de veces
			current.truncateByFrecuency(this.minTermFrecuency);
			ArrayList<Long> documentIdsInList=current.getDocuments();
			for (int i = 0; i < documentIdsInList.size() && resultDocumentIDs.size()<this.documentsLimit; i++) {
				Long doc=documentIdsInList.get(i);
				//agrega cada id de documento una vez a la lista de resultado
				if(!resultDocumentIDs.contains(doc)){
					resultDocumentIDs.add(doc);	
				}				
			}
		}
		return resultDocumentIDs;
	}



	/**
	 * Ordena la lista por importancia de término
	 * */
	private ArrayList<InvertedList> sortByTermImportance(
			ArrayList<InvertedList> InvertedLists) { 
		return InvertedLists;
	}


	/**
	 * Obtiene la lista de listas invertidas para cada termino de la busqueda
	 * */
	private ArrayList<InvertedList> getInvertedLists(TextDocument searchText) {
		ArrayList<InvertedList> documentsForEachTerm=new ArrayList<InvertedList>();
		for(String word:searchText){ 
			InvertedList listForWord = this.index.getInvertedList(word);
			if(listForWord != null){
				documentsForEachTerm.add(listForWord);
			}
		}
		return documentsForEachTerm;
	}



	public void setTop(int top) {
		this.documentsLimit = top;
	}



	public int getTop() {
		return documentsLimit;
	}



	public void setMinTermFrecuency(int minTermFrecuency) {
		this.minTermFrecuency = minTermFrecuency;
	}



	public int getMinTermFrecuency() {
		return minTermFrecuency;
	}
}
