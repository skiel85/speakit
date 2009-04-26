package speakit.io.recordfile.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.audioindexfile.AudioIndexRecord;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.io.recordfile.DirectRecordFile;

public class DirectRecordFileTest {

	private DirectRecordFile<AudioIndexRecord, StringField> sut;
	private File file;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new DirectRecordFile<AudioIndexRecord, StringField>(this.file, new RecordFactory<AudioIndexRecord>() {
			@Override
			public AudioIndexRecord createRecord() {
				return new AudioIndexRecord();
			}
		});
		this.sut.create(512);
	}

	@Test
	public void testAddAndRetrieveRecord() throws IOException, RecordSerializationException {
		AudioIndexRecord record = new AudioIndexRecord("hola", 1234);
		StringField key = new StringField("hola");
		this.sut.insertRecord(record);
		AudioIndexRecord retrievedRecord = this.sut.getRecord(key);
		Assert.assertNotNull(retrievedRecord);
		Assert.assertEquals(record.getWord(), retrievedRecord.getWord());
		Assert.assertEquals(record.getOffset(), retrievedRecord.getOffset());
	}

	@Test
	public void testAddAndRetrieveRecordFromBlock() throws IOException, RecordSerializationException {
		int blockNumber = this.sut.createBlock();
		AudioIndexRecord record = new AudioIndexRecord("hola", 1234);
		StringField key = new StringField("hola");
		this.sut.insertRecord(record, blockNumber);
		AudioIndexRecord retrievedRecord = this.sut.getRecord(key, blockNumber);
		Assert.assertNotNull(retrievedRecord);
		Assert.assertEquals(record.getWord(), retrievedRecord.getWord());
		Assert.assertEquals(record.getOffset(), retrievedRecord.getOffset());
	}

	@Test
	public void testAddVariousAndRetrieveRecordFromBlock() throws IOException, RecordSerializationException {
		int blockNumber = this.sut.createBlock();
		AudioIndexRecord record1 = new AudioIndexRecord("www", 1234);
		AudioIndexRecord record2 = new AudioIndexRecord("xxx", 5678);
		AudioIndexRecord record3 = new AudioIndexRecord("yyy", 9012);
		AudioIndexRecord record4 = new AudioIndexRecord("zzz", 3456);
		this.sut.insertRecord(record1, blockNumber);
		this.sut.insertRecord(record2, blockNumber);
		this.sut.insertRecord(record3, blockNumber);
		this.sut.insertRecord(record4, blockNumber);
		StringField key = new StringField("yyy");
		AudioIndexRecord retrievedRecord = this.sut.getRecord(key, blockNumber);
		Assert.assertNotNull(retrievedRecord);
		Assert.assertEquals(record3.getWord(), retrievedRecord.getWord());
		Assert.assertEquals(record3.getOffset(), retrievedRecord.getOffset());
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

}
