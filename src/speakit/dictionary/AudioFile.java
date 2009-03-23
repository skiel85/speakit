package speakit.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AudioFile implements RecordFactory {
	private RecordFile recordFile;
	private long currentOffset;
	
	public AudioFile(File file) throws FileNotFoundException {
		this.recordFile = new RecordFile(file, this);
	}
	
	public byte[] getAudio(long offset) throws IOException {
		AudioRecord record = (AudioRecord) this.recordFile.readRecord(offset);
		return record.getAudio();
	}
	
	public long addAudio(byte[] audio) throws IOException {
		long oldOffset = this.currentOffset; 
		AudioRecord record = new AudioRecord(audio);
		this.recordFile.writeRecord(record);
		this.currentOffset = this.recordFile.getCurrentWriteOffset();
		return oldOffset;
	}	

	@Override
	public Record createRecord() {
		return new AudioRecord();
	}
}
