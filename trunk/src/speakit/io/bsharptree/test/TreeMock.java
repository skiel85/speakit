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

	public void registerNodeInMock(TreeNode node) {
		this.nodes.put(node.getNodeNumber(), node);
	}

	public void registerNodesInMock(TreeNode[] nodes) {
		for (TreeNode node : nodes) {
			this.registerNodeInMock(node);
		}
	}

	@Override
	public void saveNode(TreeNode node) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		this.registerNodeInMock(node);
	}

	@Override
	public int getNodeSize() {
		return 150;
	}

	@Override
	public void saveNode(TreeNode node, boolean create) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		if (create) {
			node.setNodeNumber(this.nodes.size() + 100);
		}
		this.registerNodeInMock(node);
	}
}
