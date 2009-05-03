package speakit.io.record;

public abstract class OffsetRecord extends Record<LongField> {

	private LongField offset = new LongField();

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

	@Override
	public LongField getKey() {
		return this.offset;
	}

}
