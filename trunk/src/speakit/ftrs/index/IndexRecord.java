package speakit.ftrs.index;

public class IndexRecord {
	protected String term;
	protected InvertedList documents;

	public IndexRecord(String term, InvertedList documents) {
		this.term = term;
		this.documents = documents;
	}
}
