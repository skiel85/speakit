package speakit.dictionary.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import speakit.dictionary.AudioIndexRecord;
import speakit.dictionary.RecordSerializationException;

public class AudioIndexRecordTest {
	
	@Test
	public void testSerializeAndDeserialize() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		AudioIndexRecord record = new AudioIndexRecord("hola", 5214);
		try {
			record.serialize(outputStream);
		} catch (RecordSerializationException e) {
			Assert.fail();
		}
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		AudioIndexRecord deserialized = new AudioIndexRecord();
		try {
			deserialized.deserialize(inputStream);
		} catch (RecordSerializationException e) {
			Assert.fail();
		}
		
		Assert.assertEquals(record.getWord(), deserialized.getWord());
		Assert.assertEquals(record.getOffset(), deserialized.getOffset());
	}

}
