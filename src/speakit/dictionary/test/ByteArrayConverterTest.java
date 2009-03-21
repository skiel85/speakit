package speakit.dictionary.test;

import org.junit.Assert;
import org.junit.Test;

import speakit.dictionary.Converter;


public class ByteArrayConverterTest {
	@Test
	public void testConvertToInt() throws Exception {
		byte[] array=new byte[]{0x01,0x0A,0x10,0x0F};
		Assert.assertEquals(17436687, new Converter().toInt(array));
	}
	
	@Test
	public void testConvertToInt2() throws Exception {
		byte[] array=new byte[]{0x11, 0x11, 0x23, 0x25};
		Assert.assertEquals(286335781, new Converter().toInt(array ));
	}
	
	@Test
	public void testConvertToLong() throws Exception {
		byte[] array=new byte[]{0x11, 0x11, 0x23, 0x25,0x00, 0x00, 0x00, 0x00};
		long num = 1229802815069618176L;
		Assert.assertEquals( num, new Converter().toLong(array));
	}
	
	@Test
	public void testConvertToDouble() throws Exception {
		Double d = 1231.11;
		byte[] array=new Converter().toByta(d); 
		Assert.assertEquals( d, new Converter().toDouble(array));
	}
	
	
}
