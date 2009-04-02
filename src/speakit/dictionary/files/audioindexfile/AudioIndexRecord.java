package speakit.dictionary.files.audioindexfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.serialization.LongField;
import speakit.dictionary.serialization.StringField;

public class AudioIndexRecord implements Record {

	private LongField offset;
	private StringField word;

	public AudioIndexRecord() {
		this.word = new StringField();
		this.offset = new LongField();
	}

	public AudioIndexRecord(String word, long offset) {
		this.word = new StringField(word);
		this.offset = new LongField(offset);
	}

	@Override
	public int compareTo(Record o) {
		return this.word.compareTo(((AudioIndexRecord) o).word);
	}

	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		long byteCount = 0;
		StringField word = new StringField();
		LongField offset = new LongField();
		try {
			byteCount += word.deserialize(stream);
			byteCount += offset.deserialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
		this.word.setString(word.getString());
		this.offset.setLong(offset.getLong());
		return byteCount;
	}

	public long getOffset() {
		return this.offset.getLong();
	}

	public String getWord() {
		return this.word.getString();
	}

	@Override
	public long serialize(OutputStream stream) throws RecordSerializationException {
		long byteCount = 0;
		try {
			byteCount += this.word.serialize(stream);
			byteCount += this.offset.serialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
		return byteCount;
	}

	public void setOffset(long offset) {
		this.offset.setLong(offset);
	}

	public void setWord(String word) {
		this.word.setString(word);
	}

}
