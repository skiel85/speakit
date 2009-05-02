package speakit.ftrs.index;

public class InvertedIndexRecord {
	private String term;
	private int documentsQty;
	private int maxLocalFrecuency;
	private InvertedList documents;

	public InvertedIndexRecord(String term, int documentsQty, int maxLocalFrecuency, InvertedList documents) {
		this.term = term;
		this.setDocumentsQty(documentsQty);
		this.setMaxLocalFrecuency(maxLocalFrecuency);
		this.documents = documents;
	}

	public InvertedIndexRecord(String term, InvertedList documents) {
		this.term = term;
		this.documents = documents;
		calculateTermsStatistics();
	}

	public InvertedList getInvertedList() {
		return documents.clone();
	}

	public void setInvertedList(InvertedList list) {
		this.documents = list;
		this.calculateTermsStatistics();
	}

	public void calculateTermsStatistics() {
		if (this.documents == null) {
			this.documents = new InvertedList();
		}
		this.setDocumentsQty(this.documents.size());
		this.setMaxLocalFrecuency(this.documents.getMaxLocalFrecuency());
	}

	public double getTotalWeight(int totalDocuments) {
		return Math.log10(totalDocuments / this.getDocumentsQty());
	}

	// TODO: REVISAR PESO GLOBAL
	/**
	 * El documento mas importante es el mas raro, es decir el que aparezca en
	 * menos documentos. Por lo tanto el mas raro de los dos devuelve -1.
	 * 
	 * @param other
	 * @return
	 */
	public int compareByTermImportance(InvertedIndexRecord other) {
		if (this.getDocumentsQty() == other.getDocumentsQty()) {
			return 0;
		} else {
			if (this.getDocumentsQty() < other.getDocumentsQty()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	public void setTerm(String term) {
		this.term = term;
	}

	public String getTerm() {
		return term;
	}

	public void setMaxLocalFrecuency(int maxLocalFrecuency) {
		this.maxLocalFrecuency = maxLocalFrecuency;
	}

	public int getMaxLocalFrecuency() {
		return maxLocalFrecuency;
	}

	public void setDocumentsQty(int documentsQty) {
		this.documentsQty = documentsQty;
	}

	public int getDocumentsQty() {
		return documentsQty;
	}
}
