package speakit.io.recordfile;

import java.io.IOException;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

public interface RecordFile<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> {
	
	/**
	 * Inserta el registro dentro del archivo.
	 * @param record
	 * @return La posición del archivo en donde quedó guardado el registro, se define con fines de indexación por parte del cliente. 
	 * El valor devuelto puede ser un número de bloque o un offset, queda a criterio del implementador.
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public long insertRecord(RECTYPE record) throws IOException, RecordSerializationException;

	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException;

	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException;
}
