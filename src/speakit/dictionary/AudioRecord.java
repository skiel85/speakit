package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AudioRecord implements Record {

	byte[] audio;
	
	public AudioRecord() {
		// Dejado en blanco intencionalmente.
	}
	
	public AudioRecord(byte[] audio) {
		this.audio = audio;
	}
	
	public byte[] getAudio() {
		return this.audio;
	}
	
	public void setAudio(byte[] audio) {
		this.audio = audio;
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
		throw new NotImplementedException();
	}

}
