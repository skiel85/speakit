package speakit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import speakit.audio.Audio;
import speakit.dictionary.AudioDictionaryImpl;
import speakit.dictionary.files.audiofile.AudioFile;
import speakit.dictionary.files.audioindexfile.AudioIndexFile;
import speakit.wordreader.WordReader;
import speakit.wordreader.WordReaderImpl;

/**
 * 
 * Se encarga de manejar el control del programa abstrayéndose de la vista.
 * 
 */
public class Speakit {

//	SpeakitObserver observer = null;

	private AudioDictionaryImpl dataBase;

	private AudioFile audioFile;

	private AudioIndexFile audioIndexFile;

	/**
	 * Abre todos los archivos necesarios y deja listo para su uso.
	 * @throws IOException 
	 */
	public void load() throws IOException {
		File file2 = new File("AudioIndexFile.dat");
		File file = new File("AudioFile.dat");
		file.setWritable(true);
		file2.setWritable(true); 
		file.createNewFile();
		file2.createNewFile();
		audioIndexFile = new AudioIndexFile(file2);
		audioFile = new AudioFile(file);
		dataBase = new AudioDictionaryImpl(audioIndexFile, audioFile);
	}

	public Iterable<String> addDocument(TextDocument doc) throws IOException {
		ArrayList<String> words=new ArrayList<String>(); 
		for (String word : doc) {
			if (!this.dataBase.contains(word)) {
				words.add(word);
			}
		}
		return words;
	}

	public WordAudioDocument convertToAudioDocument(TextDocument doc) throws IOException {
		WordAudioDocument audio = new WordAudioDocument();
		for (String word : doc) { 
			if (this.dataBase.contains(word)) {
				audio.add(new WordAudio(word, new Audio(this.dataBase.getAudio(word),0L)));
			}
		}
		return audio;
	}

	public void addWordAudio(WordAudio audio) throws IOException {
		if (!this.dataBase.contains(audio.getWord())) {			
			dataBase.addEntry(audio.getWord(), audio.getAudio().getBytes());
		}
	} 
		
	private WordReader createWordReader(String path)
			throws FileNotFoundException, IOException {
		return new WordReaderImpl(new FileInputStream(new File(path)));
	}

	public TextDocument getTextDocumentFromFile(String path) throws FileNotFoundException, IOException {
		TextDocument document=new TextDocument();
		WordReader wordReader = createWordReader(path);
		while (wordReader.hasNext()) {
			document.add(wordReader.next());			 
		}		
		return document;
	}

}
