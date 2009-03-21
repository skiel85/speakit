package speakit.dictionary;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DictionaryImpl implements Dictionary {

	@Override
	public void addEntry(String word, byte[] audio) {
		throw new NotImplementedException();
	}

	@Override
	public boolean contains(String word) {
		throw new NotImplementedException();
	}
	
	@Override
	public byte[] getAudio(String word) {
		throw new NotImplementedException();
	}

}
