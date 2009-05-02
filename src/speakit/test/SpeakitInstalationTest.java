package speakit.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.Speakit;

public class SpeakitInstalationTest {
	private Speakit sut;
	private TestFileManager fileManager;
	private Configuration conf;

	@Before
	public void setUp() throws Exception {
		this.fileManager = new TestFileManager(this.getClass().getName());
		conf = new Configuration();
		conf.setBlockSize(256);
		conf.setTrieDepth(3);
		conf.create(fileManager);
		this.sut = new Speakit();
		this.sut.setFileManager(this.fileManager);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}

	@Test
	public void testInstall() throws IOException {
		sut.install(fileManager, conf);
	}

	@Test
	public void testIsNotInstalledInitially() throws IOException {
		Assert.assertEquals(false, sut.isInstalled(this.fileManager));
	}

	@Test
	public void testIsInstalledAfterInstallation() throws IOException {
		sut.load();
		Assert.assertEquals(true, sut.isInstalled(this.fileManager));
	}

	@Test
	public void testIsInstalledAfterInstallationAndReInstantation() throws IOException {
		Assert.assertEquals(false, sut.isInstalled(this.fileManager));
		sut.load();
		Assert.assertEquals(true, sut.isInstalled(this.fileManager));
		Speakit sut2 = new Speakit();
		Assert.assertEquals(true, sut2.isInstalled(this.fileManager));
	}

}
