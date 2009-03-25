package speakit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

	SpeakitObserver observer = null;

	private AudioDictionaryImpl dataBase;

	private AudioFile audioFile;

	private AudioIndexFile audioIndexFile;

	/**
	 * Abre todos los archivos necesarios y deja listo para su uso.
	 */
	public void launch() {
		File file2 = new File("AudioIndexFile.dat");
		File file = new File("AudioFile.dat");
		file.setWritable(true);
		file2.setWritable(true);
		try {
			file.createNewFile();
			file2.createNewFile();
			audioIndexFile = new AudioIndexFile(file2);
			audioFile = new AudioFile(file);
			dataBase = new AudioDictionaryImpl(audioIndexFile, audioFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		observer.start();
	}

	public void processFile(String path)
			throws CannotProccessFileSpeakitException {
		try {
			WordReader wordReader = createWordReader(path);
			while (wordReader.hasNext()) {
				String word = wordReader.next();
				if(!this.dataBase.contains(word)){
					byte[] audioWord = this.observer.getAudio(word);
					if(audioWord!=null){
						dataBase.addEntry(word, audioWord);	
					}	
				}else{
					this.observer.notifyAlreadyHaveIt(word);
				}
			}
			observer.start();
		} catch (IOException io) {
			throw new CannotProccessFileSpeakitException();
		}
	}

	public SpeakitObserver getObserver() {
		return observer;
	}

	public void setObserver(SpeakitObserver observer) {
		this.observer = observer;
	}

	public void readFile(String path) throws CannotProccessFileSpeakitException {

		try {
			WordReader aWordReader = createWordReader(path);

			while (aWordReader.hasNext()) {
				String word = aWordReader.next();
				if (this.dataBase.contains(word)) {
					byte[] audioWord = this.dataBase.getAudio(word);
					System.out.println("Palabra encontrada: " + word);
					this.observer.playSound(audioWord);
				}else{
					System.out.println("Palabra NOOO encontrada: " + word);
				}
			}
			observer.start();
		} catch (FileNotFoundException e) {
			throw new CannotProccessFileSpeakitException();
		} catch (IOException e) {
			throw new CannotProccessFileSpeakitException();
		}

	}

	private WordReader createWordReader(String path) throws FileNotFoundException,
			IOException {
		return new WordReaderImpl(new FileInputStream(new File(path)));
	}

}
