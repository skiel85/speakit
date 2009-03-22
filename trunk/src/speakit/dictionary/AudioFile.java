package speakit.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioFile implements RecordFactory {
	private File file;
	
	public AudioFile(File file) {
		this.file = file;
	}
	
	public byte[] getAudio(long offset) throws IOException {
		InputStream stream = new FileInputStream(file);
		stream.skip(offset);
		AudioRecord record = new AudioRecord();
		record.deserialize(stream);
		stream.close();
		return record.getAudio();
	}
	
	public long addAudio(byte[] audio) throws IOException {
		long offset = file.length();
		OutputStream stream = new FileOutputStream(file, true);
		AudioRecord record = new AudioRecord(audio);
		record.serialize(stream);
		stream.close();
		return offset;
	}	

	@Override
	public Record createRecord() {
		return new AudioRecord();
	}
}
