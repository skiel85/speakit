package speakit.ftrs.index;

public class InvertedListItem {
	private long documentId;
	private int localFrecuency;

	public InvertedListItem(int document, int localFrecuency) {
		this.documentId = document;
		this.localFrecuency = localFrecuency;
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
	public int getLocalFrecuency() {
		return localFrecuency;
	}

	/**
	 * @param frecuency
	 *            the frecuency to set
	 */
	public void setLocalFrecuency(int frecuency) {
		this.localFrecuency = frecuency;
	}

}
