package speakit.dictionary;

import java.io.IOException;

import speakit.Configuration;
import speakit.FileManager;
import speakit.audio.Audio;
import speakit.dictionary.audiofile.AudioFile;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.dictionary.audioindexfile.AudioIndexFile;
import speakit.io.record.RecordSerializationException;

public class AudioDictionaryImpl implements AudioDictionary {
	private static final String	AUDIO_FILE_DAT			= "AudioFile.dat";
	private static final String	AUDIO_INDEX_FILE_DAT	= "AudioIndexFile.dat";
	private AudioIndexFile		audioIndexFile;
	private AudioFile			audioFile;

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
	public void load(FileManager fileManager) throws IOException {
		audioIndexFile = new AudioIndexFile(fileManager.openFile(AUDIO_INDEX_FILE_DAT));
		audioFile = new AudioFile(fileManager.openFile(AUDIO_FILE_DAT));
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		this.load(filemanager);
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return filemanager.exists(AUDIO_INDEX_FILE_DAT) & filemanager.exists(AUDIO_FILE_DAT);
	}

}
