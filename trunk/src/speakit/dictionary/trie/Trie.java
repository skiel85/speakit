package speakit.dictionary.trie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import speakit.Configuration;
import speakit.FileManager;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.io.File;
import speakit.io.record.LongField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.DirectRecordFile;

public class Trie implements File, RecordFactory{
	private static final String TRIE_INDEX_DAT = "TrieIndex.dat";
	private long lastNodeNumber;
	// private ArrayList<TrieNode> TrieNodeList;
	private int depth;
	private DirectRecordFile<TrieNode, LongField> nodeFile;

	public DirectRecordFile<TrieNode, LongField> getNodeFile() {
		return nodeFile;
	}

	public void setNodeFile(DirectRecordFile<TrieNode, LongField> nodeFile) {
		this.nodeFile = nodeFile;
	}

	public Trie() {

		// TrieNodeList = new ArrayList<TrieNode>();
		// this.depth = 4;
		this.lastNodeNumber = 1;
	}

	// public ArrayList<TrieNode> getTrieNodeList() {
	// return nodeFile;
	// }
	//
	// public void setTrieNodeList(ArrayList<TrieNode> trieNodeList) {
	// nodeFile = trieNodeList;
	// }

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public long getLastNodeNumber() {
		return lastNodeNumber;
	}

	public void setLastNodeNumber(long lastNodeNumber) {
		this.lastNodeNumber = lastNodeNumber;
	}

	public void addWord(String word, long offset) throws RecordSerializationException, IOException {

		String firstPart = word.substring(0, this.getDepth() - 1);
		String lastPart = "";
		
		if (word.length() >= this.getDepth())
			lastPart = word.substring(this.getDepth() - 1);
		
		this.addWordToNode(this.getNode(0), firstPart, lastPart, 0, offset);
		
		TrieNode nodo0=this.getNode(0);
		TrieNode nodo1=this.getNode(1);
		TrieNode nodo2=this.getNode(2);
		TrieNode nodo3=this.getNode(3);
		TrieNode nodo4=this.getNode(4);
		TrieNode nodo5=this.getNode(5);
		

	}

	public boolean contains(String word) throws RecordSerializationException, IOException {
		
		String firstPart = word.substring(0, this.getDepth() - 1);
		String lastPart = "";
		
		if (word.length() >= this.getDepth())
			lastPart = word.substring(this.getDepth() - 1);
		
		long nodeNumber = this.searchTrieNode(this.getNode(0), firstPart, lastPart, 0);
		
		Iterator<WordOffsetField> recordIterator = this.getNode(nodeNumber).getWordOffsetList().iterator();
		while (recordIterator.hasNext()) {
			if (recordIterator.next().isLast())
				return true;
		}
		
		return false;
	}

	public long getOffset(String word) throws WordNotFoundException, RecordSerializationException, IOException {
		if (!this.contains(word))
			throw new WordNotFoundException(word);
		else {
			String firstPart = word.substring(0, this.getDepth() - 1);
			String lastPart = "";
			
			if (word.length() >= this.getDepth())
				lastPart = word.substring(this.getDepth() - 1);
			
			TrieNode trieNode = getNode(0);
			long nodeNumber = this.searchTrieNode(trieNode, firstPart, lastPart, 0);
			boolean found = false;
			Iterator<WordOffsetField> recordIterator = this.getNode(nodeNumber).getWordOffsetList().iterator();
			WordOffsetField wordOffset = new WordOffsetField(0, "", false);
			while (recordIterator.hasNext() & !found) {
				wordOffset = recordIterator.next();
				if (wordOffset.isLast())
					found = true;
			}
			return wordOffset.getNextRecord();

		}
	}

	private TrieNode getNode(long nodeNumber) throws IOException, RecordSerializationException {
		/**
		 * acá en lugar de utilizar el metodo getRecord( key) utilizar un indice
		 * para saber el numero de bloque del nodo nodeNumber y luego llamar a
		 * getRecord( key,blockNumber)
		 */
		return this.nodeFile.getRecord(new LongField(nodeNumber));
		
	}
	
	private void setNode(long nodeNumber, TrieNode node) throws RecordSerializationException, IOException{
		
		this.nodeFile.insertRecord(node, 0);
		
	}

	/**
	 * Función recursiva que recorre los nodos y chequea si cada letra de la
	 * palabra esta en un nodo y devuelve el indice del ultimo nodo recorrido
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 **/
	public long searchTrieNode(TrieNode node, String firstPart,String lastPart, int indexFirstPart) throws RecordSerializationException, IOException {

		Iterator<WordOffsetField> nodeIterator = node.getWordOffsetList().iterator();
		boolean foundString=false;
		
		
		while (nodeIterator.hasNext() && !foundString){
			WordOffsetField record = nodeIterator.next();
			
			if (indexFirstPart<firstPart.length() && record.getWord().equals(firstPart.substring(indexFirstPart, indexFirstPart+1))){
				foundString=true;
				searchTrieNode(this.getNode(record.getNextRecord()),firstPart, lastPart,indexFirstPart+1);
				 
				} else {
				if(record.getWord().equals(lastPart)){
					foundString=true;
					return record.getNextRecord();
				}
			}
		}
		
		return node.getNodeNumber();
	}
	
	private void incrementLastNodeNumber(){
		this.lastNodeNumber++;
	}
	
	private void addWordToNode(TrieNode initialNode, String firstPart, String lastPart, int indexFirstPart, long offset) throws RecordSerializationException, IOException{
		
		Iterator<WordOffsetField> initialNodeIterator = initialNode.getWordOffsetList().iterator();
		boolean foundString=false;
		while (initialNodeIterator.hasNext() && !foundString){
			WordOffsetField record = initialNodeIterator.next();
			
			if (indexFirstPart<firstPart.length() && record.getWord().equals(firstPart.substring(indexFirstPart, indexFirstPart+1))){
				addWordToNode(this.getNode(record.getNextRecord()),firstPart,lastPart,indexFirstPart+1, offset);
				foundString=true;
			} else {
				if(!(indexFirstPart<firstPart.length()) && record.getWord().equals(lastPart))
					foundString=true;
			}
		}
		int i=indexFirstPart;
		if (!foundString){
			if (i<firstPart.length()){
				addFirstPartToNodes(initialNode, firstPart, indexFirstPart, i);
			}
			addLastPartToNode(lastPart, offset);
			
		}
	}

	/**
	 * @param lastPart
	 * @param offset 
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	private void addLastPartToNode(String lastPart, long offset) throws IOException,
			RecordSerializationException {
		// Agrego la ultima parte de la palabra a un nuevo nodo
		
		ArrayList<WordOffsetField> newWordOffsetList=new ArrayList<WordOffsetField>();
		WordOffsetField newWordOffsetField=new WordOffsetField(offset, lastPart, true);
		newWordOffsetList.add(newWordOffsetField);
		TrieNode newNode=new TrieNode(newWordOffsetList,this.getLastNodeNumber()-1);
		this.nodeFile.insertRecord(newNode);
		//incrementLastNodeNumber();
	}

	/**
	 * @param initialNode
	 * @param firstPart
	 * @param indexFirstPart
	 * @param i
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private void addFirstPartToNodes(TrieNode initialNode, String firstPart,
			int indexFirstPart, int i) throws RecordSerializationException,
			IOException {
		// Agrego la primer letra del resto de la primer parte de la palabra al nodo actual
		//this.incrementLastNodeNumber();
		String actualChar = firstPart.substring(indexFirstPart, indexFirstPart+1);
		WordOffsetField wordOffsetField=new WordOffsetField(this.getLastNodeNumber(), actualChar, false);
		ArrayList<WordOffsetField> wordOffsetList=(ArrayList<WordOffsetField>) initialNode.getWordOffsetList();
		wordOffsetList.add(wordOffsetField);
		initialNode.setWordOffsetRecordList(wordOffsetList);
		this.nodeFile.updateRecord(initialNode);
		i++;
		this.incrementLastNodeNumber();
		
		// Agrego el resto de la primer parte de la palabra a nuevos nodos
		while(i<firstPart.length()){
			//this.incrementLastNodeNumber();
			ArrayList<WordOffsetField> newWordOffsetList=new ArrayList<WordOffsetField>();
			String newActualChar = firstPart.substring((int) i, (int) i + 1);
			WordOffsetField newWordOffsetField=new WordOffsetField(this.getLastNodeNumber(), newActualChar, false);
			newWordOffsetList.add(newWordOffsetField);
			TrieNode newNode=new TrieNode(newWordOffsetList,this.getLastNodeNumber()-1);
			this.nodeFile.insertRecord(newNode);
			this.incrementLastNodeNumber();
			i++;
		}
	}

	private void createDataFile(FileManager fileManager) throws IOException {
		nodeFile = new DirectRecordFile<TrieNode, LongField>(fileManager.openFile(TRIE_INDEX_DAT), this);
	}

	@Override
	public void load(FileManager fileManager, Configuration conf) throws IOException {
		this.depth = conf.getTrieDepth();
		createDataFile(fileManager);
		nodeFile.load();
	}

	@Override
	public void install(FileManager fileManager, Configuration conf) throws IOException {
		createDataFile(fileManager);
		this.depth = conf.getTrieDepth();
		nodeFile.create(conf.getBlockSize());
		int j = 0;
		//while (j < depth) {
			ArrayList<WordOffsetField> wordOffsetList=new ArrayList<WordOffsetField>();
			TrieNode node=new TrieNode(wordOffsetList,j);
			this.nodeFile.insertRecord(node);
			//j++;
		//}
	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return filemanager.exists(TRIE_INDEX_DAT);
	}

	@Override
	public TrieNode createRecord() {
		return new TrieNode();
	}

}
