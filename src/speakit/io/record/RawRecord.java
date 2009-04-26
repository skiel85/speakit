package speakit.io.record;

public class RawRecord extends Record<LongField> {

	private LongField offset = new LongField();

	public RawRecord() {
		this.setKey(this.offset);
	}

	/**
	 * Obtiene la posición en el archivo.
	 * 
	 * @return
	 */
	public long getOffset() {
		return this.offset.getLong();
	}

	/**
	 * Establece la posición en el archivo.
	 * 
	 * @param audio
	 */
	public void setOffset(long offset) {
		this.offset.setLong(offset);
	}

}
