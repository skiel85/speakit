package speakit.dictionary;

import java.io.IOException;

import speakit.Configuration;
import speakit.FileManager;
import speakit.audio.Audio;
import speakit.dictionary.audiofile.AudioFile;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.dictionary.audioindexfile.AudioIndexFile;
import speakit.dictionary.trie.Trie;
import speakit.io.record.RecordSerializationException;

public class AudioDictionaryImpl implements AudioDictionary {
	private static final String AUDIO_FILE_DAT = "AudioFile.dat";
	private static final String TRIE_INDEX_DAT = "TrieIndex.dat";
	private static final String TRIE_NODE_BLOCK_INDEX_DAT = "TrieNodeBlockIndex.dat";
	//private static final String AUDIO_INDEX_FILE_DAT = "AudioIndexFile.dat";
	//private AudioIndexFile audioIndexFile;
	private Trie trie;
	private AudioFile audioFile;

	public AudioDictionaryImpl() {
	}

	@Override
	public void addEntry(String word, Audio audio) throws IOException, RecordSerializationException {
		long offset = this.audioFile.addAudio(audio);
		//this.audioIndexFile.addEntry(word, offset);
		this.trie.addWord(word, offset);
	}

	@Override
	public boolean contains(String word) throws IOException {
		//return audioIndexFile.contains(word);
		return trie.contains(word);
	}

	@Override
	public Audio getAudio(String word) throws IOException, WordNotFoundException {
		//long offset = this.audioIndexFile.getOffset(word);
		long offset = this.trie.getOffset(word);
		return this.audioFile.getAudio(offset);
	}

	@Override
	public void load(FileManager fileManager, Configuration conf) throws IOException {
		//audioIndexFile = new AudioIndexFile(fileManager.openFile(AUDIO_INDEX_FILE_DAT));
		//trie = new Trie();
		//trie.install(fileManager, conf);
		if (trie==null) trie=new Trie();
		
		trie.load(fileManager, conf);
		audioFile = new AudioFile(fileManager.openFile(AUDIO_FILE_DAT));
	}

	@Override
	public void install(FileManager filemanager, Configuration conf) throws IOException {
		trie = new Trie();
		trie.install(filemanager, conf);
		this.load(filemanager, conf);
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		//return filemanager.exists(AUDIO_INDEX_FILE_DAT) & filemanager.exists(AUDIO_FILE_DAT);
		return filemanager.exists(AUDIO_FILE_DAT) && filemanager.exists(TRIE_INDEX_DAT) && filemanager.exists(TRIE_NODE_BLOCK_INDEX_DAT) ;
	}

}
