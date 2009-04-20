package speakit.ftrs.index;

import java.util.ArrayList;

public class Indexer {
	private ArrayList<IndexRecord> getRecords() {
		//con la lista de apariciones y la lista de docs
		ArrayList<IndexRecord> records = new ArrayList<IndexRecord>();
		records.add(new IndexRecord("termino1", new InversedList()));
		records.add(new IndexRecord("termino2", new InversedList()));
		records.add(new IndexRecord("termino3", new InversedList()));
		return records;
	}

	private void addTermsToAppearanceList(ArrayList<String> terms) {
		//la lista de apariciones guarda en memoria termino, orden de aparicion
	}

	public void indexTerms(Index index, ArrayList<String> terms) {
		addTermsToAppearanceList(terms);
		updateDocumentList();
		ArrayList<IndexRecord> records = getRecords();
		index.updateAppearances(records);
	}

	private void updateDocumentList() {
		//debo actualizar en disco la lista de docs, el archivo q guarda los pares id_term, nro_documento
	}

}
