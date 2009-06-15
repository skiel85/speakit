package speakit.wordreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WordReaderImpl implements WordReader {

	private String[] words;
	private int currentIndex;

	public WordReaderImpl(InputStream inputStream) throws IOException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		char[] buffer = new char[(int) inputStream.available()];
		reader.read(buffer);

		TextCleaner cleaner = new TextCleaner();
		this.words = cleaner.getWords(new String(buffer));
		this.currentIndex = 0;
	}

	@Override
	public boolean hasNext() {
		return this.currentIndex < this.words.length;

	}

	@Override
	public String next() {
		return this.words[this.currentIndex++];
	}

}
