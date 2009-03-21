package speakit.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioIndexFile implements RecordFactory {
	private File file;
	
	public AudioIndexFile(File file) {
		this.file = file;
	}
	
	public void addEntry(String word, long offset) throws IOException {
		OutputStream stream = new FileOutputStream(file, true);
		RecordFileWriter writer = new RecordFileWriter(stream);
		AudioIndexRecord record = new AudioIndexRecord(word, offset);
		writer.writeRecord(record);
		stream.close();
	}
	
	public boolean contains(String word) throws IOException {
		InputStream stream = new FileInputStream(file);
		RecordFileReader reader = new RecordFileReader(stream, this);
		while(reader.hasNext()) {
			AudioIndexRecord record = (AudioIndexRecord)reader.readRecord();
			if(record.getWord() == word) {
				return true;
			}
		}
		stream.close();
		return false;
	}
	
	public long getOffset(String word) throws IOException {
		InputStream stream = new FileInputStream(file);
		RecordFileReader reader = new RecordFileReader(stream, this);
		while(reader.hasNext()) {
			AudioIndexRecord record = (AudioIndexRecord)reader.readRecord();
			if(record.getWord() == word) {
				return record.getOffset();
			}
		}
		stream.close();
		throw new RuntimeException("No se encontró la palabra en el índice.");
	}

	@Override
	public Record createRecord() {
		return new AudioIndexRecord();
	}
}
