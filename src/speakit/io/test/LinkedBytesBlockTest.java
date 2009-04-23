package speakit.io.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.LinkedBytesBlock;

public class LinkedBytesBlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialState() throws IOException {
		LinkedBytesBlock block = new LinkedBytesBlock(0);
		Assert.assertEquals(-1, block.getNextBlockNumber());
		Assert.assertEquals(false, block.getIsRemoved());
		Assert.assertArrayEquals(new byte[] {}, block.getBytes());
	}

	@Test
	public void testSerialization1() throws IOException {
		byte[] testBytes = new byte[] { 1, 31, 32, 76 };
		LinkedBytesBlock block = new LinkedBytesBlock(0);
		block.setBytes(testBytes);
		block.setIsRemoved(true);
		block.setNextBlockNumber(1);
		serializeUnserializeAndTest(block);
	}

	@Test
	public void testSerialization2() throws IOException {
		byte[] testBytes = new byte[1000];
		LinkedBytesBlock block = new LinkedBytesBlock(4);
		block.setBytes(testBytes);
		block.setIsRemoved(false);
		block.setNextBlockNumber(12);
		serializeUnserializeAndTest(block);
	}

	@Test
	public void testSerialization3() throws IOException {
		byte[] testBytes = null;
		LinkedBytesBlock block = new LinkedBytesBlock(4);
		block.setBytes(testBytes);
		block.setIsRemoved(false);
		block.setNextBlockNumber(12);
		serializeUnserializeAndTest(block);
	}

	private void serializeUnserializeAndTest(LinkedBytesBlock block) throws IOException {
		LinkedBytesBlock deserializedBlock = new LinkedBytesBlock(0);
		deserializedBlock.deserialize(block.serialize());
		Assert.assertEquals(block.getIsRemoved(), deserializedBlock.getIsRemoved());
		Assert.assertEquals(block.getNextBlockNumber(), deserializedBlock.getNextBlockNumber());
		Assert.assertArrayEquals(block.getBytes(), deserializedBlock.getBytes());
	}

	// TODO: crear a partir de un bloque deserializado y probar que se levante
	// bien

}