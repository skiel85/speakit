package speakit.io.record.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.io.record.RecordSerializationException;
import speakit.io.record.ShortField;
import speakit.io.recordfile.test.FieldTest;

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
		 FieldTest.testFieldSerialization(field1, new ShortField());
	}
	
	
}
