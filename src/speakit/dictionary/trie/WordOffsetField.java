package speakit.dictionary.trie;

import speakit.io.record.*;

public class WordOffsetField extends CompositeField {

	private StringField		word		= new StringField();
	private LongField		nextRecord	= new LongField();
	private BooleanField	isLast		= new BooleanField();

	public WordOffsetField() {
		this.addField(word);
		this.addField(nextRecord);
		this.addField(isLast);
	}
	
	public WordOffsetField(long nextRecord, String word, boolean isLast) {
		this.nextRecord.setLong(nextRecord);
		this.word.setString(word);
		this.isLast.setBoolean(isLast);
	}

	public String getWord() {
		return word.getString();
	}

	public void setWord(String word) {
		this.word.setString(word);
	}

	public long getNextRecord() {
		return nextRecord.getLong();
	}

	public void setNextRecord(long nextRecord) {
		this.nextRecord.setLong(nextRecord);
	}

	public boolean isLast() {
		return isLast.getBoolean();
	}

	public void setLast(boolean isLast) {
		this.isLast.setBoolean(isLast);
	}
}
