package speakit.io.recordfile;

import java.io.File;
import java.io.IOException;

import speakit.io.record.LongField;
import speakit.io.record.RawRecord;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

public class SecuentialRawRecordFile<RECTYPE extends RawRecord> extends SecuentialRecordFile<RECTYPE, LongField> {

	/**
	 * Crea un archivo de registros.
	 * 
	 * @param file
	 * @param recordFactory
	 * @throws IOException
	 */
	public SecuentialRawRecordFile(File file, RecordFactory<RECTYPE> recordFactory) throws IOException {
		super(file, recordFactory);
	}

	/**
	 * Obtiene un registro a partir de un offset.
	 * 
	 * @param offset
	 *            Offset donde comienza el audio.
	 * @return Audio encontrado.
	 * @throws IOException
	 */
	public RECTYPE getRecord(long offset) throws IOException {
		LongField field = new LongField(offset);
		return (this.getRecord(field));
	}
	
	/**
	 * Obtiene un registro a partir de un offset.
	 * 
	 * @param offset
	 *            Offset donde comienza el audio.
	 * @return Audio encontrado.
	 * @throws IOException
	 */
	@Override
	public RECTYPE getRecord(LongField offset) throws IOException {
		RECTYPE record = super.getRecord(offset);
		record.setOffset(offset.getLong());
		return record;
	}

	/**
	 * Agrega un registro al archivo de registros.
	 * 
	 * @param audio
	 *            Audio que se agregar�.
	 * @return Offset donde se agreg� el audio.
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	@Override
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		long offset = this.getCurrentWriteOffset();
		super.insertRecord(record);
		record.setOffset(offset);
	}

}
