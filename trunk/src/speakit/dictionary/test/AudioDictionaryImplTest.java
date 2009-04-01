package speakit.dictionary.test;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.audio.Audio;
import speakit.dictionary.AudioDictionaryImpl;


public class AudioDictionaryImplTest {

	private AudioDictionaryImpl	sut;
	private TestDictionaryFileSet	testFileSet;
	
	

	@Before
	public void setUp() throws Exception {
		this.sut = new AudioDictionaryImpl();
		testFileSet = new TestDictionaryFileSet();
		this.sut.load(testFileSet);
	}
	@After
	public void tearDown() throws Exception {
		this.testFileSet.destroyFiles();
	}

	@Test
	public void testAddAndRetrieve() throws Exception {
		String word = "hola";
		byte[] sound = new byte[] { 10, -25, 32, 64, -122, 89 };
		Audio audio = new Audio(sound,55);
		
		this.sut.addEntry(word, audio);
		Audio retrievedAudio = this.sut.getAudio(word);

		Assert.assertArrayEquals(audio.getBytes(), retrievedAudio.getBytes());
		Assert.assertEquals(audio.getDuration(), retrievedAudio.getDuration());
	}

	@Test
	public void testAddAndGetAfterReopenDictionary() throws Exception {
		// Audios de prueba
		byte[] sound1 = new byte[] { 10, -25, 32, 64, -122, 89, 55, 0, -3, 102 };
		byte[] sound2 = new byte[] { 4, 82, 36, 25, -30, -120, 78 };
		byte[] sound3 = new byte[] { 89, 23, 2, -1, 0, 64, 64, 9, -44 };

		Audio audio1=new Audio(sound1,10);
		Audio audio2=new Audio(sound2,1);
		Audio audio3=new Audio(sound3,9898984);
		
		// Archivos de prueba
		File file1 = File.createTempFile(this.getClass().getName(), ".dat");
		File file2 = File.createTempFile(this.getClass().getName(), ".dat");

		AudioDictionaryImpl audioDictionary1 = new AudioDictionaryImpl();
		audioDictionary1.load(testFileSet);

		// Agrego 2 entradas
		audioDictionary1.addEntry("palabra1", audio1);
		audioDictionary1.addEntry("palabra2", audio2);

		// Recargo el diccionario
		AudioDictionaryImpl audioDictionary2 = new AudioDictionaryImpl();
		audioDictionary2.load(testFileSet);
		
		// Agrego una entrada
		audioDictionary2.addEntry("palabra3", audio3);

		// Obtengo la última entrada agregada y verifico
		Assert.assertArrayEquals(audio3.getBytes(), audioDictionary2.getAudio("palabra3").getBytes());
		Assert.assertEquals(audio3.getDuration(), audioDictionary2.getAudio("palabra3").getDuration());

		// Elimino archivos temporales
		file1.delete();
		file2.delete();
	}

}
