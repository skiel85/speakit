package speakit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import speakit.audio.Audio;
import speakit.dictionary.AudioDictionary;
import speakit.dictionary.audiofile.WordNotFoundException;

public class WordAudioDocument implements Iterator<WordAudio> {

	private ArrayList<WordAudio> wordAudioList = new ArrayList<WordAudio>();
	private TextDocument textDocument;
	private Iterator<String> iterator;
	private final AudioDictionary dictionary;

	public void add(WordAudio wordAudio) {
		this.wordAudioList.add(wordAudio);
	}

	public WordAudioDocument(AudioDictionary dictionary, TextDocument textDocument) {
		this.dictionary = dictionary;
		this.textDocument = textDocument;
		iterator = this.textDocument.iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public WordAudio next() {
		if (this.hasNext()) {
			String currentWord = this.iterator.next();
			try {
				return new WordAudio(currentWord, this.dictionary.getAudio(currentWord));
			} catch (WordNotFoundException ex) {

			} catch (IOException e) {

			}
			return new WordAudio("[" + currentWord + "]", new Audio(new byte[] {}));
		}
		return new WordAudio("X", new Audio(new byte[] {}));

	}

	@Override
	public void remove() {
	}

}
