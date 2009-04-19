package speakit.io.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.BytesBlock;
  

public class BytesBlockTest { 
 
	@Before
	public void setUp() throws Exception { 
	}

	@After
	public void tearDown() throws Exception { 
	}
	
	@Test 
	public void testSerialization1() throws IOException{
		byte[] testBytes = new byte[]{1,31,32,76};
		BytesBlock block = new BytesBlock(0);
		block.setBytes(testBytes);
		block.setIsRemoved(true);
		serializeUnserializeAndTest(block);
	}
	
	@Test 
	public void testSerialization2() throws IOException{
		byte[] testBytes = new byte[]{65,12,22,11,22,11,33};
		BytesBlock block = new BytesBlock(0);
		block.setBytes(testBytes);
		block.setIsRemoved(false);
		serializeUnserializeAndTest(block);
	}
	
	@Test 
	public void testSerialization3() throws IOException{
		byte[] testBytes = null;
		BytesBlock block = new BytesBlock(0);
		block.setBytes(testBytes);
		block.setIsRemoved(false);
		serializeUnserializeAndTest(block);
	}
	
	private void serializeUnserializeAndTest(BytesBlock block) throws IOException{ 
		BytesBlock deserializedBlock = new BytesBlock(0);
		deserializedBlock.deserialize(block.serialize());
		Assert.assertEquals(block.getIsRemoved(), deserializedBlock.getIsRemoved());
		Assert.assertArrayEquals(block.getBytes(), deserializedBlock.getBytes());
	}

	
}
