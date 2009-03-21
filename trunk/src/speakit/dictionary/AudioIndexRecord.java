package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

public class AudioIndexRecord implements Record {

	byte[] audio;
	
	public byte[] getAudio() {
		return this.audio;
	}
	
	public void setAudio(byte[] audio) {
		this.audio = audio;
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
		return 0;
	}

}
