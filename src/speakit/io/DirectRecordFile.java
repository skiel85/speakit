package speakit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordFactory;
import speakit.dictionary.files.RecordFile;
import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.serialization.Field;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DirectRecordFile<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE> {
	private BasicBlocksFile basicBlocksFile;
	private RecordFactory<RECTYPE> recordFactory;

	public DirectRecordFile(File file) {
		this.basicBlocksFile = new BasicBlocksFileImpl(file);
	}

	public void create(int blockSize) throws IOException {
		this.basicBlocksFile.create(blockSize);
	}

	public void load() throws IOException {
		this.basicBlocksFile.load();
	}

	@Override
	public boolean contains(KEYTYPE key) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException {
		throw new NotImplementedException();
	}

	public RECTYPE getRecord(KEYTYPE key, int blockNumber) throws IOException, RecordSerializationException {
		ByteArrayInputStream is = new ByteArrayInputStream(this.basicBlocksFile.read(blockNumber));

		while (is.available() > 0) {
			RECTYPE record = this.recordFactory.createRecord();
			record.deserialize(is);
			if (record.compareToKey(key) == 0) {
				return record;
			}
		}

		return null;
	}

	@Override
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		record.serialize(os);

		int blockNumber = this.basicBlocksFile.appendBlock();
		this.basicBlocksFile.write(blockNumber, os.toByteArray());
	}

}
