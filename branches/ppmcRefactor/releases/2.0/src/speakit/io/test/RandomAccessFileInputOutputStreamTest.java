package speakit.io.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.RandomAccessFileInputStream;
import speakit.io.RandomAccessFileOutputStream;

public class RandomAccessFileInputOutputStreamTest {
	private File file;
	private RandomAccessFile randomAccessFile;
	private InputStream inputStream;
	private OutputStream outputStream;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.randomAccessFile = new RandomAccessFile(this.file, "rw");
		this.inputStream = new RandomAccessFileInputStream(this.randomAccessFile);
		this.outputStream = new RandomAccessFileOutputStream(this.randomAccessFile);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testWriteAndReadAByte() throws IOException {
		this.outputStream.write(120);
		Assert.assertEquals(120, this.inputStream.read());
	}

	@Test
	public void testWriteAndReadAByteArray() throws IOException {
		byte[] testArray = new byte[] { 32, 12, 99, -10, 22 };
		byte[] buffer = new byte[5];
		this.outputStream.write(testArray);
		this.inputStream.read(buffer);
		Assert.assertArrayEquals(testArray, buffer);
	}

	@Test
	public void testReadTwice() throws IOException {
		byte[] testArray = new byte[] { 32, 12, 99, -10, 22 };
		byte[] buffer = new byte[2];
		this.outputStream.write(testArray);
		this.inputStream.read(buffer);
		Assert.assertArrayEquals(new byte[] { testArray[0], testArray[1] }, buffer);
		buffer = new byte[3];
		this.inputStream.read(buffer);
		Assert.assertArrayEquals(new byte[] { testArray[2], testArray[3], testArray[4] }, buffer);
	}

	@Test
	public void testWriteTwice() throws IOException {
		byte[] testArray = new byte[] { 32, 12, 99, -10, 22 };
		byte[] testArray1 = new byte[] { testArray[0], testArray[1] };
		byte[] testArray2 = new byte[] { testArray[2], testArray[3], testArray[4] };
		byte[] buffer = new byte[5];
		this.outputStream.write(testArray1);
		this.outputStream.write(testArray2);
		this.inputStream.read(buffer);
		Assert.assertArrayEquals(testArray, buffer);
	}

}