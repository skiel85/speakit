package speakit.ftrs.index;

import speakit.io.record.IndexRecord;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.StringField;

public class InvertedIndexIndexRecord extends IndexRecord<StringField> {
	private StringField	key	= new StringField();

	@Override
	public StringField getKey() {
		return this.key;
	}

	public InvertedIndexIndexRecord(String key, int block) {
		this.key.setString(key);
		this.setBlockNumber(block);
	}

	public InvertedIndexIndexRecord() {
		this("", 0);
	}

	@Override
	protected String getStringRepresentation() {
		return  this.key.toString() + "," + this.getBlockNumber();
	}

	public static RecordFactory createRecordFactory() {
		return new RecordFactory(){
			@Override
			public Record createRecord() {
				return new InvertedIndexIndexRecord( "",-1);
			}
		};
	}}
