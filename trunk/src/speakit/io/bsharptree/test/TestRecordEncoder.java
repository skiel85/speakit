package speakit.io.bsharptree.test;

import speakit.io.bsharptree.RecordEncoder;
import speakit.io.record.Record;

public class TestRecordEncoder extends RecordEncoder {

	@Override
	public Record createRecord() {
		return new TestIndexRecord();
	}

	@Override
	public Record decode(Record record) {
		return new TestIndexRecord();
	}

	@Override
	public Record encode(Record record) {
		return new TestIndexRecord();
	}

	@Override
	public void clear() {
		
	}
	
	

}
