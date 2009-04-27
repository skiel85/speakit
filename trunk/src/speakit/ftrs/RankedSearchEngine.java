package speakit.ftrs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import speakit.TextDocument;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.IndexRecord;
import speakit.ftrs.index.InvertedList;

//TODO Terminar de implementar
public class RankedSearchEngine {

	private final Index index;
	private int resultItemsCount;
	private int minTermFrecuency;

	public RankedSearchEngine(Index index, int documentsLimit, int minTermFrecuency) {
		this.index = index;
		this.resultItemsCount = documentsLimit;
		this.minTermFrecuency = minTermFrecuency;
	}

	/**
	 * Ejecuta una busqueda rankeada
	 * */
	public List<Long> search(TextDocument query) {
		// Obtiene la lista de listas invertidas del índice para la busqueda actual
		List<IndexRecord> indexedRecords = getIndexRecords(query);
		// Ordena la lista de listas invertidas segun importancia del término
		List<IndexRecord> orderedIndexedRecords = sortByTermImportance(indexedRecords);
		// Arma la lista rankeada
		List<Long> resultDocumentIDs = makeRankedList(orderedIndexedRecords);
		return resultDocumentIDs;
	}

	/**
	 * Arma una lista rankeada
	 * 
	 * @param orderedInvertedLists
	 * @return
	 */
	private List<Long> makeRankedList(List<IndexRecord> orderedInvertedLists) {
		List<Long> resultDocumentIDs = new ArrayList<Long>();
		Iterator<IndexRecord> eachIndexedTermIterator = orderedInvertedLists.iterator();

		// recorre todas las listas invertidas, corta cuando no hay mas o bien
		// cuando la lista de documentos encontrados es igual al límite de
		// documentos definido
		while (eachIndexedTermIterator.hasNext() && resultDocumentIDs.size() < this.resultItemsCount) {
			IndexRecord eachIndexedTerm = eachIndexedTermIterator.next();
			//ordena los elementos de la lista invertida por frecuencia, luego 
			//trunca la lista por frecuencia de aparicion del término en cada documento
			InvertedList truncatedList = eachIndexedTerm.getInvertedList().sortByFrecuency().truncateByFrecuency(this.minTermFrecuency);
			appendDocumentsTo(resultDocumentIDs, truncatedList,this.resultItemsCount);
		}
		return resultDocumentIDs;
	}

	/**
	 * Agrega los documentos desde "from" al final de la lista "documentList". Los items los agrega por unica vez. Si la cantidad de elementos de "documentList" alcanza limit, inmediatamente deja de agregar elementos. 
	 * @param to
	 * @param from
	 */
	private void appendDocumentsTo(List<Long> documentList, InvertedList from, int limit) {
		List<Long> documentIdsInList = from.getDocuments();
		for (int i = 0; i < documentIdsInList.size() && documentList.size() < limit; i++) {
			Long doc = documentIdsInList.get(i);
			// agrega cada id de documento una vez a la lista de resultado
			if (!documentList.contains(doc)) {
				documentList.add(doc);
			}
		}
	}

	/**
	 * Ordena la lista por importancia de término
	 * */
	private List<IndexRecord> sortByTermImportance(List<IndexRecord> list) {
		IndexRecord[] sortedLists =list.toArray(new IndexRecord[list.size()]);
		Comparator<? super IndexRecord> comparator=new Comparator<IndexRecord>(){

			@Override
			public int compare(IndexRecord o1, IndexRecord o2) {
				return o1.compareByTermImportance(o2);
			} 
		};
		Arrays.sort(sortedLists, comparator);
		return Arrays.asList(sortedLists);
	}

	/**
	 * Obtiene la lista de listas invertidas para cada termino de la busqueda
	 * */
	private List<IndexRecord> getIndexRecords(TextDocument query) {
		List<IndexRecord> documentsForEachTerm = new ArrayList<IndexRecord>();
		for (String word : query) {
			IndexRecord indexRecord = this.index.getDocumentsFor(word);
			if (indexRecord != null) {
				documentsForEachTerm.add(indexRecord);
			}
		}
		return documentsForEachTerm;
	}

	public void setResultItemsCount(int top) {
		this.resultItemsCount = top;
	}

	public int getTop() {
		return resultItemsCount;
	}

	public void setMinTermFrecuency(int minTermFrecuency) {
		this.minTermFrecuency = minTermFrecuency;
	}

	public int getMinTermFrecuency() {
		return minTermFrecuency;
	}
}
