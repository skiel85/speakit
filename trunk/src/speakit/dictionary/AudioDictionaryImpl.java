package speakit.dictionary;

import java.io.IOException;

import speakit.audio.Audio;
import speakit.dictionary.files.audiofile.AudioFile;
import speakit.dictionary.files.audioindexfile.AudioIndexFile;

public class AudioDictionaryImpl implements AudioDictionary {
	private AudioIndexFile audioIndexFile;
	private AudioFile audioFile;

	public AudioDictionaryImpl(AudioIndexFile audioIndexFile, AudioFile audioFile) {
		this.audioIndexFile = audioIndexFile;
		this.audioFile = audioFile;
	}

	@Override
	public void addEntry(String word, Audio audio) throws IOException {
		long offset = this.audioFile.addAudio(audio);
		this.audioIndexFile.addEntry(word, offset);
	}

	@Override
	public boolean contains(String word) throws IOException {
		return audioIndexFile.contains(word);
	}

	@Override
	public Audio getAudio(String word) throws IOException {
		long offset = this.audioIndexFile.getOffset(word);
		return this.audioFile.getAudio(offset);
	}

}
