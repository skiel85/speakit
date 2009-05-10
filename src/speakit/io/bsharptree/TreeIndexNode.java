package speakit.io.bsharptree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

/**
 * Nodo índice del árbol B#.
 */
@SuppressWarnings("unchecked")
public class TreeIndexNode extends TreeNode {

	private static final int	NULL_NODE_NUMBER	= 0;
	private int					level				= -1;
	private int					leftChildNodeNumber;
	List<TreeNodeElement>		elements;

	public TreeIndexNode(Tree tree, int nodeNumber, int size) {
		super(tree, nodeNumber, size);
		this.elements = new ArrayList<TreeNodeElement>();
	}

	/*
	 * public boolean balanceChilds(TreeNode nodeWhereToInsert) throws
	 * IOException { TreeNode lastNode =
	 * this.getTree().getNode(this.getLeftChildNodeNumber(), this);
	 * Iterator<TreeNodeElement> elementIt = this.getElements().iterator();
	 * while (elementIt.hasNext()) { TreeIndexNodeElement indexElement =
	 * (TreeIndexNodeElement) elementIt.next(); TreeNode node =
	 * this.getTree().getNode(indexElement.getRightChildNodeNumber(), this);
	 * lastNode.passMaximumCapacityExcedentToTheRight(node); lastNode = node; }
	 * return childrenAreInOverflow(); }
	 */
	public boolean balanceChilds(TreeNode overflowNode) throws IOException {
		final int elementIndexThatPointsToNode = getElementIndexThatPointsToNode(overflowNode);
		TreeNode[] childsToSplit = getSibilingToSplit(overflowNode, elementIndexThatPointsToNode);
		balance(childsToSplit, elementIndexThatPointsToNode);

		return childrenAreInOverflow();
	}

	/**
	 * Hace split entre los nodos pasados por parámetro. Se supone que
	 * childsToSplit[0] es el izquierdo y childsToSplit[1] el derecho.
	 * 
	 * @param childsToSplit
	 * @param middleKeyIndex
	 * @throws BlockFileOverflowException
	 * @throws WrongBlockNumberException
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private void balance(TreeNode[] childsToSplit, int middleKeyIndex) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {

		// Tengo q ver si saco las claves mayores o las menores
		TreeNode giverNode;
		TreeNode receiverNode;
		if (childsToSplit[0].isInOverflow()) {
			giverNode = childsToSplit[0];
			receiverNode = childsToSplit[1];
		} else {
			giverNode = childsToSplit[1];
			receiverNode = childsToSplit[0];
		}
		int elementCountBeforeOperation = giverNode.getElementCount() + receiverNode.getElementCount();

		List<TreeNodeElement> excedent;
		boolean lowerExcedent = false;
		if (giverNode.getNodeKey().compareTo(receiverNode.getNodeKey()) < 0) {
			// la clave de giver es menor, el excedente lo saco del final
			excedent = giverNode.extractUpperExcedent();
		} else {
			excedent = giverNode.extractLowerExcedent();
			lowerExcedent = true;
		}

		receiverNode.insertElements(excedent);
		int postOperationCount = giverNode.getElementCount() + receiverNode.getElementCount();

		if (receiverNode.isInOverflow()) {
			// Me esta dando overflow el receptor, significa q no puedo
			// balancear. Deshago los cambios
			giverNode.insertElements(excedent);
			verifyOperation(elementCountBeforeOperation, "balanceo", postOperationCount);
			return;
		}

		// Remuevo la clave del padre.
		TreeNodeElement removedIndexElement = this.getElement(middleKeyIndex);
		this.removeElement(middleKeyIndex);

		if (lowerExcedent) {
			// debo reindexar el nodo q "dono" elementos
			this.indexChild(giverNode);
		} else {
			// reindexo el nodo q recibio los elementos
			this.indexChild(receiverNode);
		}
		// chequear overflow

		this.getTree().updateNode(giverNode);
		this.getTree().updateNode(receiverNode);

		/*
		 * // Si el nodo actual es padre de nodos índice: if (this.getLevel() >
		 * 1) { // agrego al leftChild un nuevo elemento formado por
		 * TreeIndexNodeElement indexElementFromParent = new
		 * TreeIndexNodeElement(); // la clave removida del padre
		 * indexElementFromParent.setKey(removedIndexElement.getKey()); // y el
		 * puntero izquierdo de rightChild.
		 * indexElementFromParent.setRightChild(((TreeIndexNode)
		 * rightChild).getLeftChildNodeNumber()); // Inserto el elemento.
		 * leftChild.insertElement(indexElementFromParent); }
		 */

		verifyOperation(elementCountBeforeOperation, "balanceo", postOperationCount);

	}

	private void verifyOperation(int elementCountBeforeOperation, String operation, int postOperationCount) {
		// Verifico consistencia
		if (elementCountBeforeOperation != postOperationCount) {
			throw new RuntimeException("Error en el " + operation + ". cantidad de elementos antes:" + elementCountBeforeOperation + ", cantidad de elementos después:"
					+ postOperationCount);
		}
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

	// /**
	// * Devuelve la posición de la clave especificada dentro del array de
	// * elementos.
	// *
	// * @param key
	// * @return
	// */
	// private int getElementIndexOf(Field key) {
	// List<TreeNodeElement> elements = this.elements;
	// for (int i = 0; i < elements.size(); i++) {
	// TreeNodeElement element = elements.get(i);
	// if (element.getKey().compareTo(key) == 0) {
	// return i;
	// }
	// }
	// throw new
	// IllegalArgumentException("La clave pasada no está en el array de elementos");
	// }

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

	public void indexChild(TreeNode newChild) throws IOException {
		try {
			if (this.getLeftChildNodeNumber() == NULL_NODE_NUMBER) {
				this.setLeftChildNodeNumber(newChild.getNodeNumber());
			} else {
				TreeIndexNodeElement element = new TreeIndexNodeElement();
//				element.setKey(newChild.getNodeKey());
				element.setKey(newChild.getLowestKey());
				element.setRightChild(newChild.getNodeNumber());
				this.insertElement(element);
			}
		} catch (IndexOutOfBoundsException ex) {
			throw new RuntimeException("No se puede indexar el nodo. " + ex.getMessage(), ex);
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
		// split
		if (nodeWhereToInsert.isInOverflow()) {
			// el split guarda el nodo en overflow, no hace falta hacerlo de
			// vuelta
			split(nodeWhereToInsert);
		} else {
			// se guarda acá porque en caso de overflow deberia guardarse dentro
			// del metodo que hace split
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
	 * 
	 * @param overflowNode
	 * @throws IOException
	 */
	private void split(TreeNode overflowNode) throws IOException {
		final int elementIndexThatPointsToNode = getElementIndexThatPointsToNode(overflowNode);
		TreeNode[] childsToSplit = getSibilingToSplit(overflowNode, elementIndexThatPointsToNode);
		split(childsToSplit, elementIndexThatPointsToNode);
	}

	/**
	 * A partir de un nodo y el numero de elemento que lo indexa arma un array
	 * con dos nodos entre los cuales se va a realizar el split. Uno de los dos
	 * será el mismo nodo pasado como parámetro.
	 * 
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
	 * Hace split entre los nodos pasados por parámetro. Se supone que
	 * childsToSplit[0] es el izquierdo y childsToSplit[1] el derecho.
	 * 
	 * @param childsToSplit
	 * @param middleKeyIndex
	 * @throws BlockFileOverflowException
	 * @throws WrongBlockNumberException
	 * @throws RecordSerializationException
	 * @throws IOException
	 */
	private void split(TreeNode[] childsToSplit, int middleKeyIndex) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		// Remuevo la clave del padre.
		this.removeElement(middleKeyIndex);

		// Verifico si el nodo actual es padre de nodos índice.
		boolean isParentOfIndexNodes = (this.getLevel() > 1);

		// Si el nodo actual es padre de nodos índice:
		if (isParentOfIndexNodes) {
			// Obtengo los dos nodos a unir y creo uno nuevo vacío en el medio.
			TreeIndexNode leftChild = (TreeIndexNode) childsToSplit[0];
			TreeIndexNode rightChild = (TreeIndexNode) childsToSplit[1];

			// Creo una lista de nodos nietos de los nodos a unir.
			ArrayList<TreeNode> grandchildrenNodes = new ArrayList<TreeNode>();
			grandchildrenNodes.addAll(leftChild.extractChildNodes());
			grandchildrenNodes.addAll(rightChild.extractChildNodes());

			// Trato de balancear entre hermanos
			Iterator<TreeNode> grandchildNodeIt = grandchildrenNodes.iterator();
			while (grandchildNodeIt.hasNext() && !leftChild.isInOverflow()) {
				leftChild.indexChild(grandchildNodeIt.next());
			}
			TreeIndexNodeElement element = (TreeIndexNodeElement) leftChild.extractLastElement();
			rightChild.setLeftChildNodeNumber(element.getRightChildNodeNumber());
			while (grandchildNodeIt.hasNext() && !leftChild.isInOverflow()) {
				rightChild.indexChild(grandchildNodeIt.next());
			}

			if (!rightChild.isInOverflow()) {
				// balanceo exitoso
				this.indexChild(rightChild);
				this.getTree().updateNode(leftChild);
				this.getTree().updateNode(rightChild);
			} else {
				// el balanceo no fué suficiente, hay que hacer split

				// limpio los nodos que quedaron sucios por el intento de
				// balanceo
				leftChild.extractChildNodes();
				rightChild.extractChildNodes();

				// creo un nuevo nodo
				TreeIndexNode middleChild = (TreeIndexNode) leftChild.createSibling();

				// Inserto en cada nodo hasta que supera el underflow.
				for (TreeNode grandchildNode : grandchildrenNodes) {
					if (leftChild.isInUnderflow()) {
						leftChild.indexChild(grandchildNode);
					} else if (middleChild.isInUnderflow()) {
						middleChild.indexChild(grandchildNode);
					} else {
						rightChild.indexChild(grandchildNode);
					}
				}
				

				// Agrego al padre la referencia al nuevo hijo.
				this.indexChild(middleChild);

				// Agrego al padre la referencia al hijo derecho.
				this.indexChild(rightChild);

				// Guardo los hijos.
				this.getTree().updateNode(leftChild);
				this.getTree().updateNode(middleChild);
				this.getTree().updateNode(rightChild);
			}

			System.out.println(this.getTree());
		}
		// Si el nodo actual es padre de nodos hoja:
		else {
			// Obtengo los dos nodos a unir y creo uno nuevo vacío en el medio.
			TreeNode leftChild = childsToSplit[0];
			TreeNode rightChild = childsToSplit[1];
			// Guardo cantidad de elementos para luego validar consistencia.
			int elementCountBeforeSplit = leftChild.getElementCount();

			// Balanceo:
			// Agrego luego todos los elementos de rightChild.
			leftChild.insertElements(rightChild.extractAllElements());
			// paso los elementos excedentes el nodo derecho
			leftChild.passMaximumCapacityExcedentToTheRight(rightChild);

			if (!rightChild.isInOverflow()) {
				// balanceo exitoso
				this.indexChild(rightChild);
				this.getTree().updateNode(leftChild);
				this.getTree().updateNode(rightChild);

				// Verifico consistencia.
				int elementCountAfterSplit = leftChild.getElementCount() + rightChild.getElementCount();

				// Verifico que la cantidad de elementos sea la misma antes y
				// después de dividir.
				// if (elementCountBeforeSplit != elementCountAfterSplit) {
				// throw new
				// AssertionError("Error en el split. Cantidad de elementos antes:"
				// + elementCountBeforeSplit +
				// ", cantidad de elementos después:"
				// + elementCountAfterSplit);
				// }
			} else {
				// vuelvo a poner los elementos en el nodo izquierdo
				leftChild.insertElements(rightChild.extractAllElements());

				// creo un nuevo nodo para splittear
				TreeNode middleChild = leftChild.createSibling();

				// Paso a la derecha lo que excede a la minima capacidad,
				leftChild.passMinimumCapacityExcedentToTheRight(middleChild);

				// Agrego al padre la referencia al nuevo hijo.
				this.indexChild(middleChild);

				// Paso a la derecha lo que excede a la mínima capacidad.
				middleChild.passMinimumCapacityExcedentToTheRight(rightChild);

				// Vuelvo a agregar al padre la referencia al hijo derecho.
				this.indexChild(rightChild);

				// Guardo los hijos.
				this.getTree().updateNode(leftChild);
				this.getTree().updateNode(middleChild);
				this.getTree().updateNode(rightChild);

				// Verifico consistencia.
				int elementCountAfterSplit = leftChild.getElementCount() + middleChild.getElementCount() + rightChild.getElementCount();

				// Verifico que la cantidad de elementos sea la misma antes y
				// después de dividir.
				// if (elementCountBeforeSplit != elementCountAfterSplit) {
				// throw new
				// AssertionError("Error en el split. Cantidad de elementos antes:"
				// + elementCountBeforeSplit +
				// ", cantidad de elementos después:"
				// + elementCountAfterSplit);
				// }
			}
		}
	}

	/**
	 * Obtiene la una lista de los numeros de todos sus nodos hijos
	 * 
	 * @return
	 */
	public List<Integer> getChildNodeNumbers() {
		List<Integer> childNodes = new ArrayList<Integer>();
		if (this.getElementCount() > 0 || this.leftChildNodeNumber!=this.NULL_NODE_NUMBER) {
			childNodes.add(this.leftChildNodeNumber);
			for (TreeNodeElement element : this.elements) {
				childNodes.add(((TreeIndexNodeElement) element).getRightChildNodeNumber());
			}
		}
		return childNodes;
	}

	/**
	 * Obtiene la una lista de todos sus nodos hijos y elimina todos los
	 * elementos
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<TreeNode> extractChildNodes() throws IOException {
		List<TreeNode> childNodes = getChildren();
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
		this.leftChildNodeNumber = NULL_NODE_NUMBER;
		this.elements.clear();
	}

	@Override
	public String toString() {
		String result = getStringHeader() + " : " + this.getLeftChildNodeNumber();
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
		String res = "";
		int indentationSize = this.getTree().getRoot().getLevel() - this.getLevel();
		for (int i = 0; i <= indentationSize; i++) {
			res += "\t";
		}
		return res;
	}

	@Override
	public void updateRecord(Record record) throws IOException {
		int childNodeNumberWhereToUpdate = this.getChildFor(record.getKey());
		TreeNode childNodeWhereToUpdate = this.getTree().getNode(childNodeNumberWhereToUpdate, this);
		childNodeWhereToUpdate.updateRecord(record);
		this.getTree().updateNode(childNodeWhereToUpdate);
	}

	@Override
	public Field getLowestKey() throws IOException {
		if(this.getLeftChildNodeNumber()!=NULL_NODE_NUMBER){
			return this.getTree().getNode(leftChildNodeNumber, this).getLowestKey();
		}
		return null;
	}
}
