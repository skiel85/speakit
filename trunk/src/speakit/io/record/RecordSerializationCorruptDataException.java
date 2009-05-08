package speakit.io.record;

public class RecordSerializationCorruptDataException extends RecordSerializationException {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public RecordSerializationCorruptDataException(byte savedHash,byte calculatedHash) {
		super("Datos corruptos (hash invalido).\nHash guardado:"+savedHash+"\nHash calculado:"+calculatedHash);
	}
}
