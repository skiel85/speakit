package speakit.io.bsharptree;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class TreeIndexNode extends TreeNode {

	private static final int	NULL_NODE_NUMBER	= 0;
	private int				level	= -1;
	private int				leftChildNodeNumber;
	List<TreeNodeElement>	elements;

	public TreeIndexNode(Tree tree, int nodeNumber, int size) {
		super(tree, nodeNumber, size);
		this.elements = new ArrayList<TreeNodeElement>();
	}

	public boolean balanceChilds() throws IOException {
		TreeNode lastNode = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
		Iterator<TreeNodeElement> elementIt = this.getElements().iterator();
		while (elementIt.hasNext()) {
			TreeIndexNodeElement indexElement = (TreeIndexNodeElement) elementIt.next();
			TreeNode node = this.getTree().getNode(indexElement.getRightChildNodeNumber(), this);
			lastNode.passMaximumCapacityExcedentToTheRight(node);
			lastNode = node;
		}
		return childrenAreInOverflow();
	}

	private boolean childrenAreInOverflow() throws RecordSerializationException, IOException {
		TreeNode lastNode = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
		if (lastNode.isInOverflow()) {
			return true;
		} else {
			Iterator<TreeNodeElement> elementIt = this.getElements().iterator();
			while (elementIt.hasNext()) {
				TreeIndexNodeElement indexElement = (TreeIndexNodeElement) elementIt.next();
				TreeNode node = this.getTree().getNode(indexElement.getRightChildNodeNumber(), this);
				if (node.isInOverflow()) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	protected TreeNodeRecord createNodeRecord(int nodeNumber) {
		return new TreeIndexNodeRecord(nodeNumber);
	}

	@Override
	public TreeNode createSibling() throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		return this.getTree().instantiateNewIndexNodeAndSave(this.getLevel());
	}

	private int getChildFor(Field key) {
		int childForKey = this.getLeftChildNodeNumber();
		Iterator<TreeNodeElement> it = this.elements.iterator();

		boolean found = false;
		while (it.hasNext() && !found) {
			TreeIndexNodeElement element = (TreeIndexNodeElement) it.next();
			if (key.compareTo((Field) element.getKey()) >= 0) {
				childForKey = element.getRightChildNodeNumber();
			} else {
				found = true;
			}
		}

		return childForKey;
	}

	// private TreeNode[] getChildsOf(int elementIndex) throws IOException {
	// TreeNode leftChild;
	// if (elementIndex == 0) {
	// leftChild = this.getTree().getNode(this.getLeftChildNodeNumber(), this);
	// } else {
	// TreeIndexNodeElement leftElement = (TreeIndexNodeElement)
	// this.getElements().get(elementIndex - 1);
	// leftChild = this.getTree().getNode(leftElement.getRightChildNodeNumber(),
	// this);
	// }
	// TreeNode rightChild;
	// TreeIndexNodeElement element = (TreeIndexNodeElement)
	// this.getElements().get(elementIndex);
	// rightChild = this.getTree().getNode(element.getRightChildNodeNumber(),
	// this);
	//
	// return new TreeNode[] { leftChild, rightChild };
	// }

	private int[] getChildsIndexOf(int elementIndex) throws IOException {
		int leftChild;
		if (elementIndex == 0) {
			leftChild = this.getLeftChildNodeNumber();
		} else {
			TreeIndexNodeElement leftElement = (TreeIndexNodeElement) this.getElements().get(elementIndex - 1);
			leftChild = leftElement.getRightChildNodeNumber();
		}
		int rightChild;
		TreeIndexNodeElement element = (TreeIndexNodeElement) this.getElements().get(elementIndex);
		rightChild = element.getRightChildNodeNumber();

		return new int[]{leftChild, rightChild};
	}

//	/**
//	 * Devuelve la posición de la clave especificada dentro del array de
//	 * elementos.
//	 * 
//	 * @param key
//	 * @return
//	 */
//	private int getElementIndexOf(Field key) {
//		List<TreeNodeElement> elements = this.elements;
//		for (int i = 0; i < elements.size(); i++) {
//			TreeNodeElement element = elements.get(i);
//			if (element.getKey().compareTo(key) == 0) {
//				return i;
//			}
//		}
//		throw new IllegalArgumentException("La clave pasada no está en el array de elementos");
//	}

	/**
	 * Devuelve el indice de la clave que está entre medio de el nodo overflow y
	 * el nodo con el que voy a splitear
	 * 
	 * @param node
	 * @return
	 */
	private int getElementIndexThatPointsToNode(TreeNode node) {
		if (this.getLeftChildNodeNumber() == node.getNodeNumber()) {
			return 0;
		} else {
			Iterator<TreeNodeElement> iterator = this.elements.iterator();
			int counter = 0;
			while (iterator.hasNext()) {
				TreeIndexNodeElement eachElement = (TreeIndexNodeElement) iterator.next();// casteo
				// porque
				// son
				// elementos
				// de
				// este
				// mismo
				// nodo
				if (eachElement.pointsTo(node)) {
					return counter;
				}
				counter++;
			}
		}
		throw new IllegalArgumentException("El nodo no es apuntado por este nodo.");
	}

	@Override
	protected List<TreeNodeElement> getElements() {
		return this.elements;
	}

	public int getLeftChildNodeNumber() {
		return this.leftChildNodeNumber;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public Field getNodeKey() {
		return this.getElements().get(0).getKey();
	}

	@Override
	public Record getRecord(Field key) throws IOException, RecordSerializationException {
		int nodeNumberWhereToSearch = this.getChildFor(key);
		TreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToSearch, this);
		return nodeWhereToInsert.getRecord(key);
	}

	public void indexChild(TreeNode newChild) {
		if (this.getLeftChildNodeNumber() == NULL_NODE_NUMBER) {
			this.setLeftChildNodeNumber(newChild.getNodeNumber());
		} else {
			TreeIndexNodeElement element = new TreeIndexNodeElement();
			element.setKey(newChild.getNodeKey());
			element.setRightChild(newChild.getNodeNumber());
			this.insertElement(element);
		}
	}

	/**
	 * Inserta un registro recursivamente y balancea o splittea si hace falta
	 */

	@Override
	public void insertRecord(Record record) throws IOException, RecordSerializationException {
		int nodeNumberWhereToInsert = this.getChildFor(record.getKey());
		TreeNode nodeWhereToInsert = this.getTree().getNode(nodeNumberWhereToInsert, this);
		if (nodeWhereToInsert.getNodeNumber() == this.getNodeNumber()) {
			System.out.println(this.getTree().toString());
			throw new RuntimeException("El nodeNumberWhereToInsert es el mismo nodo.");
		}

		nodeWhereToInsert.insertRecord(record);

		// TODO balanceo
		if (nodeWhereToInsert.isInOverflow()) {
			// ...some beauty code...
		}
		// split
		if (nodeWhereToInsert.isInOverflow()) {
			// el split guarda el nodo en overflow, no hace falta hacerlo de vuelta 
			split(nodeWhereToInsert);
		} else {
			// se guarda acá porque en caso de overflow deberia guardarse dentro del metodo que hace split
			this.getTree().updateNode(nodeWhereToInsert);
		}
	}

	@Override
	protected void load(TreeNodeRecord nodeRecord) {
		super.load(nodeRecord);
		this.level = ((TreeIndexNodeRecord) nodeRecord).getLevel();
		TreeIndexNodeRecord indexNodeRecord = (TreeIndexNodeRecord) nodeRecord;
		this.leftChildNodeNumber = indexNodeRecord.getLeftChildNodeNumber();
		for (TreeNodeElement element : indexNodeRecord.getElements()) {
			elements.add(element);
		}
	}

	@Override
	protected void save(TreeNodeRecord nodeRecord) {
		super.save(nodeRecord);
		((TreeIndexNodeRecord) nodeRecord).setLevel(this.level);
		TreeIndexNodeRecord indexNodeRecord = (TreeIndexNodeRecord) nodeRecord;
		indexNodeRecord.setLeftChildNodeNumber(getLeftChildNodeNumber());
		for (TreeNodeElement element : elements) {
			indexNodeRecord.addElement(element);
		}
	}

	public void setLeftChildNodeNumber(int leftChildNodeNumber) {
		this.leftChildNodeNumber = leftChildNodeNumber;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * hace split del nodo especificado con el nodo hermano que corresponda
	 * @param overflowNode
	 * @throws IOException
	 */
	private void split(TreeNode overflowNode) throws IOException {
		final int elementIndexThatPointsToNode = getElementIndexThatPointsToNode(overflowNode);
		TreeNode[] childsToSplit = getSibilingToSplit(overflowNode, elementIndexThatPointsToNode);
		split(childsToSplit,elementIndexThatPointsToNode);
	}

	/**
	 * A partir de un nodo y el numero de elemento que lo indexa arma un array con dos nodos entre los cuales se va a realizar el split. Uno de los dos será el mismo nodo pasado como parámetro.
	 * @param overflowNode
	 * @param elementIndexThatPointsToNode
	 * @return
	 * @throws IOException
	 */
	private TreeNode[] getSibilingToSplit(TreeNode overflowNode, final int elementIndexThatPointsToNode) throws IOException {
		TreeNode[] childsToSplit = new TreeNode[2];		
		final int[] childsindex = this.getChildsIndexOf(elementIndexThatPointsToNode);		
		for (int i = 0; i < childsindex.length; i++) {
			int childindex = childsindex[i];
			if (childindex == overflowNode.getNodeNumber()) {
				childsToSplit[i] = overflowNode;
			} else {
				childsToSplit[i] = this.getTree().getNode(childindex, this);
			}
		}
		return childsToSplit;
	}

	/**
	 * Hace split entre los nodos pasados por parámetro. Se supone que childsToSplit[0] es el izquierdo y childsToSplit[1] el derecho.
	 * @param childsToSplit
	 * @param middleKeyIndex
	 * @throws BlockFileOverflowException
	 * @throws WrongBlockNumberException
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private void split(TreeNode[] childsToSplit, int middleKeyIndex) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		// Remuevo la clave del padre.
		TreeNodeElement removedIndexElement = this.getElement(middleKeyIndex);
		this.removeElement(middleKeyIndex);

		// Obtengo los dos nodos a unir y creo uno nuevo vacío en el medio.
		TreeNode leftChild = childsToSplit[0];
		TreeNode rightChild = childsToSplit[1];
		TreeNode middleChild = leftChild.createSibling();

		// Si el nodo actual es padre de nodos índice:
		if (this.getLevel() > 1) {
			// agrego al leftChild un nuevo elemento formado por
			TreeIndexNodeElement indexElementFromParent = new TreeIndexNodeElement();
			// la clave removida del padre
			indexElementFromParent.setKey(removedIndexElement.getKey());
			// y el puntero izquierdo de rightChild.
			indexElementFromParent.setRightChild(((TreeIndexNode) rightChild).getLeftChildNodeNumber());
			// Inserto el elemento.
			leftChild.insertElement(indexElementFromParent);
		}

		// Agrego luego todos los elementos de rightChild.
		leftChild.insertElements(rightChild.extractAllElements());

		// Guardo cantidad de elementos para luego validar consistencia.
		int elementCountBeforeSplit = leftChild.getElementCount();

		// Paso a la derecha lo que excede a la minima capacidad,
		leftChild.passMinimumCapacityExcedentToTheRight(middleChild);
		// y luego el del medio queda con excedente y lo pasa a su vez a la
		// derecha
		middleChild.passMinimumCapacityExcedentToTheRight(rightChild);

		// Agrego la referencia de los hijos.
		this.indexChild(middleChild);
		this.indexChild(rightChild);

		// Guardo los hijos.
		this.getTree().updateNode(leftChild);
		this.getTree().updateNode(middleChild);
		this.getTree().updateNode(rightChild);

		// Verifico consistencia.
		int elementCountAfterSplit = leftChild.getElementCount() + middleChild.getElementCount() + rightChild.getElementCount();
		if (elementCountBeforeSplit != elementCountAfterSplit) {
			throw new RuntimeException("Error en el split. cantidad de elementos antes:" + elementCountBeforeSplit + ", cantidad de elementos después:" + elementCountAfterSplit);
		}
	}
	
	/**
	 * Obtiene la una lista de los numeros de todos sus nodos hijos
	 * @return
	 */
	public List<Integer> getChildNodeNumbers(){
		List<Integer> childNodes = new ArrayList<Integer>();
		childNodes.add(this.leftChildNodeNumber);
		for (TreeNodeElement element : this.elements) {
			childNodes.add(((TreeIndexNodeElement) element).getRightChildNodeNumber());
		}
		return childNodes;
	}
	
	/**
	 * Obtiene la una lista de todos sus nodos hijos y elimina todos los elementos
	 * @return
	 * @throws IOException 
	 */
	public List<TreeNode> getExtractChildNodes() throws IOException{
		List<TreeNode> childNodes = new ArrayList<TreeNode>();
		childNodes.add(this.getTree().getNode(this.leftChildNodeNumber,this));
		for (TreeNodeElement element : this.elements) {
			childNodes.add(this.getTree().getNode(((TreeIndexNodeElement) element).getRightChildNodeNumber(),this));
		}
		clear();
		return childNodes;
	}
	
	@Override
	public List<TreeNode> getChildren() throws IOException {
		ArrayList<TreeNode> result = new ArrayList<TreeNode>();
		result.add(this.getTree().getNode(this.getLeftChildNodeNumber(), this));
		for (TreeNodeElement element : this.elements) {
			TreeIndexNodeElement indexElement = (TreeIndexNodeElement) element;
			result.add(this.getTree().getNode(indexElement.getRightChildNodeNumber(), this));
		}
		return result;
	}

	private void clear() {
		this.leftChildNodeNumber=NULL_NODE_NUMBER;
		this.elements.clear();
	}

	@Override
	public String toString() { 	
		String result = formatNodeNumber(this.getNodeNumber()  ) + ": " + this.getLeftChildNodeNumber();  
		for (TreeNodeElement element : this.elements) {
			TreeIndexNodeElement indexElement = (TreeIndexNodeElement) element;
			result += "(" + element.getKey().toString() + ")" + indexElement.getRightChildNodeNumber(); 
		}

		for (Integer nodeNumber : getChildNodeNumbers()) {
			try {
				result += "\n" + getCorrectIndent() + this.getTree().getNode(nodeNumber, this).toString();
			} catch (IOException e) {
				result += "\n" + getCorrectIndent() + formatNodeNumber(nodeNumber) + ": IOException";
			}
		}

		return result;
	}

	private String getCorrectIndent() {
		String res="";
		int indentationSize = this.getTree().getRoot().getLevel()-this.getLevel(); 
		for (int i = 0; i <= indentationSize; i++) {
			res+="\t";
		}
		return res;
	}
}