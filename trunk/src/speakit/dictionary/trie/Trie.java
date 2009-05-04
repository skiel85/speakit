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

public class Trie implements File, RecordFactory<TrieNode> {
	private static final String TRIE_INDEX_DAT = "TrieIndex.dat";
	private long lastRecordNumber;
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
		this.lastRecordNumber = 0;
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

	public long getLastRecordNumber() {
		return lastRecordNumber;
	}

	public void setLastRecordNumber(long lastRecordNumber) {
		this.lastRecordNumber = lastRecordNumber;
	}

	public void addWord(String word, long offset) throws RecordSerializationException, IOException {

		String firstPart = word.substring(0, this.getDepth() - 1);
		String lastPart = "";
		long nodeNumber = 0;
		if (word.length() >= this.getDepth())
			lastPart = word.substring(this.getDepth() - 1);

		// Obtengo el nodo en el que encontre la ultima coincidencia entre la
		// palabra y el trie
		if (this.getNodeFile().contains(new LongField(0)))
			nodeNumber = this.searchTrieNode(this.getNode(0), firstPart, 0);

		if (nodeNumber < firstPart.length()) {
			long j = nodeNumber;
			while (j < this.getDepth() && j < firstPart.length()) {
				String actualChar = firstPart.substring((int) j, (int) j + 1);
				WordOffsetField wordOffsetField=new WordOffsetField(j + 1, actualChar, false);
				ArrayList<WordOffsetField> wordOffsetList=(ArrayList<WordOffsetField>)this.getNode(j).getWordOffsetList();
				wordOffsetList.add(wordOffsetField);
				TrieNode node=this.getNode(j);
				node.setWordOffsetRecordList(wordOffsetList);
				this.nodeFile.updateRecord(node);
				this.getNode(j);
				j++;
			}
			WordOffsetField wordOffsetField=new WordOffsetField(offset, lastPart, true);
			ArrayList<WordOffsetField> wordOffsetList=(ArrayList<WordOffsetField>)this.getNode(j).getWordOffsetList();
			wordOffsetList.add(wordOffsetField);
			//this.getNode(j).setWordOffsetRecordList(wordOffsetList);
			TrieNode node=this.getNode(j);
			node.setWordOffsetRecordList(wordOffsetList);
			this.nodeFile.updateRecord(node);
			//this.setNode(j, node);
			//this.getNode(j).getWordOffsetList().add(new WordOffsetField(offset, lastPart, true));
		} else {
			WordOffsetField wordOffsetField=new WordOffsetField(offset, lastPart, true);
			ArrayList<WordOffsetField> wordOffsetList=(ArrayList<WordOffsetField>)this.getNode(this.getDepth()).getWordOffsetList();
			wordOffsetList.add(wordOffsetField);
			//this.getNode(this.getDepth()).setWordOffsetRecordList(wordOffsetList);
			TrieNode node=this.getNode(this.getDepth());
			node.setWordOffsetRecordList(wordOffsetList);
			this.nodeFile.updateRecord(node);
			//this.setNode(this.getDepth(), node);
			//this.getNode(this.getDepth()).getWordOffsetList().add(new WordOffsetField(offset, lastPart, true));
		}
//		this.getNode(0);
//		this.getNode(1);
//		this.getNode(2);
//		this.getNode(3);

	}

	public boolean contains(String word) throws RecordSerializationException, IOException {
		long nodeNumber = this.searchTrieNode(this.getNode(0), word, 0);
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
			TrieNode trieNode = getNode(0);
			long nodeNumber = this.searchTrieNode(trieNode, word, 0);
			boolean encontrado = false;
			Iterator<WordOffsetField> recordIterator = this.getNode(nodeNumber).getWordOffsetList().iterator();
			WordOffsetField wordOffset = new WordOffsetField(0, "", false);
			while (recordIterator.hasNext() & !encontrado) {
				wordOffset = recordIterator.next();
				if (wordOffset.isLast())
					encontrado = true;
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
	public long searchTrieNode(TrieNode node, String word, int index) throws RecordSerializationException, IOException {

		ArrayList<WordOffsetField> WordOffsetFieldList=(ArrayList<WordOffsetField>)node.getWordOffsetList();
		WordOffsetFieldList.size();
		Iterator<WordOffsetField> nodeIterator = node.getWordOffsetList().iterator();
		while (nodeIterator.hasNext() && index < word.length()) {
			WordOffsetField record = nodeIterator.next();
			if (record.getWord().equals(word.substring(index, index + 1)))
				return searchTrieNode(this.getNode(record.getNextRecord()), word, index + 1);

		}

		return node.getNodeNumber();
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
		while (j < depth) {
			ArrayList<WordOffsetField> wordOffsetList=new ArrayList<WordOffsetField>();
			TrieNode node=new TrieNode(wordOffsetList,j);
			this.nodeFile.insertRecord(node);
			j++;
		}
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
