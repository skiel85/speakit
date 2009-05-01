package speakit.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.Speakit;
import speakit.TextDocument;
import speakit.WordAudio;
import speakit.audio.Audio;
import speakit.io.record.RecordSerializationException;

public class SpeakitTest {

	private FileManager	fileManager;
	private Speakit		sut;

	@Before
	public void setUp() throws Exception {
		this.fileManager = new TestFileManager(this.getClass().getName());
		this.sut = new Speakit();
		this.sut.install(this.fileManager, new Configuration());
		this.sut.load(this.fileManager);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}

	public TextDocument createTextDocument(String text) throws FileNotFoundException, IOException {
		File file = File.createTempFile(this.getClass().getName(), ".txt");
		OutputStream os = new FileOutputStream(file);
		os.write(text.getBytes());
		os.close();

		TextDocument textDocument = this.sut.getTextDocumentFromFile(file.getAbsolutePath());

		file.delete();

		return textDocument;
	}

	@Test
	public void testCreateTextDocument() throws FileNotFoundException, IOException {
		Iterator<String> it = createTextDocument("Hola mundo.").iterator();
		Assert.assertEquals("hola", it.next());
		Assert.assertEquals("mundo", it.next());
		Assert.assertFalse(it.hasNext());

	}

	@Test
	public void testAddDocumentAndGetUnknownWords() throws IOException {
		Iterator<String> it = this.sut.addDocument(createTextDocument("Ser o no ser, esa es la cuestión.")).iterator();
		Assert.assertEquals("ser", it.next());
		Assert.assertEquals("o", it.next());
		Assert.assertEquals("no", it.next());
		Assert.assertEquals("esa", it.next());
		Assert.assertEquals("es", it.next());
		Assert.assertEquals("la", it.next());
		Assert.assertEquals("cuestión", it.next());
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testAddWordAndGetUnknownWords() throws IOException, RecordSerializationException {
		WordAudio wordAudio = new WordAudio("esa", new Audio(new byte[]{1, 34, -65, 77, 82}));
		this.sut.addWordAudio(wordAudio);
		Iterator<String> it = this.sut.addDocument(createTextDocument("Ser o no ser, esa es la cuestión.")).iterator();

		Assert.assertEquals("ser", it.next());
		Assert.assertEquals("o", it.next());
		Assert.assertEquals("no", it.next());
		Assert.assertEquals("es", it.next());
		Assert.assertEquals("la", it.next());
		Assert.assertEquals("cuestión", it.next());

		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testConvertToAudioDocument() throws IOException, RecordSerializationException {
		WordAudio wordAudioSer = new WordAudio("ser", new Audio(new byte[]{1, 34, -65, 77, 82}));
		WordAudio wordAudioO = new WordAudio("o", new Audio(new byte[]{3, 8, -65, 54, 82}));
		WordAudio wordAudioNo = new WordAudio("no", new Audio(new byte[]{1, 9, -5, 77, 3}));
		WordAudio wordAudioEsa = new WordAudio("esa", new Audio(new byte[]{-91, 34, 65, 88, 82}));
		WordAudio wordAudioEs = new WordAudio("es", new Audio(new byte[]{111, 34, -65, 8, 82}));
		WordAudio wordAudioLa = new WordAudio("la", new Audio(new byte[]{112, 0, -65, 65, 82}));
		WordAudio wordAudioCuestion = new WordAudio("cuestión", new Audio(new byte[]{1, -24, -3, 77, 80}));
		this.sut.addWordAudio(wordAudioSer);
		this.sut.addWordAudio(wordAudioO);
		this.sut.addWordAudio(wordAudioNo);
		this.sut.addWordAudio(wordAudioEsa);
		this.sut.addWordAudio(wordAudioEs);
		this.sut.addWordAudio(wordAudioLa);
		this.sut.addWordAudio(wordAudioCuestion);
		Iterator<WordAudio> it = this.sut.convertToAudioDocument(createTextDocument("Ser o no ser, esa es la cuestión."));
		Assert.assertArrayEquals(wordAudioSer.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioO.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioNo.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioSer.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioEsa.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioEs.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioLa.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioCuestion.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testRestartSpeakitAndContinueAddingWords() throws IOException, RecordSerializationException {
		this.sut = new Speakit();
		this.sut.load(this.fileManager);

		WordAudio wordAudioSer = new WordAudio("ser", new Audio(new byte[]{1, 34, -65, 77, 82}));
		WordAudio wordAudioO = new WordAudio("o", new Audio(new byte[]{3, 8, -65, 54, 82}));
		WordAudio wordAudioNo = new WordAudio("no", new Audio(new byte[]{1, 9, -5, 77, 3}));
		WordAudio wordAudioEsa = new WordAudio("esa", new Audio(new byte[]{-91, 34, 65, 88, 82}));
		this.sut.addWordAudio(wordAudioSer);
		this.sut.addWordAudio(wordAudioO);
		this.sut.addWordAudio(wordAudioNo);
		this.sut.addWordAudio(wordAudioEsa);

		this.sut = new Speakit();
		this.sut.load(this.fileManager);

		WordAudio wordAudioEs = new WordAudio("es", new Audio(new byte[]{111, 34, -65, 8, 82}));
		WordAudio wordAudioLa = new WordAudio("la", new Audio(new byte[]{112, 0, -65, 65, 82}));
		WordAudio wordAudioCuestion = new WordAudio("cuestión", new Audio(new byte[]{1, -24, -3, 77, 80}));
		this.sut.addWordAudio(wordAudioEs);
		this.sut.addWordAudio(wordAudioLa);
		this.sut.addWordAudio(wordAudioCuestion);

		Iterator<WordAudio> it = this.sut.convertToAudioDocument(createTextDocument("Ser o no ser, esa es la cuestión."));
		Assert.assertArrayEquals(wordAudioSer.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioO.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioNo.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioSer.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioEsa.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioEs.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioLa.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertArrayEquals(wordAudioCuestion.getAudio().getBytes(), it.next().getAudio().getBytes());
		Assert.assertFalse(it.hasNext());
	}
}
