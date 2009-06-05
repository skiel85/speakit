package speakit.compression.arithmetic.test;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.BitPacker;

public class BitPackerTest {
	private BitPacker sut;

	@Before
	public void setUp() throws Exception {
		sut = new BitPacker();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testPackEightZeroes() throws IOException {
		for (int i = 0; i < 8; i++) {
			sut.pack(false);
		}
		List<Byte> bytes = sut.flush();
		Assert.assertEquals(1, bytes.size());
		Assert.assertEquals("00000000", byteToBinaryString(bytes.get(0)));
	}

	@Test
	public void testPackEightOnes() throws IOException {
		for (int i = 0; i < 8; i++) {
			sut.pack(true);
		}
		List<Byte> bytes = sut.flush();
		Assert.assertEquals(1, bytes.size());
		Assert.assertEquals("11111111", byteToBinaryString(bytes.get(0)));
	}

	@Test
	public void testPack01010101() throws IOException {
		boolean bit = false;
		for (int i = 0; i < 8; i++) {
			sut.pack(bit);
			bit = !bit;
		}
		List<Byte> bytes = sut.flush();
		Assert.assertEquals(1, bytes.size());
		Assert.assertEquals("01010101", byteToBinaryString(bytes.get(0)));
	}

	@Test
	public void testPackThreeBytes() {
		boolean bit = false;
		for (int i = 0; i < 8; i++) {
			sut.pack(bit);
		}
		bit = !bit;
		for (int i = 0; i < 8; i++) {
			sut.pack(bit);
		}
		bit = !bit;
		for (int i = 0; i < 8; i++) {
			sut.pack(bit);
			bit = !bit;
		}
		List<Byte> bytes = sut.flush();
		Assert.assertEquals(3, bytes.size());
		Assert.assertEquals("00000000", byteToBinaryString(bytes.get(0)));
		Assert.assertEquals("11111111", byteToBinaryString(bytes.get(1)));
		Assert.assertEquals("01010101", byteToBinaryString(bytes.get(2)));
	}

	@Test
	public void testPadding() {
		sut.pack(true);
		sut.pack(false);
		sut.pack(false);
		sut.pack(true);
		sut.pack(true);
		sut.pack(false);
		sut.pack(true);
		sut.pack(false);
		sut.pack(true);
		sut.pack(false);
		sut.end();

		List<Byte> bytes = sut.flush();
		Assert.assertEquals(2, bytes.size());
		Assert.assertEquals("10011010", byteToBinaryString(bytes.get(0)));
		Assert.assertEquals("10100000", byteToBinaryString(bytes.get(1)));
	}

	@Test
	public void testPaddingExtremeCase() {
		sut.pack(true);
		sut.pack(false);
		sut.pack(false);
		sut.pack(true);
		sut.pack(true);
		sut.pack(false);
		sut.pack(true);
		sut.pack(false);
		sut.end();

		List<Byte> bytes = sut.flush();
		Assert.assertEquals(2, bytes.size());
		Assert.assertEquals("10011010", byteToBinaryString(bytes.get(0)));
		Assert.assertEquals("10000000", byteToBinaryString(bytes.get(1)));
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

	@Test
	public void testUnpackTheOnlyOneByte() throws IOException {
		for (int i = 0; i < 7; i++) {
			sut.pack(false);
		}
		for (int i = 0; i < 7; i++) {
			Assert.assertEquals(false, sut.unpack());
		}
	}

	@Test
	public void testUnpackTheOnlyOneCompleteByte() throws IOException {
		for (int i = 0; i < 8; i++) {
			sut.pack(false);
		}
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(false, sut.unpack());
		}
	}
	
	@Test
	public void testUnpackEightOnes() throws IOException {
		for (int i = 0; i < 8; i++) {
			sut.pack(true);
		}
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(true, sut.unpack());
		}
	}

	@Test
	public void testUnpackVariousBytes() throws IOException {
		sut.pack(true);
		sut.pack(false);
		sut.pack(false);
		sut.pack(true);
		sut.pack(true);
		sut.pack(false);
		sut.pack(true);
		sut.pack(false);
		sut.pack(true);
		sut.pack(false);

		Assert.assertEquals(true, sut.unpack());
		Assert.assertEquals(false, sut.unpack());
		Assert.assertEquals(false, sut.unpack());
		Assert.assertEquals(true, sut.unpack());
		Assert.assertEquals(true, sut.unpack());
		Assert.assertEquals(false, sut.unpack());
		Assert.assertEquals(true, sut.unpack());
		Assert.assertEquals(false, sut.unpack());
		Assert.assertEquals(true, sut.unpack());
		Assert.assertEquals(false, sut.unpack());
	}
}