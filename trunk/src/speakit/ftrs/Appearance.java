package speakit.ftrs;


public class Appearance {
	private int termId;
	private int document;
	
	public Appearance(int termId, int document) {
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
}
