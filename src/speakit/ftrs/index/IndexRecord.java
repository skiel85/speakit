package speakit.ftrs.index;

public class IndexRecord {
	protected String term;
	protected InversedList documents;

	public IndexRecord(String term, InversedList documents) {
		this.term = term;
		this.documents = documents;
	}
}
