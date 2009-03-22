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
	
	/*
	public RecordFile(InputStream inputStream, OutputStream outputStream, RecordFactory recordFactory) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.recordFactory = recordFactory;
	}
	*/
	
	public RecordFile(File file, RecordFactory recordFactory) throws FileNotFoundException {
		this.inputStream = new FileInputStream(file);
		this.outputStream = new FileOutputStream(file, true);
		this.recordFactory = recordFactory;
	}
	
	public Record readRecord() throws IOException {
		Record record = recordFactory.createRecord();
		long bytesRead;
		try {
			bytesRead = record.deserialize(this.inputStream);
			this.currentReadOffset += bytesRead; 
			return record;
		} catch (RecordSerializationException e) {
			// TODO Resolver que hacer cuando una excepcion es lanzada en RecordFile.readRecord
			e.printStackTrace();
		}
		return null;
	}
	
	public Record readRecord(long offset) throws IOException {
		this.resetReadOffset();
		this.inputStream.skip(offset);
		return this.readRecord();
	}
	
	public void writeRecord(Record record) throws IOException {
		long bytesWritten;
		try {
			bytesWritten = record.serialize(this.outputStream);
			this.currentWriteOffset += bytesWritten;
		} catch (RecordSerializationException e) {
			// TODO Resolver que hacer cuando una excepcion es lanzada en RecordFile.writeRecord
			e.printStackTrace();
		}
	}
	
	public boolean eof() throws IOException {
		return (this.inputStream.available() == 0);
	}
	
	public void resetReadOffset() throws IOException {
		this.currentReadOffset = 0;
		if(this.inputStream.markSupported()) {
			this.inputStream.reset();
		}
		else if(this.inputStream instanceof FileInputStream) {
			((FileInputStream)this.inputStream).getChannel().position(0);
		}
		else {
			throw new RuntimeException("Operación no soportada para " + this.inputStream.getClass().getName());
		}
	}

	public long getCurrentReadOffset() {
		return this.currentReadOffset;
	}
	
	public long getCurrentWriteOffset() {
		return this.currentWriteOffset;
	}
}
