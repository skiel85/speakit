package speakit.bsharptree.test;

import speakit.io.record.IndexRecord;
import speakit.io.record.StringField;

public class TestIndexRecord extends IndexRecord<StringField> {
	private StringField key = new StringField();

	@Override
	protected StringField getKey() {
		return this.key;
	}

	public TestIndexRecord(String key, int block) {
		this.key.setString(key);
		this.setBlockNumber(block);
	}
}
