package speakit.io.record.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.FrontCodedWord;
import speakit.io.record.FrontCodedStringField;
import speakit.io.record.RecordSerializationException;

public class FrontCodedStringFieldTest {
	
	 
	@Before
	public void setUp() throws Exception { 
	}

	@After
	public void tearDown() throws Exception {
	}

	
//	
//	@Test
//	public void test1() {
//		 FrontCodedStringField string1=new FrontCodedStringField();
//		 string1.load(new FrontCodedWord((short) 12,"esto es una palabra"));
//		 string1.compareTo(string1)
//	}
//	
	
	@Test
	public void testSerialize() throws RecordSerializationException, IOException {
		 FrontCodedStringField string1=new FrontCodedStringField();
		 string1.load(new FrontCodedWord((short) 12,"esto es una palabra"));
		 byte[] string1Serialization = string1.serialize();
		 
		 FrontCodedStringField string2 = new FrontCodedStringField();		 
		 string2.deserialize(string1Serialization);
		 byte[] string2serialization = string2.serialize();
		 
		 Assert.assertEquals(string1.asFrontCodedWord().getEndingCharacters(), string2.asFrontCodedWord().getEndingCharacters());
		 Assert.assertEquals(string1.asFrontCodedWord().getMatchingCharacters(), string2.asFrontCodedWord().getMatchingCharacters());
		 
		 Assert.assertArrayEquals(string1Serialization, string2serialization);
	}
}
