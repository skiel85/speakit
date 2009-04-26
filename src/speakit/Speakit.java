package speakit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import speakit.dictionary.AudioDictionaryImpl;
import speakit.dictionary.DictionaryFileSet;
import speakit.documentstorage.DocumentRepository;
import speakit.documentstorage.TextDocumentList;
import speakit.ftrs.FTRS;
import speakit.ftrs.FTRSImpl;
import speakit.io.record.RecordSerializationException;

/**
 * 
 * Se encarga de manejar el control del programa abstrayéndose de la vista.
 * 
 */
public class Speakit implements SpeakitInterface {

	private AudioDictionaryImpl dataBase;
	private FTRS ftrs = new FTRSImpl();
	private DocumentRepository repository;

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

		repository = new DocumentRepository();
		repository.load(createFile("DocumentRepository.dat"));
	}

	private File createFile(String path) throws IOException {
		File file = new File(path);
		file.setWritable(true);
		file.createNewFile();
		return file;
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
	 * 
	 * @param doc
	 *            Documento a Indexar
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private void indexDocument(TextDocument doc) throws IOException, RecordSerializationException {
		this.ftrs.indexDocuments(doc);
		this.repository.store(doc);
	}

	public WordAudioDocument convertToAudioDocument(TextDocument doc) throws IOException {
		return new WordAudioDocument(this.dataBase, doc);
	}

	public void addWordAudio(WordAudio audio) throws IOException, RecordSerializationException {
		if (!this.dataBase.contains(audio.getWord())) {
			dataBase.addEntry(audio.getWord(), audio.getAudio());
		}
	}

	public TextDocument getTextDocumentFromFile(String path) throws FileNotFoundException, IOException {
		TextDocument document = new TextDocument();
		document.loadFromFile(new File(path));
		return document;
	}

	@Override
	public TextDocumentList search(TextDocument searchText) throws IOException {
		return this.ftrs.search(searchText);
	}

}
