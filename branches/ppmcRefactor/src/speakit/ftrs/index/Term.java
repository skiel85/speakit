package speakit.ftrs.index;

public class Term {
	private int termId;
	private String value;

	public Term(int termId, String value) {
		setTermId(termId);
		setValue(value);
	}

	/**
	 * @return the termId
	 */
	public int getTermId() {
		return termId;
	}

	/**
	 * @param termId
	 *            the termId to set
	 */
	public void setTermId(int termId) {
		this.termId = termId;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object term) {
		// La igualdad del termino esta definida solo por la palabra q contiene,
		// no por la posicion
		return value.equals(((Term) term).value);
	}
}
