package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

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
	public void deserialize(InputStream stream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serialize(OutputStream stream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(Record o) {
		return this.word.compareTo(((AudioIndexRecord)o).word);
	}

}
