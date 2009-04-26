package speakit.io.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.Block;

public class BlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAppendContent() {
		Block block = new Block(1);
		block.setContent(new byte[] { 10, 20, 30, 40 });
		block.appendContent(new byte[] { 50, 60 });
		Assert.assertArrayEquals(new byte[] { 10, 20, 30, 40, 50, 60 }, block.getContent());
	}
}
