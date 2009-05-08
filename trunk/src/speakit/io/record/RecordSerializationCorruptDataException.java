package speakit.io.record;

public class RecordSerializationCorruptDataException extends RecordSerializationException {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
 
	public RecordSerializationCorruptDataException(String source, byte savedHash, byte calculatedHash) {
		super("Datos corruptos (hash invalido). Origen: "+source+". \nHash guardado:"+savedHash+"\nHash calculado:"+calculatedHash);
	}
}
