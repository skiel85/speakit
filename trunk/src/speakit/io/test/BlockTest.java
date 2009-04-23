package speakit.io.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.files.RecordSerializationException;
import speakit.io.RemovableBlock;

public class BlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSerialization1() throws IOException, RecordSerializationException {
		byte[] testBytes = new byte[] { 1, 31, 32, 76 };
		RemovableBlock block = new RemovableBlock(0);
		block.setContent(testBytes);
		block.setRemoved(true);
		serializeUnserializeAndTest(block);
	}

	@Test
	public void testSerialization2() throws IOException, RecordSerializationException {
		byte[] testBytes = new byte[] { 65, 12, 22, 11, 22, 11, 33 };
		RemovableBlock block = new RemovableBlock(0);
		block.setContent(testBytes);
		block.setRemoved(false);
		serializeUnserializeAndTest(block);
	}

	@Test
	public void testSerialization3() throws IOException, RecordSerializationException {
		byte[] testBytes = null;
		RemovableBlock block = new RemovableBlock(0);
		block.setContent(testBytes);
		block.setRemoved(false);
		serializeUnserializeAndTest(block);
	}

	private void serializeUnserializeAndTest(RemovableBlock block) throws IOException, RecordSerializationException {
		RemovableBlock deserializedBlock = new RemovableBlock(0);
		deserializedBlock.deserialize(block.serialize());
		Assert.assertEquals(block.isRemoved(), deserializedBlock.isRemoved());
		Assert.assertArrayEquals(block.getContent(), deserializedBlock.getContent());
	}

}
