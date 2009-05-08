package speakit.io.bsharptree;

import speakit.io.record.Record;
import speakit.io.record.RecordFactory;

public abstract class RecordEncoder implements RecordFactory {

	public abstract void clear();

	/**
	 * Crea un registro para codificado
	 */
	@Override
	public abstract Record createRecord();

	public abstract Record decode(Record record);

	public abstract Record encode(Record record);

}
