package speakit.dictionary.trie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import speakit.Configuration;
import speakit.FileManager;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.io.File;
import speakit.io.record.IntegerField;
import speakit.io.record.LongField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.DirectRecordFile;

public class Trie implements File, RecordFactory {
	private static final String						TRIE_INDEX_DAT				= "TrieIndex.dat";
	private static final String						TRIE_NODE_BLOCK_INDEX_DAT	= "TrieNodeBlockIndex.dat";
	private long									nextNodeNumber;

	private int										depth;
	private DirectRecordFile<TrieNode, LongField>	nodeFile;
	private TrieNodeBlockIndexFile					nodeBlockIndexFile;

	public DirectRecordFile<TrieNode, LongField> getNodeFile() {
		return nodeFile;
	}

	public void setNodeFile(DirectRecordFile<TrieNode, LongField> nodeFile) {
		this.nodeFile = nodeFile;
	}

	public Trie() {
		this.nextNodeNumber = 1;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public long getNextNodeNumber() {
		return nextNodeNumber;
	}

	public void setNextNodeNumber(long nextNodeNumber) {
		this.nextNodeNumber = nextNodeNumber;
	}

	public void addWord(String word, long offset) throws RecordSerializationException, IOException {

		String firstPart = "";
		String lastPart = "";
		if (word.length() < this.getDepth())
			word = word + " ";

		if (word.length() < this.getDepth() - 1) {
			firstPart = word.substring(0);
		} else {
			firstPart = word.substring(0, this.getDepth() - 1);
		}

		if (word.length() >= this.getDepth())
			lastPart = word.substring(this.getDepth() - 1);

		this.addWordToNode(this.getNode(0), firstPart, lastPart, 0, offset);
		//		
		// TrieNode nodo0=this.getNode(0);
		// TrieNode nodo1=this.getNode(1);
		// TrieNode nodo2=this.getNode(2);
		// TrieNode nodo3=this.getNode(3);
		// TrieNode nodo4=this.getNode(4);
		// TrieNode nodo5=this.getNode(5);
		// TrieNode nodo6=this.getNode(6);
		// TrieNode nodo7=this.getNode(7);
		// TrieNode nodo8=this.getNode(8);
		// TrieNode nodo9=this.getNode(9);
		// TrieNode nodo10=this.getNode(10);
		// TrieNode nodo11=this.getNode(11);
		// TrieNode nodo12=this.getNode(12);
		// TrieNode nodo13=this.getNode(13);

	}

	public boolean contains(String word) throws RecordSerializationException, IOException {

		String firstPart = "";
		String lastPart = "";
		if (word.length() < this.getDepth())
			word = word + " ";

		if (word.length() < this.getDepth() - 1) {
			firstPart = word.substring(0);
		} else {
			firstPart = word.substring(0, this.getDepth() - 1);
		}

		if (word.length() >= this.getDepth())
			lastPart = word.substring(this.getDepth() - 1);

		long nodeNumber = this.searchTrieNode(this.getNode(0), firstPart, lastPart, 0);

		Iterator<WordOffsetField> recordIterator = this.getNode(nodeNumber).getWordOffsetList().iterator();
		while (recordIterator.hasNext()) {
			WordOffsetField wordOffset = recordIterator.next();
			if (wordOffset.isLast() && (wordOffset.getWord().equals(lastPart) || wordOffset.getWord().equals(" ")))
				return true;
		}

		return false;
	}

	public long getOffset(String word) throws WordNotFoundException, RecordSerializationException, IOException {
		String firstPart = "";
		String lastPart = "";
		if (word.length() < this.getDepth())
			word = word + " ";

		if (word.length() < this.getDepth() - 1) {
			firstPart = word.substring(0);
		} else {
			firstPart = word.substring(0, this.getDepth() - 1);
		}

		if (word.length() >= this.getDepth())
			lastPart = word.substring(this.getDepth() - 1);

		long nodeNumber = this.searchTrieNode(this.getNode(0), firstPart, lastPart, 0);

		boolean offsetFound = false;
		long offset = 0;
		Iterator<WordOffsetField> recordIterator = this.getNode(nodeNumber).getWordOffsetList().iterator();
		while (recordIterator.hasNext()) {
			WordOffsetField wordOffset = recordIterator.next();
			if (wordOffset.isLast() && (wordOffset.getWord().equals(lastPart) || wordOffset.getWord().equals(" "))) {
				offsetFound = true;
				offset = wordOffset.getNextRecord();

			}

		}
		if (!offsetFound)
			throw new WordNotFoundException(word);

		return offset;

	}

	private TrieNode getNode(long nodeNumber) throws IOException, RecordSerializationException {
		/**
		 * acá en lugar de utilizar el metodo getRecord( key) utilizar un indice
		 * para saber el numero de bloque del nodo nodeNumber y luego llamar a
		 * getRecord( key,blockNumber)
		 */
		// this.nodeBlockIndexFile.getNodeBlockIndexFile().getRecord(new
		// IntegerField(0));
		TrieNodeBlockIndex trieBlockIndex = this.nodeBlockIndexFile.getNodeBlockIndexFile().getRecord(new IntegerField((int) nodeNumber));
		if (!(trieBlockIndex == null)) {
			int blockNumber = trieBlockIndex.getBlockNumber();
			return this.nodeFile.getRecord(new LongField(nodeNumber), blockNumber);
		} else {
			return null;
		}

	}
	/**
	 * Función recursiva que recorre los nodos y chequea si cada letra de la
	 * palabra esta en un nodo y devuelve el indice del ultimo nodo recorrido
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 **/
	public long searchTrieNode(TrieNode node, String firstPart, String lastPart, int indexFirstPart) throws RecordSerializationException, IOException {

		Iterator<WordOffsetField> wordOffsetListIterator = node.getWordOffsetList().iterator();
		boolean foundString = false;
		long nodeFound = 0;

		while (wordOffsetListIterator.hasNext() && !foundString) {
			WordOffsetField wordOffset = wordOffsetListIterator.next();

			if (indexFirstPart < firstPart.length()) {
				String actualChar = "";

				if (indexFirstPart == (firstPart.length() - 1) && firstPart.length() < this.getDepth()) {
					actualChar = firstPart.substring(indexFirstPart);
				} else {
					actualChar = firstPart.substring(indexFirstPart, indexFirstPart + 1);
				}
				if (wordOffset.getWord().equals(actualChar)) {
					foundString = true;
					if (wordOffset.isLast()) {
						nodeFound = node.getNodeNumber();
					} else {
						nodeFound = searchTrieNode(this.getNode(wordOffset.getNextRecord()), firstPart, lastPart, indexFirstPart + 1);
					}
				}

			} else {
				if (wordOffset.getWord().equals(lastPart)) {
					foundString = true;
					nodeFound = node.getNodeNumber();
				}

			}

		}

		return nodeFound;
	}

	private void incrementNextNodeNumber() {
		this.nextNodeNumber++;
	}

	private void addWordToNode(TrieNode initialNode, String firstPart, String lastPart, int indexFirstPart, long offset) throws RecordSerializationException, IOException {

		Iterator<WordOffsetField> initialNodeIterator = initialNode.getWordOffsetList().iterator();
		boolean foundString = false;
		while (initialNodeIterator.hasNext() && !foundString) {
			WordOffsetField record = initialNodeIterator.next();
			String actualChar = "";
			if ((indexFirstPart == (firstPart.length() - 1) && firstPart.length() < this.getDepth()) || (indexFirstPart == this.getDepth() - 1)) {
				actualChar = firstPart.substring(indexFirstPart);
			} else {
				actualChar = firstPart.substring(indexFirstPart, indexFirstPart + 1);
			}

			if (indexFirstPart < firstPart.length() && record.getWord().equals(actualChar)) {
				addWordToNode(this.getNode(record.getNextRecord()), firstPart, lastPart, indexFirstPart + 1, offset);
				foundString = true;
			} else {
				if (!(indexFirstPart < firstPart.length()) && record.getWord().equals(lastPart))
					foundString = true;
			}
		}
		int i = indexFirstPart;
		if (!foundString) {
			if (i < firstPart.length()) {
				addFirstPartToNodes(initialNode, firstPart, indexFirstPart, i, offset);
			}
			if (!lastPart.equals(""))
				addLastPartToNode(initialNode, lastPart, offset, indexFirstPart);

		}
	}

	/**
	 * @param initialNode
	 * @param lastPart
	 * @param offset
	 * @param indexFirstPart
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	private void addLastPartToNode(TrieNode initialNode, String lastPart, long offset, int indexFirstPart) throws IOException, RecordSerializationException {
		// Agrego la ultima parte de la palabra a un nuevo nodo

		if (!(indexFirstPart == (this.getDepth() - 1))) {
			ArrayList<WordOffsetField> newWordOffsetList = new ArrayList<WordOffsetField>();
			WordOffsetField newWordOffsetField = new WordOffsetField(offset, lastPart, true);
			newWordOffsetList.add(newWordOffsetField);
			TrieNode newNode = new TrieNode(newWordOffsetList, this.getNextNodeNumber());
			long blockNumber = this.nodeFile.insertRecord(newNode);
			TrieNodeBlockIndex trieNodeBlockIndex = new TrieNodeBlockIndex(new IntegerField((int) blockNumber), new IntegerField((int) newNode.getNodeNumber()));
			this.nodeBlockIndexFile.getNodeBlockIndexFile().insertRecord(trieNodeBlockIndex);
			incrementNextNodeNumber();

		} else {
			WordOffsetField wordOffsetField = new WordOffsetField(offset, lastPart, true);
			ArrayList<WordOffsetField> wordOffsetList = (ArrayList<WordOffsetField>) initialNode.getWordOffsetList();
			wordOffsetList.add(wordOffsetField);
			initialNode.clearWordOffsetRecordList();
			initialNode.setWordOffsetRecordList(wordOffsetList);

			long updatedBlockNumber = this.nodeFile.updateRecord(initialNode);

			TrieNodeBlockIndex updatedTrieNodeBlockIndex = new TrieNodeBlockIndex();
			updatedTrieNodeBlockIndex = this.nodeBlockIndexFile.getNodeBlockIndexFile().getRecord(new IntegerField((int) initialNode.getNodeNumber()));
			updatedTrieNodeBlockIndex.setBlockNumber((int) updatedBlockNumber);
			this.nodeBlockIndexFile.getNodeBlockIndexFile().updateRecord(updatedTrieNodeBlockIndex);
		}

	}

	/**
	 * @param initialNode
	 * @param firstPart
	 * @param indexFirstPart
	 * @param i
	 * @param offset
	 * @param lastPart
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private void addFirstPartToNodes(TrieNode initialNode, String firstPart, int indexFirstPart, int i, long offset) throws RecordSerializationException, IOException {
		// Agrego la primer letra del resto de la primer parte de la palabra al
		// nodo actual

		String actualChar = "";

		WordOffsetField wordOffsetField = new WordOffsetField();
		// Si la palabra es más chica que el depth y esta la ultima letra a
		// agregar, hay que poner el offset.
		if (indexFirstPart == firstPart.length() - 1 && firstPart.substring(indexFirstPart).equals(" ")) {
			actualChar = firstPart.substring(indexFirstPart);
			wordOffsetField = new WordOffsetField(offset, actualChar, true);
		} else {
			actualChar = firstPart.substring(indexFirstPart, indexFirstPart + 1);
			wordOffsetField = new WordOffsetField(this.getNextNodeNumber(), actualChar, false);
		}

		ArrayList<WordOffsetField> wordOffsetList = (ArrayList<WordOffsetField>) initialNode.getWordOffsetList();
		wordOffsetList.add(wordOffsetField);
		initialNode.clearWordOffsetRecordList();
		initialNode.setWordOffsetRecordList(wordOffsetList);
		long updatedBlockNumber = this.nodeFile.updateRecord(initialNode);

		TrieNodeBlockIndex updatedTrieNodeBlockIndex = new TrieNodeBlockIndex();
		updatedTrieNodeBlockIndex = this.nodeBlockIndexFile.getNodeBlockIndexFile().getRecord(new IntegerField((int) initialNode.getNodeNumber()));
		updatedTrieNodeBlockIndex.setBlockNumber((int) updatedBlockNumber);
		this.nodeBlockIndexFile.getNodeBlockIndexFile().updateRecord(updatedTrieNodeBlockIndex);

		i++;

		// Agrego el resto de la primer parte de la palabra a nuevos nodos
		while (i < firstPart.length()) {
			this.incrementNextNodeNumber();
			ArrayList<WordOffsetField> newWordOffsetList = new ArrayList<WordOffsetField>();
			WordOffsetField newWordOffsetField = new WordOffsetField();
			String newActualChar = firstPart.substring((int) i, (int) i + 1);

			if (newActualChar.equals(" ")) {
				newWordOffsetField = new WordOffsetField(offset, newActualChar, true);
			} else {
				newWordOffsetField = new WordOffsetField(this.getNextNodeNumber(), newActualChar, false);
			}

			newWordOffsetList.add(newWordOffsetField);
			TrieNode newNode = new TrieNode(newWordOffsetList, this.getNextNodeNumber() - 1);
			long blockNumber = this.nodeFile.insertRecord(newNode);
			TrieNodeBlockIndex trieNodeBlockIndex = new TrieNodeBlockIndex(new IntegerField((int) blockNumber), new IntegerField((int) newNode.getNodeNumber()));
			this.nodeBlockIndexFile.getNodeBlockIndexFile().insertRecord(trieNodeBlockIndex);

			i++;
		}
	}

	private void createDataFile(FileManager fileManager) throws IOException {
		this.nodeFile = new DirectRecordFile<TrieNode, LongField>(fileManager.openFile(TRIE_INDEX_DAT), this);
		this.nodeBlockIndexFile = new TrieNodeBlockIndexFile(fileManager.openFile(TRIE_NODE_BLOCK_INDEX_DAT));

	}

	@Override
	public void load(FileManager fileManager, Configuration conf) throws IOException {

		this.depth = conf.getTrieDepth();
		createDataFile(fileManager);
		nodeFile.load();
		this.nodeBlockIndexFile.getNodeBlockIndexFile().load();

		boolean foundLastNode = false;
		int i = 0;
		while (!foundLastNode) {
			if (this.getNode(i) == null)
				foundLastNode = true;
			i++;
		}
		this.setNextNodeNumber(i - 1);

	}

	@Override
	public void install(FileManager fileManager, Configuration conf) throws IOException {
		createDataFile(fileManager);
		this.depth = conf.getTrieDepth();
		nodeFile.create(conf.getBlockSize());
		this.nodeBlockIndexFile.getNodeBlockIndexFile().create(conf.getBlockSize());

		int j = 0;
		ArrayList<WordOffsetField> wordOffsetList = new ArrayList<WordOffsetField>();
		TrieNode node = new TrieNode(wordOffsetList, j);
		long blockNumber = this.nodeFile.insertRecord(node);
		TrieNodeBlockIndex trieNodeBlockIndex = new TrieNodeBlockIndex(new IntegerField((int) blockNumber), new IntegerField(0));
		this.nodeBlockIndexFile.getNodeBlockIndexFile().insertRecord(trieNodeBlockIndex);
		this.nodeBlockIndexFile.getNodeBlockIndexFile().getRecord(new IntegerField(0));

	}

	@Override
	public boolean isInstalled(FileManager filemanager) throws IOException {
		return filemanager.exists(TRIE_INDEX_DAT) && filemanager.exists(TRIE_NODE_BLOCK_INDEX_DAT);
	}

	@Override
	public TrieNode createRecord() {
		return new TrieNode();
	}

	public String getTrieFileName() {
		return TRIE_INDEX_DAT;
	}

	public String getTrieNodeBlockIndexFileName() {
		return TRIE_NODE_BLOCK_INDEX_DAT;
	}

}
