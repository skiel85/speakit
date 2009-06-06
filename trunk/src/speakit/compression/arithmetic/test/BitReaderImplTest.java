package speakit.compression.arithmetic.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.BitReaderImpl;
import speakit.compression.arithmetic.BitWriterImpl;

public class BitReaderImplTest {
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
		BitWriterImpl writer = new BitWriterImpl(os);

		for (int i = 0; i < 8; i++) {
			writer.write(false);
		}
		writer.close();

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		BitReaderImpl reader = new BitReaderImpl(is);

		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(false, reader.readBit());
		}
	}

	@Test
	public void testUnpackTheOnlyOneCompleteByte() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BitWriterImpl writer = new BitWriterImpl(os);

		for (int i = 0; i < 8; i++) {
			writer.write(false);
		}
		writer.close();

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		BitReaderImpl reader = new BitReaderImpl(is);

		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(false, reader.readBit());
		}
	}

	@Test
	public void testUnpackEightOnes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BitWriterImpl writer = new BitWriterImpl(os);

		for (int i = 0; i < 8; i++) {
			writer.write(true);
		}
		writer.close();

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		BitReaderImpl reader = new BitReaderImpl(is);

		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(true, reader.readBit());
		}
	}

	@Test
	public void testUnpackVariousBytes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BitWriterImpl writer = new BitWriterImpl(os);

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
		BitReaderImpl reader = new BitReaderImpl(is);

		Assert.assertEquals(true, reader.readBit());
		Assert.assertEquals(false, reader.readBit());
		Assert.assertEquals(false, reader.readBit());
		Assert.assertEquals(true, reader.readBit());
		Assert.assertEquals(true, reader.readBit());
		Assert.assertEquals(false, reader.readBit());
		Assert.assertEquals(true, reader.readBit());
		Assert.assertEquals(false, reader.readBit());
		Assert.assertEquals(true, reader.readBit());
		Assert.assertEquals(false, reader.readBit());
	}
}