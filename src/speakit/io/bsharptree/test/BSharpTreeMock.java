package speakit.io.bsharptree.test;

import java.io.File;
import java.util.HashMap;

import speakit.io.bsharptree.BSharpTree;
import speakit.io.bsharptree.BSharpTreeLeafNode;
import speakit.io.bsharptree.BSharpTreeNode;
import speakit.io.record.StringField;

public class BSharpTreeMock extends BSharpTree<TestIndexRecord, StringField> {

	private HashMap<Integer, BSharpTreeNode> nodes;

	public BSharpTreeMock(File file) {
		super(file);
		this.nodes = new HashMap<Integer, BSharpTreeNode>();
	}

	@Override
	public BSharpTreeNode getNode(int nodeNumber) {
		return this.nodes.get(nodeNumber);
	}

	public void registerNodeInMock(BSharpTreeNode node) {
		this.nodes.put(node.getNodeNumber(), node);
	}

	public void registerNodesInMock(BSharpTreeNode[] nodes) {
		for (BSharpTreeNode node : nodes) {
			this.registerNodeInMock(node);
		}
	}
}
