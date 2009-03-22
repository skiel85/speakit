package speakit.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RecordFile {
	private InputStream inputStream;
	private OutputStream outputStream;
	private RecordFactory recordFactory;
	private long currentWriteOffset;
	private long currentReadOffset;
	
	public RecordFile(InputStream inputStream, OutputStream outputStream, RecordFactory recordFactory) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.recordFactory = recordFactory;
	}
	
	public RecordFile(File file, RecordFactory recordFactory) throws FileNotFoundException {
		this.inputStream = new FileInputStream(file);
		this.outputStream = new FileOutputStream(file, true);
		this.recordFactory = recordFactory;
	}
	
	public Record readRecord() throws IOException {
		Record record = recordFactory.createRecord();
		long bytesRead = record.deserialize(this.inputStream);
		this.currentReadOffset += bytesRead; 
		return record;
	}
	
	public Record readRecord(long offset) throws IOException {
		this.inputStream.reset();
		this.inputStream.skip(offset);
		return this.readRecord();
	}
	
	public void writeRecord(Record record) throws IOException {
		long bytesWritten = record.serialize(this.outputStream);
		this.currentWriteOffset += bytesWritten;
	}
	
	public boolean eof() throws IOException {
		return (this.inputStream.available() == 0);
	}
	
	public void resetReadOffset() throws IOException {
		this.currentReadOffset = 0;
		this.inputStream.reset();
	}

	public long getCurrentReadOffset() {
		return this.currentReadOffset;
	}
	
	public long getCurrentWriteOffset() {
		return this.currentWriteOffset;
	}
}
