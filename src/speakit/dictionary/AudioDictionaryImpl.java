package speakit.dictionary;

import java.io.File;
import java.io.IOException;

import speakit.audio.Audio;
import speakit.dictionary.audiofile.AudioFile;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.dictionary.audioindexfile.AudioIndexFile;
import speakit.io.RecordSerializationException;

public class AudioDictionaryImpl implements AudioDictionary {
	private AudioIndexFile audioIndexFile;
	private AudioFile audioFile;

	public AudioDictionaryImpl() {
	}

	@Override
	public void addEntry(String word, Audio audio) throws IOException, RecordSerializationException {
		long offset = this.audioFile.addAudio(audio);
		this.audioIndexFile.addEntry(word, offset);
	}

	@Override
	public boolean contains(String word) throws IOException {
		return audioIndexFile.contains(word);
	}

	@Override
	public Audio getAudio(String word) throws IOException, WordNotFoundException {
		long offset = this.audioIndexFile.getOffset(word);
		return this.audioFile.getAudio(offset);
	}

	@Override
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

		audioIndexFile = new AudioIndexFile(fileSet.getAudioIndexFile());
		audioFile = new AudioFile(fileSet.getAudioFile());
	}

	@Override
	public void load(DictionaryFileSet fileSet) throws IOException {
		audioIndexFile = new AudioIndexFile(fileSet.getAudioIndexFile());
		audioFile = new AudioFile(fileSet.getAudioFile());
	}

}
