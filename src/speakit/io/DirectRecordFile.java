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

public class DirectRecordFile<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE> {
	private BlockFile blocksFile;
	private RecordFactory<RECTYPE> recordFactory;

	public DirectRecordFile(File file, RecordFactory<RECTYPE> recordFactory) {
		this.blocksFile = new LinkedBlockFile(file);
		this.recordFactory = recordFactory;
	}

	public void create(int blockSize) throws IOException {
		this.blocksFile.create(blockSize);
	}

	public void load() throws IOException {
		this.blocksFile.load();
	}

	@Override
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException {
		RECTYPE record = this.getRecord(key);
		return (record != null);
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		for (Block block : this.blocksFile) {
			RECTYPE record = this.getRecord(key, block);
			if (record != null) {
				return record;
			}
		}
		return null;
	}

	public RECTYPE getRecord(KEYTYPE key, int blockNumber) throws IOException, RecordSerializationException {
		Block block = this.blocksFile.getBlock(blockNumber);
		return this.getRecord(key, block);
	}

	private RECTYPE getRecord(KEYTYPE key, Block block) throws IOException, RecordSerializationException {
		ByteArrayInputStream is = new ByteArrayInputStream(block.getContent());

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

		Block block = this.blocksFile.getNewBlock();
		block.setContent(os.toByteArray());
		this.blocksFile.saveBlock(block);
	}

}
