package speakit.io.blockfile.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.blockfile.BasicBlockFile;
import speakit.io.blockfile.BasicBlockFileImpl;

public class BasicBlockFileCreationTest {

	private static final int BLOCK_SIZE = 512;

	File file;

	private BasicBlockFile createdFile;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.createdFile = new BasicBlockFileImpl(this.file);
		this.createdFile.create(BLOCK_SIZE);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testBlockSizeAfterCreated() throws IOException {
		Assert.assertEquals(BLOCK_SIZE, createdFile.getBlockSize());
		Assert.assertEquals(BLOCK_SIZE, createdFile.getFileSize());
	}

}
