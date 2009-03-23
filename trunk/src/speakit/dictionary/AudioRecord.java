package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioRecord implements Record {
 
	SerializableByteArray audio;
	
	public AudioRecord() { 
		this.audio = new SerializableByteArray();		
	}
	
	public AudioRecord( byte[] audio) { 
		this.audio = new SerializableByteArray(audio);
	}
	
	public byte[] getAudio() {
		return this.audio.getBytes();
	}
	
	public void setAudio(byte[] audio) {
		this.audio.setBytes(audio);
	}
		
	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		try {
			return this.audio.deserialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
	}

	@Override
	public long serialize(OutputStream stream) throws RecordSerializationException {
		try {
			return this.audio.serialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
	}

	@Override
	public int compareTo(Record o) { 
		if(o instanceof AudioRecord){
			AudioRecord other=(AudioRecord)o;
			return this.audio.compareTo(other.audio);	
		}else{
			return this.getClass().toString().compareTo(o.getClass().toString());
		}		 
	}

	private int min(int length, int length2) {
		return (length<length2)?length:length2;
	} 
}
