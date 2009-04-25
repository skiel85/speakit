package speakit.io;

import java.io.IOException;
import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BlockFileIterator implements Iterator<Block> {

	private final BlockFile bytesBlocksFile;
	private int current = 0;
	private Block nextBlock = null;

	public BlockFileIterator(BlockFile bytesBlocksFile) {
		this.bytesBlocksFile = bytesBlocksFile;
		this.current = 0;
		try {
			this.nextBlock = this.bytesBlocksFile.getBlock(current);
		} catch (IOException e) {
			this.nextBlock = null;
		}
	}

	@Override
	public boolean hasNext() {
		return (this.nextBlock != null);
	}

	@Override
	public Block next() {
		Block currentBlock;
		currentBlock = this.nextBlock;
		this.current++;
		try {
			this.nextBlock = bytesBlocksFile.getBlock(current);
		} catch (IOException e) {
			this.nextBlock = null;
		}
		return currentBlock;
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

}
