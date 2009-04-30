package speakit.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.Speakit;

public class SpeakitInstalationTest {
	private TestFileManager fileSet;
	private Speakit sut;

	@Before
	public void setUp() throws Exception {
		this.fileSet = new TestFileManager(this.getClass().getName());
		this.sut = new Speakit();
		this.sut.load(this.fileSet);
	}

	@After
	public void tearDown() throws Exception {
		this.fileSet.destroyFiles();
	}

	@Test
	public void testInstall() throws IOException{
		FileManager fileManager=new FileManager();
		Configuration conf=new Configuration();
		sut.install(fileManager,conf); 
	}
 
}
