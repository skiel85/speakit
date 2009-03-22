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
import speakit.dictionary.SerializablePrimitiveType;
import speakit.dictionary.SerializableString;
import sun.management.FileSystem;

public class SerializablePrimitiveTypeTest {

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
		SerializableInteger original = new SerializableInteger(123);
		SerializableInteger deserialized = new SerializableInteger();
		serializeAndUnserialize(out,original,deserialized);
		Assert.assertEquals(original.getValue(), deserialized.getValue());
	}
	
	@Test
	public void testCompleteStringSerialization() {
		SerializableString original = new SerializableString("hola mundo!");
		SerializableString deserialized = new SerializableString();
		deserializeAndTest(original,deserialized);
	}
	
	private static void deserializeAndTest(SerializableString original,SerializableString deserialized){
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		serializeAndUnserialize(out1,original,deserialized); Assert.assertEquals(original.getValue(), deserialized.getValue());
		Assert.assertEquals(original.getSerializationSize(), deserialized.getSerializationSize());
		Assert.assertEquals(original.getSerializationSize(), deserialized.getSerializationSize());
	}
	
	@Test
	public void testDoubleCompleteStringSerialization() {
		SerializableString original = new SerializableString("!mundos hola");
		SerializableString deserialized = new SerializableString();
		deserializeAndTest(original,deserialized);
		SerializableString deserialized2 = new SerializableString();
		deserializeAndTest(deserialized,deserialized2);
	}
	
	private static void serializeAndUnserialize(ByteArrayOutputStream out,SerializablePrimitiveType original,SerializablePrimitiveType deserialized){
		try {
			original.serialize(out);
		} catch (IOException e) {
			Assert.fail("Serializacion:" + e.toString());
		}
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		try {
			deserialized.deserialize(in);
		} catch (IOException e) {
			Assert.fail("deserializacion:" + e.toString());
		}
	}


}
