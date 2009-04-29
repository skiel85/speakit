package speakit.io.record;

public class OffsetRecord extends Record<LongField> {

	private LongField offset = new LongField();

	public OffsetRecord() {
		this.setKey(this.offset);
	}

	/**
	 * Obtiene la posici�n en el archivo.
	 * 
	 * @return
	 */
	public long getOffset() {
		return this.offset.getLong();
	}

	/**
	 * Establece la posici�n en el archivo.
	 * 
	 * @param audio
	 */
	public void setOffset(long offset) {
		this.offset.setLong(offset);
	}

}