package speakit.ftrs.index;

public class IndexRecord {
	protected String term;
	protected int documentsQty;
	protected int maxLocalFrecuency;
	protected InvertedList documents;

	public IndexRecord(String term,int documentsQty, int maxLocalFrecuency, InvertedList documents) {
		this.term = term;
		this.documentsQty = documentsQty;
		this.maxLocalFrecuency = maxLocalFrecuency;
		this.documents = documents;
	}
	
	public IndexRecord(String term, InvertedList documents) {
		this.term = term; 
		this.documents = documents;
	}

	public InvertedList getInvertedList() {
		return documents;
	}
}
