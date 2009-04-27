package speakit.io.recordfile;

import java.io.File;
import java.io.IOException;

import speakit.io.record.LongField;
import speakit.io.record.OffsetRecord;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

public class OffsetRecordFile<RECTYPE extends OffsetRecord> extends SecuentialRecordFile<RECTYPE, LongField> {

	/**
	 * Crea un archivo de registros.
	 * 
	 * @param file
	 * @param recordFactory
	 * @throws IOException
	 */
	public OffsetRecordFile(File file, RecordFactory<RECTYPE> recordFactory) throws IOException {
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
		RECTYPE record = super.readRecord(offset.getLong());
		record.setOffset(offset.getLong());
		return record;
	}

	/**
	 * Agrega un registro al archivo de registros.
	 * 
	 * @param audio
	 *            Audio que se agregará.
	 * @return Offset donde se agregó el audio.
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
