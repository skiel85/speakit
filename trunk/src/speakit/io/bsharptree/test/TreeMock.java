package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.TreeNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class TreeMock extends Tree<TestIndexRecord, StringField> {

	private HashMap<Integer, TreeNode> nodes;

	public TreeMock(File file) {
		super(file, TestIndexRecord.createFactory(), new InvertedIndexIndexRecordEncoder());
		this.nodes = new HashMap<Integer, TreeNode>();
	}

	@Override
	public TreeNode getNode(int nodeNumber, TreeNode parent) throws IOException {
		return this.nodes.get(nodeNumber);
	}

	@Override
	public int getNodeSize() {
		return 150;
	}

	public void registerNodeInMock(TreeNode node) {
		this.nodes.put(node.getNodeNumber(), node);
	}

	public void registerNodesInMock(TreeNode[] nodes) {
		for (TreeNode node : nodes) {
			this.registerNodeInMock(node);
		}
	}
	
	@Override
	public void updateNode(TreeNode node) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		this.registerNodeInMock(node);
	}
	
	@Override
	protected int allocateNodeSpace(int blockQty) throws IOException {
		return (this.nodes.size() + 100);
	} 
}
