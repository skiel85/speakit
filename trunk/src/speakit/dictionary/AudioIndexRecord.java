package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioIndexRecord implements Record {

	long offset;
	
	String word;
	
	public AudioIndexRecord() {
		// Dejado intencionalmente en blanco.
	}
	
	public AudioIndexRecord(String word, long offset) {
		this.word = word;
		this.offset = offset;
	}
	
	@Override
	public int compareTo(Record o) {
		return this.word.compareTo(((AudioIndexRecord)o).word);
	}
	
	@Override
	public long deserialize(InputStream stream) throws IOException {
		long byteCount = 0;
		SerializableString word = new SerializableString();
		SerializableLong offset = new SerializableLong();
		byteCount += word.deserialize(stream);
		byteCount += offset.deserialize(stream);
		this.word = word.value;
		this.offset = offset.value;
		return byteCount;
	}
	
	public long getOffset() {
		return this.offset;
	}
	
	public String getWord() {
		return this.word;
	}
	
	@Override
	public long serialize(OutputStream stream) throws IOException {
		long byteCount = 0;
		byteCount += new SerializableString(this.word).serialize(stream);
		byteCount += new SerializableLong(this.offset).serialize(stream);
		return byteCount;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
