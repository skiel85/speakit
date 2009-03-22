package speakit.dictionary.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.naming.BinaryRefAddr;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.SerializableInteger;
import sun.management.FileSystem;

public class SerializableIntegerTest {

	private ByteArrayOutputStream out;

	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream(); 
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCompleteSerialization() {
		SerializableInteger originalInt = new SerializableInteger(19);
		try {
			originalInt.serialize(out);
		} catch (IOException e) {
			Assert.fail("Serializacion:" + e.toString());
		}
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		SerializableInteger deserializedInt = new SerializableInteger();
		try {
			deserializedInt.deserialize(in);
		} catch (IOException e) {
			Assert.fail("deserializacion:" + e.toString());
		}
		Assert.assertEquals(originalInt.getValue(), deserializedInt.getValue());
	}


}
