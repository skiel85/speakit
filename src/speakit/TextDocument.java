package speakit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import speakit.compression.arithmetic.Symbol;
import speakit.wordreader.WordReader;
import speakit.wordreader.WordReaderImpl;

public class TextDocument implements Iterable<String> {

	private String text;
	private WordReader wordReader;
	private long id;
	private boolean filterRepeated;

	public TextDocument(long id, File file, boolean filterRepeated) throws IOException {
		this(id, "", filterRepeated);
		loadFromFile(file);
	}
	
	public TextDocument(long id, String text, boolean filterRepeated) {
		this.filterRepeated = filterRepeated;
		this.id = id;
		this.text = text;
		wordReader = new WordReaderImpl(this.text);
	}

	public TextDocument(long id, String text) {
		this(id, text, false);
	}

	public TextDocument(String text) {
		this(0, text, false);
	}

	public TextDocument() {
		this("");
	}

	public void loadFromFile(File file) throws IOException {
		FileInputStream in = new FileInputStream(file); 
		InputStreamReader reader = new InputStreamReader(in,"UTF-16");  //el notepad++ marca a estos archivos como ucs-2 big endian
		String readString = new String();
		int current=0;		
		while((current=reader.read())>=0){
			readString+=new Symbol((char)current).getChar();
		}
		this.text=readString;
	}

	@Override
	public Iterator<String> iterator() {
		wordReader = new WordReaderImpl(this.text);
		List<String> words = new ArrayList<String>();
		if (wordReader.hasNext()) {
			while (wordReader.hasNext()) {
				String word = wordReader.next();
				if (!this.filterRepeated || !words.contains(word)) {
					words.add(word);
				}
			}
		}
		return words.iterator();
	}

	/*
	 * Para implementar este método es necesario tener como mínimo la lista de
	 * stop words. Esto se implementa en FTRS public ArrayList<String>
	 * getRelevantTerms() { return words; }
	 */

	public String getPreview() {
		if (this.text.length() > 100) {
			return this.text.substring(0, 100);
		} else {
			return this.text + "....";
		}
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}
	
	
	/**
	 * Compara por id y por texto
	 */
	@Override
	public boolean equals(Object obj) {
		TextDocument other = (TextDocument) obj;
		if (this.id != other.id) {
			return false;
		} else {
			return this.text.compareTo(other.text) == 0;
		}
	}
}
