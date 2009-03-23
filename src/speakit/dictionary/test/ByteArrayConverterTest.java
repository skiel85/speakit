package speakit.dictionary.test;

import org.junit.Assert;
import org.junit.Test;

import speakit.dictionary.ByteArrayConverter;


public class ByteArrayConverterTest {
	@Test
	public void testConvertToInt() throws Exception {
		byte[] array=new byte[]{0x01,0x0A,0x10,0x0F};
		Assert.assertEquals(17436687, ByteArrayConverter.toInt(array));
	}
	
	@Test
	public void testConvertToInt2() throws Exception {
		byte[] array=new byte[]{0x11, 0x11, 0x23, 0x25};
		Assert.assertEquals(286335781, ByteArrayConverter.toInt(array ));
	}
	
	@Test
	public void testConvertToLong() throws Exception {
		byte[] array=new byte[]{0x11, 0x11, 0x23, 0x25,0x00, 0x00, 0x00, 0x00};
		long num = 1229802815069618176L;
		Assert.assertEquals( num, ByteArrayConverter.toLong(array));
	}
	
	@Test
	public void testConvertToDouble() throws Exception {
		Double d = 1231.11;
		byte[] array=ByteArrayConverter.toByta(d); 
		Assert.assertEquals( d, ByteArrayConverter.toDouble(array));
	}
	
	
}
