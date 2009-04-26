package speakit.dictionary.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.audio.Audio;
import speakit.dictionary.audiofile.AudioFile;
import speakit.io.record.RecordSerializationException;

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
	public void testAddAndGetAudio() throws IOException, RecordSerializationException {
		byte[] sound = new byte[] { 10, -25, 32, 64, -122, 89 };
		this.sut.addAudio(new Audio(sound));
		Assert.assertArrayEquals(sound, this.sut.getAudio(0).getBytes());
	}

	@Test
	public void testAddSomeAudiosAndGetOne() throws IOException, RecordSerializationException {
		byte[] sound1 = new byte[] { 10, -25, 32, 64, -122, 89, 55, 0, -3, 102 };
		byte[] sound2 = new byte[] { 4, 82, 36, 25, -30, -120, 78 };
		byte[] sound3 = new byte[] { 89, 23, 2, -1, 0, 64, 64, 9, -44 };
		Audio audio1 = new Audio(sound1);
		Audio audio2 = new Audio(sound2);
		Audio audio3 = new Audio(sound3);

		long offset1 = this.sut.addAudio(audio1);
		long offset2 = this.sut.addAudio(audio2);
		long offset3 = this.sut.addAudio(audio3);
		Assert.assertArrayEquals(audio1.getBytes(), this.sut.getAudio(offset1).getBytes());
		Assert.assertArrayEquals(audio2.getBytes(), this.sut.getAudio(offset2).getBytes());
		Assert.assertArrayEquals(audio3.getBytes(), this.sut.getAudio(offset3).getBytes());
		Assert.assertArrayEquals(audio2.getBytes(), this.sut.getAudio(offset2).getBytes());

		assertAudioEquals(audio1, this.sut.getAudio(offset1));
		assertAudioEquals(audio2, this.sut.getAudio(offset2));
		assertAudioEquals(audio3, this.sut.getAudio(offset3));
		assertAudioEquals(audio2, this.sut.getAudio(offset2));
	}

	private void assertAudioEquals(Audio audio1, Audio audio) {
		Assert.assertArrayEquals(audio1.getBytes(), audio.getBytes());
	}

	@Test
	public void testAddAndGetAfterReopenAudioFile() throws Exception {
		// Audios de prueba
		byte[] sound1 = new byte[] { 10, -25, 32, 64, -122, 89, 55, 0, -3, 102 };
		byte[] sound2 = new byte[] { 4, 82, 36, 25, -30, -120, 78 };
		byte[] sound3 = new byte[] { 89, 23, 2, -1, 0, 64, 64, 9, -44 };
		byte[] sound4 = new byte[] { 8, 5, 99 };

		Audio audio1 = new Audio(sound1);
		Audio audio2 = new Audio(sound2);
		Audio audio3 = new Audio(sound3);
		Audio audio4 = new Audio(sound4);

		// Archivos de prueba
		File file = File.createTempFile(this.getClass().getName(), ".dat");

		// Cargo el archivo de audio
		AudioFile audioFile1 = new AudioFile(file);

		// Agrego 2 entradas
		long offset1 = audioFile1.addAudio(audio1);
		long offset2 = audioFile1.addAudio(audio2);

		// Recargo el archivo de audio
		AudioFile audioFile2 = new AudioFile(file);

		// Agrego una entrada
		long offset3 = audioFile2.addAudio(audio3);
		long offset4 = audioFile2.addAudio(audio4);

		// Obtengo la última entrada agregada y verifico
		assertAudioEquals(audio1, audioFile2.getAudio(offset1));
		assertAudioEquals(audio2, audioFile2.getAudio(offset2));
		assertAudioEquals(audio3, audioFile2.getAudio(offset3));
		assertAudioEquals(audio4, audioFile2.getAudio(offset4));

		// Elimino archivos temporales
		file.delete();
	}

}
