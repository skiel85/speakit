package speakit.dictionary.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.dictionary.DictionaryImpl;

public class DictionaryImplTest {

	private DictionaryImpl sut;
	
	@Before
	public void setUp() throws Exception {
		this.sut = new DictionaryImpl();
	}

	@After
	public void tearDown() throws Exception {
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
