package speakit.dictionary.files.audiofile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.serialization.ByteArrayField;
import speakit.dictionary.serialization.IntegerField;

public class AudioRecord implements Record {

	private IntegerField duration;
	private ByteArrayField audio;

	public AudioRecord() {
		this.audio = new ByteArrayField();
		this.duration=new IntegerField(0);
	}

	public AudioRecord(byte[] audio,int duration) {
		this.audio = new ByteArrayField(audio);
		this.duration=new IntegerField(duration);
	}

	public byte[] getAudio() {
		return this.audio.getBytes();
	}

	public void setAudio(byte[] audio) {
		this.audio.setBytes(audio);
	}
	
	public void setDuration(int duration) {
		this.duration.setInteger(duration);
	}

	public int getDuration() {
		return this.duration.getInteger();
	}

	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		try {
			return this.duration.deserialize(stream) + this.audio.deserialize(stream); 
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
	}

	@Override
	public long serialize(OutputStream stream) throws RecordSerializationException {
		try {
			return this.duration.serialize(stream)+ this.audio.serialize(stream);
		} catch (IOException e) {
			throw new RecordSerializationException();
		}
	}

	@Override
	public int compareTo(Record o) {
		if (o instanceof AudioRecord) {
			AudioRecord other = (AudioRecord) o;
			return this.audio.compareTo(other.audio);
		} else {
			return this.getClass().toString().compareTo(o.getClass().toString());
		}
	}

}
