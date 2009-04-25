package speakit.ftrs.index;

public class InvertedListItem {
	private int document;
	private int frecuency;

	public InvertedListItem(int document, int frecuency) {
		this.document = document;
		this.frecuency = frecuency;
	}

	/**
	 * @return the document
	 */
	public int getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(int document) {
		this.document = document;
	}

	/**
	 * @return the frecuency
	 */
	public int getFrecuency() {
		return frecuency;
	}

	/**
	 * @param frecuency
	 *            the frecuency to set
	 */
	public void setFrecuency(int frecuency) {
		this.frecuency = frecuency;
	}

}
