package speakit.io.bsharptree;

import speakit.io.record.Record;
import speakit.io.record.RecordFactory;

/**
 * Null pattern
 * 
 * @author Nahuel
 * 
 */
@SuppressWarnings("unchecked")
public class DefaultRecordEncoder extends RecordEncoder {

	private final RecordFactory recordFactory;

	public DefaultRecordEncoder(RecordFactory recordFactory) {
		this.recordFactory = recordFactory;
	}

	@Override
	public void clear() {
	}

	@Override
	public Record createRecord() {
		return recordFactory.createRecord();
	}

	@Override
	public Record decode(Record record) {
		return record;
	}

	@Override
	public Record encode(Record record) {
		return record;
	}

}
