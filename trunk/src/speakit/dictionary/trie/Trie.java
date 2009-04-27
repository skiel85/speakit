package speakit.dictionary.trie;

import java.util.ArrayList;


public class Trie {
	long lastRecordNumber;
	ArrayList<TrieRecord> TrieRecordFile;
	int depth;
	
	public Trie() {
		
		TrieRecordFile = new ArrayList<TrieRecord>();
		this.depth = 4;
		this.lastRecordNumber = 0;
	}
	
	public Trie(int customDepth) {
		
		TrieRecordFile = new ArrayList<TrieRecord>();
		this.depth = customDepth;
		this.lastRecordNumber = 0;
	}

	public ArrayList<TrieRecord> getTrieRecordFile() {
		return TrieRecordFile;
	}
	
	public void setTrieRecordFile(ArrayList<TrieRecord> trieRecordFile) {
		TrieRecordFile = trieRecordFile;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public long getLastRecordNumber() {
		return lastRecordNumber;
	}

	public void setLastRecordNumber(long lastRecordNumber) {
		this.lastRecordNumber = lastRecordNumber;
	}

	public void addWord(String word, long offset){
		
		boolean foundRecord=false;
		long recordNumber=0;
		int i=0;
		
		word=word+"/n";
		
		/** TODO Implementar que recorra hasta depth y después busque el resto de la palabra **/
		while (i<word.length() || !foundRecord){
			long newRecordNumber=searchWordRecordNumber(word.substring(i, i+1),recordNumber);
			if (newRecordNumber==recordNumber) foundRecord=true;
			recordNumber=newRecordNumber;
			i++;
		}
		
		if (foundRecord){
			for (int j=i; j<word.length();j++){
				/** TODO Agregar el resto de las letras que faltan y, en el ultimo, agregar el offset 
				 * al archivo de audio**/				
			}
		} 
		
		
	}
	
	public long searchWordRecordNumber(String word, long recordNumber){
		
		String wordFromRecordFile = this.getTrieRecordFile().get((int) recordNumber).getWord();
		
		if (wordFromRecordFile.indexOf(word)==-1) return recordNumber;
		else return this.getTrieRecordFile().get((int) recordNumber).getNextRecord();
		
	}
	
	public boolean contains (String word){
		
		word=word+"/n";
		boolean contains=true;
		long recordNumber=0;
		int i=0;
		
		/** TODO Implementar que recorra hasta depth y después busque el resto de la palabra **/
		while (i<word.length() || contains){
			long newRecordNumber=searchWordRecordNumber(word.substring(i, i+1),recordNumber);
			if (newRecordNumber==recordNumber) contains=false;
			recordNumber=newRecordNumber;
			i++;
		}
		
		return contains;
		
	}

}
