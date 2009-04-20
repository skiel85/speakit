package speakit.io;

import java.io.IOException;
import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BytesBlocksFileIterator implements Iterator<BytesBlock> {

	private final BytesBlocksFile bytesBlocksFile;
	private int current = 0;
	private BytesBlock nextBlock = null;

	public BytesBlocksFileIterator(BytesBlocksFile bytesBlocksFile) {
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
	public BytesBlock next() {
		BytesBlock currentBlock;
		do {
			currentBlock = this.nextBlock;
			this.current++;
			try {
				this.nextBlock = bytesBlocksFile.getBlock(current);
			} catch (IOException e) {
				this.nextBlock = null;
			}
		} while (currentBlock != null && currentBlock.getIsRemoved());
		return currentBlock;
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

}
