package speakit.dictionary.test;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.io.record.BooleanField;
import speakit.io.record.ByteArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.LongField;
import speakit.io.record.StringField;

public class FieldTest {

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
		IntegerField original = new IntegerField(123);
		IntegerField deserialized = new IntegerField();
		serializeAndUnserialize(out, original, deserialized);
		Assert.assertEquals(original.getInteger(), deserialized.getInteger());
	}

	@Test
	public void testCompleteStringSerialization() {
		StringField original = new StringField("hola mundo!");
		StringField deserialized = new StringField();
		deserializeAndTest(original, deserialized);
	}

	@Test
	public void testCompleteBooleanSerialization() {
		BooleanField original1 = new BooleanField(true);
		BooleanField original2 = new BooleanField(false);
		BooleanField deserialized = new BooleanField();
		deserializeAndTest(original1, deserialized);
		deserializeAndTest(original2, deserialized);
	}

	@Test
	public void testCompleteByteArraySerialization() {
		ByteArrayField original = new ByteArrayField("esto es un array de bytes".getBytes());
		ByteArrayField deserialized = new ByteArrayField();
		serializeAndUnserialize(out, original, deserialized);
		assertArrayEquals(original.getBytes(), deserialized.getBytes());
	}

	private static void deserializeAndTest(StringField original, StringField deserialized) {
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		serializeAndUnserialize(out1, original, deserialized);
		Assert.assertEquals(original.getSerializationSize(), deserialized.getSerializationSize());
		Assert.assertEquals(original.getString(), deserialized.getString());
	}

	private static void deserializeAndTest(BooleanField original, BooleanField deserialized) {
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		serializeAndUnserialize(out1, original, deserialized);
		Assert.assertEquals(original.getSerializationSize(), deserialized.getSerializationSize());
		Assert.assertEquals(original.getBoolean(), deserialized.getBoolean());
	}

	@Test
	public void testDoubleCompleteStringSerialization() {
		StringField original = new StringField("!mundos hola");
		StringField deserialized = new StringField();
		deserializeAndTest(original, deserialized);
		StringField deserialized2 = new StringField();
		deserializeAndTest(deserialized, deserialized2);
	}

	@Test
	public void testDoubleCompleteStringSerializationWithValueChange() {
		StringField original = new StringField("!mundos hola");
		StringField deserialized = new StringField();
		deserializeAndTest(original, deserialized);
		deserialized.setString("modific");
		StringField deserialized2 = new StringField();
		deserializeAndTest(deserialized, deserialized2);
	}

	@Test
	public void testByteArrayFieldComparison() {
		byte[] bytes1 = new byte[] { 12, 21, 34, 12, 29, 8 };
		byte[] bytes2 = new byte[] { 12, 21, 11, 12, 29, 8 };
		byte[] greaterArray = new byte[] { 12, 21, 34, 12, 29, 8, 0 };

		ByteArrayField a = new ByteArrayField(bytes1);
		ByteArrayField b = new ByteArrayField(bytes2);

		Assert.assertEquals(1, a.compareTo(b));
		Assert.assertEquals(-1, b.compareTo(a));

		b.setBytes(bytes1);
		Assert.assertEquals(0, a.compareTo(b));

		b.setBytes(greaterArray);
		Assert.assertEquals(-1, a.compareTo(b));
		Assert.assertEquals(1, b.compareTo(a));

		Assert.assertTrue(a.compareTo(new IntegerField(1)) < 0);
		Assert.assertTrue(new IntegerField(1).compareTo(a) > 0);
	}

	@Test
	public void testStringFieldComparison() {
		StringField a = new StringField("aaaaad");
		StringField b = new StringField("aaaaac");
		StringField c = new StringField("aaaaada");
		StringField d = new StringField();
		StringField equal2a = new StringField("aaaaad");

		Assert.assertTrue(a.compareTo(b) > 0);
		Assert.assertTrue(b.compareTo(a) < 0);

		Assert.assertTrue(a.compareTo(c) < 0);
		Assert.assertTrue(c.compareTo(a) > 0);

		Assert.assertTrue(b.compareTo(c) < 0);
		Assert.assertTrue(c.compareTo(b) > 0);

		Assert.assertTrue(a.compareTo(d) > 0);
		Assert.assertTrue(d.compareTo(a) < 0);

		Assert.assertTrue(d.compareTo(d) == 0);

		Assert.assertTrue(a.compareTo(equal2a) == 0);
		Assert.assertTrue(equal2a.compareTo(a) == 0);
	}

	@Test
	public void testIntegerFieldComparison() {
		IntegerField a = new IntegerField(1);
		IntegerField b = new IntegerField(2);
		IntegerField c = new IntegerField(3);

		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(b.compareTo(a) > 0);

		Assert.assertTrue(a.compareTo(c) < 0);
		Assert.assertTrue(c.compareTo(a) > 0);

		Assert.assertTrue(b.compareTo(c) < 0);
		Assert.assertTrue(c.compareTo(b) > 0);
	}

	@Test
	public void testIntegerFieldCompareEquals() {
		Field a = new IntegerField(1);
		Field b = new IntegerField(1);

		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(b.compareTo(a) == 0);
	}

	@Test
	public void testLongFieldCompareEquals() {
		Field a = new LongField(99991999999L);
		Field b = new LongField(99991999999L);

		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(b.compareTo(a) == 0);
	}

	private static void serializeAndUnserialize(ByteArrayOutputStream out, Field original, Field deserialized) {
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
