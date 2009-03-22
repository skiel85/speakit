package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AudioIndexRecord implements Record {

	public AudioIndexRecord() {
		// Dejado intencionalmente en blanco.
	}
	
	public AudioIndexRecord(String word, long offset) {
		this.word = word;
		this.offset = offset;
	}
	
	String word;
	
	public String getWord() {
		return this.word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	long offset;
	
	public long getOffset() {
		return this.offset;
	}
	
	public void setOffset(long offset) {
		this.offset = offset;
	}
	
	@Override
	public long deserialize(InputStream stream) {
		throw new NotImplementedException();
	}

	@Override
	public long serialize(OutputStream stream) {
		throw new NotImplementedException();
	}

	@Override
	public int compareTo(Record o) {
		return this.word.compareTo(((AudioIndexRecord)o).word);
	}

}
