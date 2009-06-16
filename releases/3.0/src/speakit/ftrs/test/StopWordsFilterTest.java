package speakit.ftrs.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.ftrs.StopWordsFilter;
import speakit.test.TestFileManager;

public class StopWordsFilterTest {

	private StopWordsFilter filter;
	private FileManager fileManager;
	private Configuration conf;
	@Before
	public void setUp() throws IOException {
		fileManager = new TestFileManager(this.getClass().getName());
		conf = new Configuration();
		filter = new StopWordsFilter();
	}
	
	@Test
	public void testInstall() throws IOException {
		if (!filter.isInstalled(fileManager))
			filter.install(new FileManager(), new Configuration());
		filter.load(fileManager, conf);
		File file = new File(filter.getFileName());
		Assert.assertTrue("El archivo deberia haberse creado", file.exists());
	}
	@Test
	public void testAddWordsToStopWords() throws IOException {
		if (!filter.isInstalled(fileManager))
			filter.install(new FileManager(), new Configuration());
		filter.load(fileManager, conf);
		File file = new File(filter.getFileName());
		Assert.assertTrue("El archivo deberia haberse creado", file.exists());
	}
	@Test
	public void testGetRelevantWords() throws IOException{
		if (!filter.isInstalled(fileManager))
			filter.install(new FileManager(), new Configuration());
		String text = "un camión estaba ante nosotros argüello //,";
		TextDocument document = new TextDocument(text);
		Assert.assertEquals("camión argüello", this.filter.getRelevantWords(document).getText()); 
	}
	@Ignore
	//Posible mejora
	public void addNewStopWord() throws IOException{
		/*if (!filter.isInstalled(fileManager))
			filter.install(new FileManager(), new Configuration());
		String word = "sarasa";
		Assert.assertFalse("la palabra no deberia estar", filter.isStopWord(word, fileManager, conf));
		filter.addNewStopWord(word);
		Assert.assertTrue("Ya la agregue, tiene q estar la muy guacha!", filter.isStopWord(word, fileManager, conf));
		*/
	}
}
