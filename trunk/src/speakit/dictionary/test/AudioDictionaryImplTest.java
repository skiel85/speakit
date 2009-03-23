package speakit.dictionary.test;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.AudioDictionaryImpl;
import speakit.dictionary.files.audiofile.AudioFile;
import speakit.dictionary.files.audioindexfile.AudioIndexFile;

public class AudioDictionaryImplTest {

	private AudioDictionaryImpl sut;
	private File file1;
	private File file2;	
	
	@Before
	public void setUp() throws Exception {
		file1 = File.createTempFile(this.getClass().getName(), ".dat");
		file2 = File.createTempFile(this.getClass().getName(), ".dat");
		
		AudioFile audioFile = new AudioFile(file1);
		AudioIndexFile audioIndexFile = new AudioIndexFile(file2);
		this.sut = new AudioDictionaryImpl(audioIndexFile, audioFile);
	}

	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
	}
	
	@Test
	public void testAddAndRetrieve() throws Exception {
		String word = "hola"; 
		byte[] audio = new byte[] {10, -25, 32, 64, -122, 89};
		
		this.sut.addEntry(word, audio);
		byte[] retrievedAudio = this.sut.getAudio(word);
		
		Assert.assertArrayEquals(audio, retrievedAudio);
	}

}
