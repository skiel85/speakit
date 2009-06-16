package speakit.compression.lzp;

import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.StringField;

public class LzpTableRecord extends Record<StringField> {

	private IntegerField matchPosition = new IntegerField();
	private StringField context = new StringField();
	
	public LzpTableRecord() {
		
	}
	
	public LzpTableRecord(String context, Integer matchPos) {
		this();
		this.context.setString(context);
		this.matchPosition.setInteger(matchPos.intValue());
	}
	/**
	 * @return the matchPosition
	 */
	public IntegerField getMatchPosition() {
		return matchPosition;
	}

	/**
	 * @param matchPosition the matchPosition to set
	 */
	public void setMatchPosition(IntegerField matchPosition) {
		this.matchPosition = matchPosition;
	}

	/**
	 * @return the context
	 */
	public StringField getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(StringField context) {
		this.context = context;
	}

	@Override
	protected Field[] getFields() {
		return new Field[] {this.context, this.matchPosition};
	}

	@Override
	public StringField getKey() {
		return this.context;
	}
	
	public static RecordFactory createRecordFactory() {
		return new RecordFactory(){
			@Override
			public Record createRecord() {
				return new LzpTableRecord();
			}
		};
	}

}
