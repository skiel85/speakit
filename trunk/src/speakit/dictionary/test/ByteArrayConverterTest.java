package speakit.dictionary.test;

import org.junit.Assert;
import org.junit.Test;

import speakit.dictionary.serialization.ByteArrayConverter;

public class ByteArrayConverterTest {
	@Test
	public void testConvertToInt() throws Exception {
		byte[] array = new byte[] { 0x01, 0x0A, 0x10, 0x0F };
		Assert.assertEquals(17436687, ByteArrayConverter.toInt(array));
		Assert.assertEquals(0, ByteArrayConverter.toInt(new byte[] { 0x00, 0x00, 0x00, 0x00 }));
		Assert.assertEquals(1, ByteArrayConverter.toInt(new byte[] { 0x00, 0x00, 0x00, 0x01 }));
		Assert.assertEquals(256, ByteArrayConverter.toInt(new byte[] { 0x00, 0x00, 0x01, 0x00 }));
		Assert.assertEquals(9898984, ByteArrayConverter.toInt(ByteArrayConverter.toByteArray(9898984)));
		Assert.assertEquals(0, ByteArrayConverter.toInt(new byte[] { 0x00, 0x00, 0x01 }));

	}

	// @Test
	// public void testConvertToInt3() throws Exception {
	// // byte[] array = new byte[] { 0x01, 0x0A, 0x10, 0x0F };
	// Assert.assertEquals(022,ByteArrayConverter.toInt(ByteArrayConverter.toByta(022)));
	// }

	@Test
	public void testConvertToInt2() throws Exception {
		byte[] array = new byte[] { 0x11, 0x11, 0x23, 0x25 };
		Assert.assertEquals(286335781, ByteArrayConverter.toInt(array));
	}

	@Test
	public void testConvertToLong() throws Exception {
		byte[] array = new byte[] { 0x11, 0x11, 0x23, 0x25, 0x00, 0x00, 0x00, 0x00 };
		long num = 1229802815069618176L;

		Assert.assertEquals(num, ByteArrayConverter.toLong(array));
		Assert.assertEquals(0, ByteArrayConverter.toLong(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 }));
		Assert.assertEquals(76543L, ByteArrayConverter.toLong(ByteArrayConverter.toByteArray(76543L)));
		Assert.assertEquals(9976543L, ByteArrayConverter.toLong(ByteArrayConverter.toByteArray(9976543L)));
		Assert.assertEquals(9876549976543L, ByteArrayConverter.toLong(ByteArrayConverter.toByteArray(9876549976543L)));
		Assert.assertEquals(5456789087669876543L, ByteArrayConverter.toLong(ByteArrayConverter.toByteArray(5456789087669876543L)));

	}

}
