package speakit;

import java.util.ArrayList;
import java.util.Iterator;

public class WordAudioDocument implements Iterable<WordAudio> {

	private ArrayList<WordAudio> wordAudioList=new ArrayList<WordAudio>(); 

	public void add(WordAudio wordAudio) { 
		this.wordAudioList.add(wordAudio);
	}

	@Override
	public Iterator<WordAudio> iterator() {
		return wordAudioList.iterator();
	}

}
