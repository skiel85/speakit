package speakit.io.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.audioindexfile.AudioIndexRecord;
import speakit.dictionary.serialization.StringField;
import speakit.io.DirectRecordFile;
import speakit.io.RecordFactory;
import speakit.io.RecordSerializationException;

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
		Assert.assertEquals(retrievedRecord.getWord(), record.getWord());
		Assert.assertEquals(retrievedRecord.getOffset(), record.getOffset());
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

}
