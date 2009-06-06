package speakit.compression.arithmetic.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.BinaryBitReader;
import speakit.compression.arithmetic.Bit;

public class BitReaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testRead() throws Exception {
		BinaryBitReader reader = new BinaryBitReader(new InputStreamReader(new ByteArrayInputStream(new byte[]{1, 2, 4})));
		Assert.assertEquals("000000010000001000000100", new String(Bit.toCharArray(reader.readBits(8 * 3))));
		reader.readBit();
	}
}
