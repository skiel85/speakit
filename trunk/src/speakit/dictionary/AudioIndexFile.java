package speakit.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
		while(!this.recordFile.eof()) {
			AudioIndexRecord record = (AudioIndexRecord) this.recordFile.readRecord();
			if(record.getWord() == word) {
				return true;
			}
		}
		return false;
	}
	
	public long getOffset(String word) throws IOException {
		while(!this.recordFile.eof()) {
			AudioIndexRecord record = (AudioIndexRecord) this.recordFile.readRecord();
			if(record.getWord().compareTo(word)==0) {
				return record.getOffset();
			}
		}
		throw new RuntimeException("No se encontr� la palabra en el �ndice.");
	}

	@Override
	public Record createRecord() {
		return new AudioIndexRecord();
	}
}
