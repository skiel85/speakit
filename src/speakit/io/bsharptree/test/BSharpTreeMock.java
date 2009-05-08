package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.blockfile.BlockFileOverflowException;
import speakit.io.blockfile.WrongBlockNumberException;
import speakit.io.bsharptree.BSharpTree;
import speakit.io.bsharptree.BSharpTreeNode;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeMock extends BSharpTree<TestIndexRecord, StringField> {

	private HashMap<Integer, BSharpTreeNode> nodes;

	public BSharpTreeMock(File file) {
		super(file,TestIndexRecord.createFactory(), new InvertedIndexIndexRecordEncoder());
		this.nodes = new HashMap<Integer, BSharpTreeNode>();
	}

	@Override
	public BSharpTreeNode getNode(int nodeNumber, BSharpTreeNode parent) throws IOException {
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

	@Override
	public void saveNode(BSharpTreeNode node) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		this.registerNodeInMock(node);
	}

	@Override
	public int getNodeSize() {
		return 150;
	}

	@Override
	public void saveNode(BSharpTreeNode node, boolean create) throws BlockFileOverflowException, WrongBlockNumberException, RecordSerializationException, IOException {
		if (create) {
			node.setNodeNumber(this.nodes.size() + 100);
		}
		this.registerNodeInMock(node);
	}
}
