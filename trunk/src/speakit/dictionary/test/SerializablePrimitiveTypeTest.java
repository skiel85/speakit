package speakit.dictionary.test;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.serialization.SerializableByteArray;
import speakit.dictionary.serialization.SerializableInteger;
import speakit.dictionary.serialization.SerializablePrimitiveType;
import speakit.dictionary.serialization.SerializableString;

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
		Assert.assertEquals(original.getInteger(), deserialized.getInteger());
	}
	
	@Test
	public void testCompleteStringSerialization() {
		SerializableString original = new SerializableString("hola mundo!");
		SerializableString deserialized = new SerializableString();
		deserializeAndTest(original,deserialized);
	}
	
	@Test
	public void testCompleteByteArraySerialization() {
		SerializableByteArray original = new SerializableByteArray("esto es un array de bytes".getBytes());
		SerializableByteArray deserialized = new SerializableByteArray();
		serializeAndUnserialize(out,original,deserialized);
		assertArrayEquals(original.getBytes(), deserialized.getBytes());
	}
	
	private static void deserializeAndTest(SerializableString original,SerializableString deserialized){
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		serializeAndUnserialize(out1,original,deserialized);
		Assert.assertEquals(original.getSerializationSize(), deserialized.getSerializationSize());
		Assert.assertEquals(original.getString(), deserialized.getString());
	}
	
	@Test
	public void testDoubleCompleteStringSerialization() {
		SerializableString original = new SerializableString("!mundos hola");
		SerializableString deserialized = new SerializableString();
		deserializeAndTest(original,deserialized);
		SerializableString deserialized2 = new SerializableString();
		deserializeAndTest(deserialized,deserialized2);
	}
	
	@Test
	public void testDoubleCompleteStringSerializationWithValueChange() {
		SerializableString original = new SerializableString("!mundos hola");
		SerializableString deserialized = new SerializableString();
		deserializeAndTest(original,deserialized);
		deserialized.setString("modific");
		SerializableString deserialized2 = new SerializableString();
		deserializeAndTest(deserialized,deserialized2);
	}
	
	@Test
	public void testSerializableByteArrayComparison() {
		byte[] bytes1 = new byte[]{12,21,34,12,29,8};
		byte[] bytes2 = new byte[]{12,21,11,12,29,8};
		byte[] greaterArray = new byte[]{12,21,34,12,29,8,0};
		
		SerializableByteArray a = new SerializableByteArray(bytes1);
		SerializableByteArray b = new SerializableByteArray(bytes2);

		Assert.assertEquals(1, a.compareTo(b));
		Assert.assertEquals(-1, b.compareTo(a));
		
		b.setBytes(bytes1);
		Assert.assertEquals(0, a.compareTo(b));
		
		b.setBytes(greaterArray);
		Assert.assertEquals(-1, a.compareTo(b));
		Assert.assertEquals(1, b.compareTo(a));
		
		Assert.assertTrue(a.compareTo(new SerializableInteger(1))<0);
		Assert.assertTrue(new SerializableInteger(1).compareTo(a)>0);
	}
	
	@Test
	public void testSerializableStringComparison() {
		SerializableString a = new SerializableString("aaaaad"); 
		SerializableString b = new SerializableString("aaaaac");
		SerializableString c = new SerializableString("aaaaada");
		SerializableString d = new SerializableString();
		
		Assert.assertTrue(a.compareTo(b)>0);	
		Assert.assertTrue(b.compareTo(a)<0);
		
		Assert.assertTrue(a.compareTo(c)<0);	
		Assert.assertTrue(c.compareTo(a)>0);
		
		Assert.assertTrue(b.compareTo(c)<0);	
		Assert.assertTrue(c.compareTo(b)>0);
		
		Assert.assertTrue(a.compareTo(d)>0);	
		Assert.assertTrue(d.compareTo(a)<0);
		
		Assert.assertTrue(d.compareTo(d)==0);
	}
	
	@Test
	public void testSerializableIntegerComparison() {
		SerializableInteger a = new SerializableInteger(1); 
		SerializableInteger b = new SerializableInteger(2);
		SerializableInteger c = new SerializableInteger(3);
		
		Assert.assertTrue(a.compareTo(b)<0);	
		Assert.assertTrue(b.compareTo(a)>0);
		
		Assert.assertTrue(a.compareTo(c)<0);	
		Assert.assertTrue(c.compareTo(a)>0);
		
		Assert.assertTrue(b.compareTo(c)<0);	
		Assert.assertTrue(c.compareTo(b)>0);
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
