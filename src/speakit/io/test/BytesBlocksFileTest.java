package speakit.io.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.io.BlocksFile;
import speakit.io.BytesBlock;
import speakit.io.BytesBlocksFile;

public class BytesBlocksFileTest {

	private static final int	BLOCK_SIZE	= 256;

	File						file;

	BytesBlocksFile					sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		BlocksFile createdFile;
		createdFile = new BytesBlocksFile(this.file);
		createdFile.create(BLOCK_SIZE);

		sut = new BytesBlocksFile(this.file);
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
