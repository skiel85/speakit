package speakit.io.bsharptree;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

public class RamTree<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> extends Tree<RECTYPE, KEYTYPE> {
	private HashMap<Integer, TreeNode> nodes = new HashMap<Integer, TreeNode>();
	private int nodeSize;

	public RamTree(File file, RecordFactory recordFactory) {
		super(file, recordFactory);
	}

	public RamTree(File file, RecordFactory recordFactory, RecordEncoder encoder) {
		super(file, recordFactory, encoder);
	}

	@Override
	public TreeNode getNode(int nodeNumber, TreeNode parent) throws IOException {
		return nodes.get(new Integer(nodeNumber));
	}

	@Override
	protected int allocateNodeSpace(int blockQty) throws IOException {
		int newNodeNumber = nodes.size();
		for (int i = newNodeNumber; i < blockQty + newNodeNumber; i++) {
			nodes.put(i, null);
		}
		return newNodeNumber;
	}

	@Override
	public void updateNode(TreeNode node) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		nodes.put(new Integer(node.getNodeNumber()), node);
	}

	@Override
	public void load() throws IOException {
		// No cargo.
	}

	@Override
	public void create(int nodeSize) throws IOException {
		allocateNodeSpace(ROOT_NODE_BLOCKS_QTY);
		this.root = instantiateRootNode(0);
		this.updateNode(this.root);
		this.nodeSize = nodeSize;
	}

	@Override
	public int getNodeSize() {
		return this.nodeSize;
	}
}
