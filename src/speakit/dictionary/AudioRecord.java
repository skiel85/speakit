package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AudioRecord implements Record {

	String word;
	
	public String getWord() {
		return this.word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	int offset;
	
	public int getOffset() {
		return this.offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
		
	@Override
	public void deserialize(InputStream stream) {
		throw new NotImplementedException();
	}

	@Override
	public void serialize(OutputStream stream) {
		throw new NotImplementedException();
	}

	@Override
	public int compareTo(Record o) {
		return this.word.compareTo(((AudioRecord)o).word);
	}

}
