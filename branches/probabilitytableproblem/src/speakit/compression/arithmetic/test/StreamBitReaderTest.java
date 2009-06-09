package speakit.compression.arithmetic.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.Bit;
import speakit.compression.arithmetic.BitWriter;
import speakit.compression.arithmetic.StreamBitReader;
import speakit.compression.arithmetic.StreamBitWriter;
import speakit.compression.arithmetic.StringBitReader;

public class StreamBitReaderTest {
	private ByteArrayOutputStream os;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUnpackTheOnlyOneByte() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamBitWriter writer = new StreamBitWriter(os);

		for (int i = 0; i < 8; i++) {
			writer.write(false);
		}
		writer.close();

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		StreamBitReader reader = new StreamBitReader(is);

		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(false, reader.readBit().getBit());
		}
	}

	@Test
	public void testUnpackTheOnlyOneCompleteByte() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamBitWriter writer = new StreamBitWriter(os);

		for (int i = 0; i < 8; i++) {
			writer.write(false);
		}
		writer.close();

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		StreamBitReader reader = new StreamBitReader(is);

		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(false, reader.readBit().getBit());
		}
	}

	@Test
	public void testUnpackEightOnes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamBitWriter writer = new StreamBitWriter(os);

		for (int i = 0; i < 8; i++) {
			writer.write(true);
		}
		writer.close();

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		StreamBitReader reader = new StreamBitReader(is);

		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(true, reader.readBit().getBit());
		}
	}

	@Test
	public void testUnpackVariousBytes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamBitWriter writer = new StreamBitWriter(os);

		writer.write(true);
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(true);
		writer.write(false);
		writer.write(true);
		writer.write(false);
		writer.write(true);
		writer.write(false);
		writer.close();

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		StreamBitReader reader = new StreamBitReader(is);

		Assert.assertEquals(true, reader.readBit().getBit());
		Assert.assertEquals(false, reader.readBit().getBit());
		Assert.assertEquals(false, reader.readBit().getBit());
		Assert.assertEquals(true, reader.readBit().getBit());
		Assert.assertEquals(true, reader.readBit().getBit());
		Assert.assertEquals(false, reader.readBit().getBit());
		Assert.assertEquals(true, reader.readBit().getBit());
		Assert.assertEquals(false, reader.readBit().getBit());
		Assert.assertEquals(true, reader.readBit().getBit());
		Assert.assertEquals(false, reader.readBit().getBit());
	}
	
	@Test
	public void testWriteAndRead() throws Exception {
		String bits = "01010111" + "11100110";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BitWriter writer = new StreamBitWriter(os);

		writer.write(bits);
		os.flush();

		byte[] byteArray = os.toByteArray();
		
		StreamBitReader reader = new StreamBitReader(new ByteArrayInputStream(byteArray));

		Bit[] readBits = reader.readBits(bits.length());

		String stringReadBits = new String(Bit.toCharArray(readBits));

		Assert.assertEquals(bits, stringReadBits);
	}

	@Test
	public void testStringBitReader() throws Exception {
		String bits = "01010111" + "11100110";

		StringBitReader reader = new StringBitReader(bits); 
		Bit[] readBits = reader.readBits(bits.length()); 
		String stringReadBits = new String(Bit.toCharArray(readBits)); 
		Assert.assertEquals(bits, stringReadBits);
	}
}