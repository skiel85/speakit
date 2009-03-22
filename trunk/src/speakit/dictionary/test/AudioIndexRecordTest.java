package speakit.dictionary.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import speakit.dictionary.AudioIndexRecord;

public class AudioIndexRecordTest {
	
	@Test
	public void testSerializeAndDeserialize() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		AudioIndexRecord record = new AudioIndexRecord("hola", 5214);
		record.serialize(outputStream);
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		AudioIndexRecord deserialized = new AudioIndexRecord();
		deserialized.deserialize(inputStream);
		
		Assert.assertEquals(record.getWord(), deserialized.getWord());
		Assert.assertEquals(record.getOffset(), deserialized.getOffset());
	}

}
