package speakit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import speakit.dictionary.AudioDictionaryImpl;
import speakit.dictionary.DictionaryFileSet;
import speakit.dictionary.files.RecordSerializationException;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.IndexRecord;
import speakit.ftrs.index.Indexer;
import speakit.wordreader.WordReader;
import speakit.wordreader.WordReaderImpl;

/**
 * 
 * Se encarga de manejar el control del programa abstrayéndose de la vista.
 * 
 */
public class Speakit implements SpeakitInterface {

	private AudioDictionaryImpl dataBase;
	private Index index;

	/**
	 * Carga Speakit con el conjunto de archivos predeterminado.
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		DictionaryFileSet fileSet = new DictionaryFileSet() {
			File audioFile;
			File audioIndexFile;

			{
				this.audioFile = new File("AudioFile.dat");
				this.audioIndexFile = new File("AudioIndexFile.dat");
				this.audioFile.setWritable(true);
				this.audioIndexFile.setWritable(true);
				this.audioFile.createNewFile();
				this.audioIndexFile.createNewFile();
			}

			@Override
			public File getAudioFile() {
				return this.audioFile;
			}

			@Override
			public File getAudioIndexFile() {
				return this.audioIndexFile;
			}
		};

		this.load(fileSet);
	}

	/**
	 * Carga Speakit con el conjunto de archivos especificado.
	 * 
	 * @throws IOException
	 */
	public void load(DictionaryFileSet fileSet) throws IOException {
		dataBase = new AudioDictionaryImpl();
		dataBase.load(fileSet);
	}

	public Iterable<String> addDocument(TextDocument doc) throws IOException {
		indexDocument(doc);
		ArrayList<String> words = new ArrayList<String>();
		for (String word : doc) {
			if (!this.dataBase.contains(word) && !words.contains(word)) {
				words.add(word);
			}
		}
		return words;
	}

	/**
	 * Procesa el documento para poder recuperarlo mediante el modulo FTRS
	 * @param doc Documento a Indexar
	 */
	private void indexDocument(TextDocument doc) {
		ArrayList<String> terms = doc.getRelevantTerms();
		Indexer indexer = new Indexer();
		indexer.indexTerms(index, terms);
	}

	
	public WordAudioDocument convertToAudioDocument(TextDocument doc) throws IOException { 
		return new WordAudioDocument(this.dataBase,doc); 
	}

	public void addWordAudio(WordAudio audio) throws IOException, RecordSerializationException {
		if (!this.dataBase.contains(audio.getWord())) {
			dataBase.addEntry(audio.getWord(), audio.getAudio());
		}
	}

	private WordReader createWordReader(String path) throws FileNotFoundException, IOException {
		return new WordReaderImpl(new FileInputStream(new File(path)));
	}

	public TextDocument getTextDocumentFromFile(String path) throws FileNotFoundException, IOException {
		TextDocument document = new TextDocument();
		WordReader wordReader = createWordReader(path);
		while (wordReader.hasNext()) {
			document.add(wordReader.next());
		}
		return document;
	}

}
