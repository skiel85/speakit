package speakit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import speakit.dictionary.AudioDictionaryImpl;
import speakit.documentstorage.TextDocumentList;
import speakit.ftrs.FTRS;
import speakit.ftrs.FTRSImpl;
import speakit.io.record.RecordSerializationException;
import speakit.test.TestFileManager;

/**
 * 
 * Se encarga de manejar el control del programa abstrayéndose de la vista.
 * 
 */
public class Speakit implements SpeakitInterface {

	private AudioDictionaryImpl dataBase;
	private FTRS ftrs;
	
	private FileManager fileManager;
	private Configuration conf;

	public Speakit() {
		this.conf = new Configuration();
		this.fileManager = new FileManager();
		this.dataBase = new AudioDictionaryImpl();
		this.ftrs = new FTRSImpl();
	}

	/**
	 * Carga Speakit con el conjunto de archivos predeterminado.
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		this.conf.load(fileManager);
		if (!this.isInstalled(fileManager)) {
			this.install(fileManager, conf);
		}
		this.load(fileManager);
	}

	/**
	 * Carga Speakit con el conjunto de archivos especificado.
	 * 
	 * @throws IOException
	 */
	public void load(FileManager fileManager) throws IOException {
		this.dataBase.load(fileManager, this.conf);
		this.ftrs.load(fileManager, this.conf);
	}

	/**
	 * A partir de un documento, indexa ese documento y devuelve un iterable 
	 * de strings de ese documento
	 * 
	 * @param doc
	 * @return Iterable<String>
	 * 
	 * @throws IOException
	 */
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
	 * A partir de una lista de documentos, indexa esa lista y devuelve un iterable 
	 * de documentos
	 * 
	 * @param docs
	 * @return Iterable<TextDocument>
	 * 
	 * @throws IOException
	 */
	public Iterable<TextDocument> addDocuments(TextDocumentList docs) throws IOException {
		indexDocuments(docs);
		ArrayList<TextDocument>	documents = new ArrayList<TextDocument>();
		Iterator<TextDocument> iterator = docs.iterator();
		TextDocument textDocument;
		while(iterator.hasNext()){
			textDocument = (TextDocument)iterator.next();
			documents.add(textDocument);
		}
		return documents;
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
	}

	private void indexDocuments (TextDocumentList docs)throws IOException, RecordSerializationException {
		this.ftrs.indexDocuments(docs);
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

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		this.ftrs.install(filemanager, conf);
		this.dataBase.install(filemanager, conf);
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return this.ftrs.isInstalled(filemanager) & dataBase.isInstalled(filemanager);
	}

	public void setFileManager(TestFileManager fileManager) {
		this.fileManager = fileManager;
	}

}
