package speakit.dictionary.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.AudioFile;

public class AudioFileTest {
	File file;
	AudioFile sut;
	
	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new AudioFile(file);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testAddAndGetAudio() throws IOException {
		byte[] audio = new byte[] {10, -25, 32, 64, -122, 89};
		this.sut.addAudio(audio);
		this.sut.getAudio(0);
	}

}
