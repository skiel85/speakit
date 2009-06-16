package speakit.io.recordfile.test;

import java.io.IOException;

import org.junit.Assert;

import speakit.io.record.Field;
import speakit.io.record.RecordSerializationException;

public class FieldTest {
	public static void testFieldSerialization(Field field1, Field newField) throws RecordSerializationException, IOException {
		byte[] field1Serialization = field1.serialize();
		newField.deserialize(field1Serialization);
		byte[] field2serialization = newField.serialize();

		Assert.assertArrayEquals(field1Serialization, field2serialization);
		Assert.assertEquals(field1.toString(), newField.toString());
	}
}
