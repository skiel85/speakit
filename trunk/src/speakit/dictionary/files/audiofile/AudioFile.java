package speakit.dictionary.files.audiofile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import speakit.audio.Audio;
import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordFactory;
import speakit.dictionary.files.RecordFile;

public class AudioFile implements RecordFactory {
	private RecordFile recordFile;
	private long currentOffset;

	public AudioFile(File file) throws FileNotFoundException {
		this.recordFile = new RecordFile(file, this);
		this.currentOffset = file.length();
	}

	public Audio getAudio(long offset) throws IOException {
		AudioRecord record = (AudioRecord) this.recordFile.readRecord(offset);
		return new Audio(record.getAudio(),record.getDuration());
	}

	public long addAudio(Audio audio) throws IOException {
		long oldOffset = this.currentOffset;
		AudioRecord record = new AudioRecord(audio.getBytes(),audio.getDuration());
		this.recordFile.writeRecord(record);
		this.currentOffset = this.recordFile.getCurrentWriteOffset();
		return oldOffset;
	}

	@Override
	public Record createRecord() {
		return new AudioRecord();
	}
}
