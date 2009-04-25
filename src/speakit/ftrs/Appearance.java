package speakit.ftrs;


public class Appearance implements Comparable<Appearance>{
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

	@Override
	public int compareTo(Appearance o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
