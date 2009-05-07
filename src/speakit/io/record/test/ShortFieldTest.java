package speakit.io.record.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.record.Field;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.ShortField;

public class ShortFieldTest {
	@Before
	public void setUp() throws Exception { 
	}

	@After
	public void tearDown() throws Exception {
	}
 
	
	@Test
	public void testSerialize() throws RecordSerializationException, IOException {
		 ShortField field1=new ShortField((short) 12);
		 testFieldSerialization(field1, new ShortField());
	}
	
	private static void testFieldSerialization(Field field1,Field newField) throws RecordSerializationException, IOException{
		 byte[] field1Serialization = field1.serialize(); 	 
		 newField.deserialize(field1Serialization);
		 byte[] field2serialization = newField.serialize();
		 
		 Assert.assertArrayEquals(field1Serialization, field2serialization);
	}
}
