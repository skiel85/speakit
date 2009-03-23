package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioIndexRecord implements Record {

	SerializableLong offset;	
	SerializableString word;
	
	public AudioIndexRecord() {
		this.word = new SerializableString();
		this.offset = new SerializableLong();
	}
	
	public AudioIndexRecord(String word, long offset) {
		this.word = new SerializableString(word);
		this.offset = new SerializableLong(offset);
	}
	
	@Override
	public int compareTo(Record o) {
		return this.word.compareTo(((AudioIndexRecord)o).word);
	}
	
	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		long byteCount = 0;
		SerializableString word = new SerializableString();
		SerializableLong offset = new SerializableLong();
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
