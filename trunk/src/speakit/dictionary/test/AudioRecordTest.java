package speakit.dictionary.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.files.audiofile.AudioRecord;

public class AudioRecordTest {

	@Test
	public void testSerializeAndDeserialize() throws IOException {
		byte[] audio = new byte[] { 10, -25, 32, 64, -122, 89, 55, 0, -3, 102 };

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		AudioRecord record = new AudioRecord(audio,654654);
		try {
			record.serialize(outputStream);
		} catch (RecordSerializationException e) {
			Assert.fail();
		}

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		AudioRecord deserialized = new AudioRecord();
		try {
			deserialized.deserialize(inputStream);
		} catch (RecordSerializationException e) {
			Assert.fail();
		}

		Assert.assertArrayEquals(record.getAudio(), deserialized.getAudio());
		Assert.assertEquals(record.getDuration(), deserialized.getDuration());
	}

}
