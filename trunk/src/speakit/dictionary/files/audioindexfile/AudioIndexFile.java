package speakit.dictionary.files.audioindexfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordFactory;
import speakit.dictionary.files.RecordFile;

public class AudioIndexFile implements RecordFactory {
	private RecordFile recordFile;

	public AudioIndexFile(File file) throws FileNotFoundException {
		this.recordFile = new RecordFile(file, this);
	}

	public void addEntry(String word, long offset) throws IOException {
		AudioIndexRecord record = new AudioIndexRecord(word, offset);
		this.recordFile.writeRecord(record);
	}

	public boolean contains(String word) throws IOException {
		this.recordFile.resetReadOffset();
		while (!this.recordFile.eof()) {
			AudioIndexRecord record = (AudioIndexRecord) this.recordFile.readRecord();
			if (record.getWord().compareTo(word) == 0) {
				return true;
			}
		}
		return false;
	}

	public long getOffset(String word) throws IOException {
		recordFile.resetReadOffset();// TODO: hacer una prueba de esto
		while (!this.recordFile.eof()) {
			AudioIndexRecord record = (AudioIndexRecord) this.recordFile.readRecord();
			if (record.getWord().compareTo(word) == 0) {
				return record.getOffset();
			}
		}
		throw new RuntimeException("No se encontró la palabra en el índice.");
	}

	@Override
	public Record createRecord() {
		return new AudioIndexRecord();
	}
}
