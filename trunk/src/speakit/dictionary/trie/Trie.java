package speakit.dictionary.trie;

import java.util.ArrayList;
import java.util.Iterator;

import speakit.dictionary.audiofile.WordNotFoundException;


public class Trie {
	private long lastRecordNumber;
	//ArrayList<TrieRecord> TrieRecordFile;
	private ArrayList<TrieNode> TrieNodeList;
	private int depth;
	
	public Trie() {
		
		//TrieRecordFile = new ArrayList<TrieRecord>();
		TrieNodeList = new ArrayList<TrieNode>();
		this.depth = 4;
		this.lastRecordNumber = 0;
		
		int j=0;
		while(j<depth){
			TrieNodeList.add(new TrieNode(j));
			j++;
		}
	}
	
	public ArrayList<TrieNode> getTrieNodeList() {
		return TrieNodeList;
	}

	public void setTrieNodeList(ArrayList<TrieNode> trieNodeList) {
		TrieNodeList = trieNodeList;
	}

	public Trie(int customDepth) {
		
		//TrieRecordFile = new ArrayList<TrieRecord>();
		TrieNodeList = new ArrayList<TrieNode>();
		this.depth = customDepth;
		this.lastRecordNumber = 0;
	}

	/*public ArrayList<TrieRecord> getTrieRecordFile() {
		return TrieRecordFile;
	}
	
	public void setTrieRecordFile(ArrayList<TrieRecord> trieRecordFile) {
		TrieRecordFile = trieRecordFile;
	}*/
	
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
		
		String firstPart=word.substring(0, this.getDepth()-1);
		String lastPart="";
		long nodeNumber=0;
		if(word.length()>this.getDepth()) lastPart=word.substring(this.getDepth()-1);
		
		//Obtengo el nodo en el que encontre la ultima coincidencia entre la palabra y el trie
		if (this.getTrieNodeList().size()>0) nodeNumber=this.searchTrieNode(this.getTrieNodeList().get(0), firstPart, 0);
		
		if (nodeNumber<firstPart.length()){
			long j=nodeNumber;
			while(j<this.getDepth() && j<firstPart.length()){
				String actualChar=firstPart.substring((int)j,(int) j+1);
				this.getTrieNodeList().get((int)j).getWordOffsetRecordList().add(new WordOffsetRecord(j+1,actualChar,false));
				j++;
			}
			this.getTrieNodeList().get((int)j).getWordOffsetRecordList().add(new WordOffsetRecord(offset,lastPart,true));
		} else {
			this.getTrieNodeList().get(this.getDepth()).getWordOffsetRecordList().add(new WordOffsetRecord(offset,lastPart,true));	
		}
		
		
		
	}

	public boolean contains(String word){
		long nodeNumber=this.searchTrieNode(this.getTrieNodeList().get(0), word, 0);
		Iterator <WordOffsetRecord> recordIterator=this.getTrieNodeList().get((int)nodeNumber).getWordOffsetRecordList().iterator();
		while(recordIterator.hasNext()){
			if (recordIterator.next().isLast()) return true;
		}
		return false;
	}
	
	public long getOffset(String word) throws WordNotFoundException{
		if (!this.contains(word)) throw new WordNotFoundException(word);
		else{
			long nodeNumber=this.searchTrieNode(this.getTrieNodeList().get(0), word, 0);
			boolean encontrado=false;
			Iterator <WordOffsetRecord> recordIterator=this.getTrieNodeList().get((int)nodeNumber).getWordOffsetRecordList().iterator();
			WordOffsetRecord record = new WordOffsetRecord(0,"",false);
			while (recordIterator.hasNext() & !encontrado){
				record=recordIterator.next();
				if (record.isLast()) encontrado=true;
			}
			return record.getNextRecord();
			
		}
	}
	
	
	/**
	  Función recursiva que recorre los nodos y chequea si cada letra de la palabra esta en un nodo y devuelve el 
	  indice del ultimo nodo recorrido
	 **/
	public long searchTrieNode(TrieNode node, String word, int index){
		
		Iterator<WordOffsetRecord> nodeIterator=node.getWordOffsetRecordList().iterator();
		while (nodeIterator.hasNext() && index<word.length()){
			WordOffsetRecord record=(WordOffsetRecord)nodeIterator.next();
			if(record.getWord().equals(word.substring(index, index+1))) 
				return searchTrieNode(this.getTrieNodeList().get((int) record.getNextRecord()), word, index+1);
			
		}
		
		return node.getNodeNumber();
	}

}
