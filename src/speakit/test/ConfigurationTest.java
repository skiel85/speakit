package speakit.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;

public class ConfigurationTest {
	private Configuration conf;
	private TestFileManager fileManager;

	@Before
	public void setUp() throws Exception {
		fileManager = new TestFileManager(this.getClass().getName());
		conf = new Configuration();
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}

	@Test
	public void testLoadsCorrectly() throws IOException {
		conf.setBlockSize(51);
		conf.setTrieDepth(41);
		conf.create(fileManager);
		Assert.assertEquals(51, conf.getBlockSize());
		Assert.assertEquals(41, conf.getTrieDepth());
	}

}
