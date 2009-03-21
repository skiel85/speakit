package speakit.dictionary.test;

import org.junit.Assert;
import org.junit.Test;

import speakit.dictionary.ByteArrayConverter;


public class ByteArrayConverterTest {
	@Test
	public void testConvertToInt() throws Exception {
		byte[] array=new byte[]{0x01,0x0A,0x10,0x0F};
		Assert.assertEquals(17436687, new ByteArrayConverter().toInt(array));
	}
}
