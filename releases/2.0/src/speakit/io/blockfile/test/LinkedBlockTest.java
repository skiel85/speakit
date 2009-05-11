package speakit.io.blockfile.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.blockfile.LinkedBlock;

public class LinkedBlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialState() throws IOException {
		LinkedBlock block = new LinkedBlock(0);
		Assert.assertEquals(-1, block.getNextBlockNumber());
		Assert.assertEquals(false, block.isRemoved());
		Assert.assertArrayEquals(new byte[] {}, block.getContent());
	}

	@Test
	public void testSerialization1() throws IOException {
		byte[] testBytes = new byte[] { 1, 31, 32, 76 };
		LinkedBlock block = new LinkedBlock(0);
		block.setContent(testBytes);
		block.setRemoved(true);
		block.setNextBlockNumber(1);
		serializeUnserializeAndTest(block);
	}

	@Test
	public void testSerialization2() throws IOException {
		byte[] testBytes = new byte[1000];
		LinkedBlock block = new LinkedBlock(4);
		block.setContent(testBytes);
		block.setRemoved(false);
		block.setNextBlockNumber(12);
		serializeUnserializeAndTest(block);
	}

	@Test
	public void testSerialization3() throws IOException {
		byte[] testBytes = null;
		LinkedBlock block = new LinkedBlock(4);
		block.setContent(testBytes);
		block.setRemoved(false);
		block.setNextBlockNumber(12);
		serializeUnserializeAndTest(block);
	}

	private void serializeUnserializeAndTest(LinkedBlock block) throws IOException {
		LinkedBlock deserializedBlock = new LinkedBlock(0);
		deserializedBlock.deserialize(block.serialize());
		Assert.assertEquals(block.isRemoved(), deserializedBlock.isRemoved());
		Assert.assertEquals(block.getNextBlockNumber(), deserializedBlock.getNextBlockNumber());
		Assert.assertArrayEquals(block.getContent(), deserializedBlock.getContent());
	}

	// TODO: crear a partir de un bloque deserializado y probar que se levante
	// bien

}