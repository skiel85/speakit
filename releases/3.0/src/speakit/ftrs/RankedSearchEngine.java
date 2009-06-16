package speakit.ftrs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedIndex;
import speakit.ftrs.index.InvertedIndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.io.record.RecordSerializationException;

//TODO Terminar de implementar
public class RankedSearchEngine {

	private final InvertedIndex index;
	private int resultItemsCount;
	private int minTermFrecuency;

	public RankedSearchEngine(InvertedIndex index, int documentsLimit, int minTermFrecuency) {
		this.index = index;
		this.resultItemsCount = documentsLimit;
		this.minTermFrecuency = minTermFrecuency;
	}

	/**
	 * Ejecuta una busqueda rankeada
	 * @throws IOException 
	 * @throws RecordSerializationException 
	 * */
	public List<Long> search(TextDocument query) throws RecordSerializationException, IOException {
		// Obtiene la lista de listas invertidas del índice para la busqueda
		// actual
		List<InvertedIndexRecord> indexedRecords = getIndexRecords(query);
		// Ordena la lista de listas invertidas segun importancia del término
		List<InvertedIndexRecord> orderedIndexedRecords = sortByTermImportance(indexedRecords);
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
	private List<Long> makeRankedList(List<InvertedIndexRecord> orderedInvertedLists) {
		List<Long> resultDocumentIDs = new ArrayList<Long>();
		Iterator<InvertedIndexRecord> eachIndexedTermIterator = orderedInvertedLists.iterator();

		// recorre todas las listas invertidas, corta cuando no hay mas o bien
		// cuando la lista de documentos encontrados es igual al límite de
		// documentos definido
		while (eachIndexedTermIterator.hasNext() && resultDocumentIDs.size() < this.resultItemsCount) {
			InvertedIndexRecord eachIndexedTerm = eachIndexedTermIterator.next();
			// ordena los elementos de la lista invertida por frecuencia, luego
			// trunca la lista por frecuencia de aparicion del término en cada
			// documento
			InvertedList truncatedList = eachIndexedTerm.getInvertedList().truncateByFrecuency(this.minTermFrecuency);
			appendDocumentsTo(resultDocumentIDs, truncatedList, this.resultItemsCount);
		}
		return resultDocumentIDs;
	}

	/**
	 * Agrega los documentos desde "from" al final de la lista "documentList".
	 * Los items los agrega por unica vez. Si la cantidad de elementos de
	 * "documentList" alcanza limit, inmediatamente deja de agregar elementos.
	 * 
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
	private List<InvertedIndexRecord> sortByTermImportance(List<InvertedIndexRecord> list) {
		InvertedIndexRecord[] sortedLists = list.toArray(new InvertedIndexRecord[list.size()]);
		Comparator<? super InvertedIndexRecord> comparator = new Comparator<InvertedIndexRecord>() {

			@Override
			public int compare(InvertedIndexRecord o1, InvertedIndexRecord o2) {
				return o1.compareByTermImportance(o2);
			}
		};
		Arrays.sort(sortedLists, comparator);
		return Arrays.asList(sortedLists);
	}

	/**
	 * Obtiene la lista de listas invertidas para cada termino de la busqueda
	 * @throws IOException 
	 * @throws RecordSerializationException 
	 * */
	private List<InvertedIndexRecord> getIndexRecords(TextDocument query) throws RecordSerializationException, IOException {
		List<InvertedIndexRecord> documentsForEachTerm = new ArrayList<InvertedIndexRecord>();
		for (String word : query) {
			InvertedIndexRecord indexRecord = this.index.getDocumentsFor(word);
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
