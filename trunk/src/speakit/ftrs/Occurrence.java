package speakit.ftrs;

public class Occurrence implements Comparable<Occurrence> {
	private int termId;
	private int document;

	public Occurrence(int termId, int document) {
		this.termId = termId;
		this.document = document;
	}

	/**
	 * @return the termId
	 */
	public int getTermId() {
		return termId;
	}

	/**
	 * @return the document
	 */
	public int getDocument() {
		return document;
	}

	@Override
	public int compareTo(Occurrence o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
