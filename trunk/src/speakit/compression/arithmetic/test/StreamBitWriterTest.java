package speakit.compression.arithmetic.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.StreamBitWriter;

public class StreamBitWriterTest {
	private ByteArrayOutputStream os;
	private StreamBitWriter sut;

	@Before
	public void setUp() throws Exception {
		os = new ByteArrayOutputStream();
		sut = new StreamBitWriter(os);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteEightZeroes() throws IOException {
		for (int i = 0; i < 8; i++) {
			sut.write(false);
		}
		sut.close();
		byte[] bytes = os.toByteArray();
		Assert.assertEquals(2, bytes.length);
		Assert.assertEquals("00000000", byteToBinaryString(bytes[0]));
		Assert.assertEquals("10000000", byteToBinaryString(bytes[1]));
	}

	@Test
	public void testWriteEightOnes() throws IOException {
		for (int i = 0; i < 8; i++) {
			sut.write(true);
		}
		sut.close();
		byte[] bytes = os.toByteArray();
		Assert.assertEquals(2, bytes.length);
		Assert.assertEquals("11111111", byteToBinaryString(bytes[0]));
		Assert.assertEquals("10000000", byteToBinaryString(bytes[1]));
	}

	@Test
	public void testWrite01010101() throws IOException {
		boolean bit = false;
		for (int i = 0; i < 8; i++) {
			sut.write(bit);
			bit = !bit;
		}
		sut.close();
		byte[] bytes = os.toByteArray();
		Assert.assertEquals(2, bytes.length);
		Assert.assertEquals("01010101", byteToBinaryString(bytes[0]));
		Assert.assertEquals("10000000", byteToBinaryString(bytes[1]));
	}

	@Test
	public void testWriteThreeBytes() throws IOException {
		boolean bit = false;
		for (int i = 0; i < 8; i++) {
			sut.write(bit);
		}
		bit = !bit;
		for (int i = 0; i < 8; i++) {
			sut.write(bit);
		}
		bit = !bit;
		for (int i = 0; i < 8; i++) {
			sut.write(bit);
			bit = !bit;
		}
		sut.close();
		byte[] bytes = os.toByteArray();
		Assert.assertEquals(4, bytes.length);
		Assert.assertEquals("00000000", byteToBinaryString(bytes[0]));
		Assert.assertEquals("11111111", byteToBinaryString(bytes[1]));
		Assert.assertEquals("01010101", byteToBinaryString(bytes[2]));
		Assert.assertEquals("10000000", byteToBinaryString(bytes[3]));
	}

	@Test
	public void testPadding() throws IOException {
		sut.write(true);
		sut.write(false);
		sut.write(false);
		sut.write(true);
		sut.write(true);
		sut.write(false);
		sut.write(true);
		sut.write(false);
		sut.write(true);
		sut.write(false);

		sut.close();
		byte[] bytes = os.toByteArray();
		
		Assert.assertEquals(2, bytes.length);
		Assert.assertEquals("10011010", byteToBinaryString(bytes[0]));
		Assert.assertEquals("10100000", byteToBinaryString(bytes[1]));
	}

	@Test
	public void testPaddingExtremeCase() throws IOException {
		sut.write(true);
		sut.write(false);
		sut.write(false);
		sut.write(true);
		sut.write(true);
		sut.write(false);
		sut.write(true);
		sut.write(false);
		
		sut.close();
		byte[] bytes = os.toByteArray();

		Assert.assertEquals(2, bytes.length);
		Assert.assertEquals("10011010", byteToBinaryString(bytes[0]));
		Assert.assertEquals("10000000", byteToBinaryString(bytes[1]));
	}

	private String byteToBinaryString(Byte b) {
		String result = Integer.toBinaryString(b);
		if (result.length() > 8) {
			result = result.substring(3 * 8);
		} else {
			for (int i = result.length(); i < 8; i++) {
				result = "0" + result;
			}
		}
		return result;
	}

//	@Test
//	public void testUnpackTheOnlyOneByte() throws IOException {
//		for (int i = 0; i < 7; i++) {
//			sut.pack(false);
//		}
//		for (int i = 0; i < 7; i++) {
//			Assert.assertEquals(false, sut.unpack());
//		}
//	}
//
//	@Test
//	public void testUnpackTheOnlyOneCompleteByte() throws IOException {
//		for (int i = 0; i < 8; i++) {
//			sut.pack(false);
//		}
//		for (int i = 0; i < 8; i++) {
//			Assert.assertEquals(false, sut.unpack());
//		}
//	}
//
//	@Test
//	public void testUnpackEightOnes() throws IOException {
//		for (int i = 0; i < 8; i++) {
//			sut.pack(true);
//		}
//		for (int i = 0; i < 8; i++) {
//			Assert.assertEquals(true, sut.unpack());
//		}
//	}
//
//	@Test
//	public void testUnpackVariousBytes() throws IOException {
//		sut.pack(true);
//		sut.pack(false);
//		sut.pack(false);
//		sut.pack(true);
//		sut.pack(true);
//		sut.pack(false);
//		sut.pack(true);
//		sut.pack(false);
//		sut.pack(true);
//		sut.pack(false);
//
//		Assert.assertEquals(true, sut.unpack());
//		Assert.assertEquals(false, sut.unpack());
//		Assert.assertEquals(false, sut.unpack());
//		Assert.assertEquals(true, sut.unpack());
//		Assert.assertEquals(true, sut.unpack());
//		Assert.assertEquals(false, sut.unpack());
//		Assert.assertEquals(true, sut.unpack());
//		Assert.assertEquals(false, sut.unpack());
//		Assert.assertEquals(true, sut.unpack());
//		Assert.assertEquals(false, sut.unpack());
//	}
}