package speakit.dictionary.test;


import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.AudioIndexFile;

public class AudioIndexFileTest {

	File file;
	AudioIndexFile sut;
	
	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new AudioIndexFile(file);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testAddAndGetAudio() throws IOException {
		this.sut.addEntry("hola", 88761);
		Assert.assertEquals(88761, this.sut.getOffset("hola"));
	}

}
