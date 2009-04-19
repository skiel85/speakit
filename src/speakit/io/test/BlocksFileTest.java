package speakit.io.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.io.BasicBlocksFile;
import speakit.io.BlocksFile;
import speakit.io.BytesBlock;

public class BlocksFileTest {

	private static final int	BLOCK_SIZE	= 256;

	File						file;

	BlocksFile					sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		BasicBlocksFile createdFile;
		createdFile = new BlocksFile(this.file);
		createdFile.create(BLOCK_SIZE);

		sut = new BlocksFile(this.file);
		sut.load();
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}
	
	@Test 
	public void testCreationGivesConsecutiveBlockNumbers() throws IOException{
		BytesBlock first=sut.getNewBlock();
		BytesBlock second=sut.getNewBlock();		 
		Assert.assertEquals(first.getBlockNumber()+1, second.getBlockNumber());
	}
	
	@Test 
	public void testReutilizesRemovedBlock() throws IOException{ 
		sut.getNewBlock();
		BytesBlock second=sut.getNewBlock(); 
		sut.getNewBlock();
		sut.removeBlock(second);
		//verifico que reutiliza el bloque "second" eliminado
		Assert.assertEquals(second.getBlockNumber(), sut.getNewBlock().getBlockNumber());
	}

	
}
