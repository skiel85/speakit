package speakit.dictionary.test;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.audio.Audio;
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

		// Cargo el diccionario
		AudioFile audioFile1 = new AudioFile(file1);
		AudioIndexFile audioIndexFile1 = new AudioIndexFile(file2);
		AudioDictionaryImpl audioDictionary1 = new AudioDictionaryImpl(audioIndexFile1, audioFile1);

		// Agrego 2 entradas
		audioDictionary1.addEntry("palabra1", audio1);
		audioDictionary1.addEntry("palabra2", audio2);

		// Recargo el diccionario
		AudioFile audioFile2 = new AudioFile(file1);
		AudioIndexFile audioIndexFile2 = new AudioIndexFile(file2);
		AudioDictionaryImpl audioDictionary2 = new AudioDictionaryImpl(audioIndexFile2, audioFile2);

		// Agrego una entrada
		audioDictionary2.addEntry("palabra3", audio3);

		// Obtengo la �ltima entrada agregada y verifico
		Assert.assertArrayEquals(audio3.getBytes(), audioDictionary2.getAudio("palabra3").getBytes());
		Assert.assertEquals(audio3.getDuration(), audioDictionary2.getAudio("palabra3").getDuration());

		// Elimino archivos temporales
		file1.delete();
		file2.delete();
	}

}
