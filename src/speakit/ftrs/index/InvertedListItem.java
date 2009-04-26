package speakit.ftrs.index;

public class InvertedListItem {
	private long documentId;
	private int frecuency;

	public InvertedListItem(int document, int frecuency) {
		this.documentId = document;
		this.frecuency = frecuency;
	}

	/**
	 * @return the document
	 */
	public long getDocumentId() {
		return documentId;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
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
