package speakit.io;

import java.io.IOException;
import java.util.Iterator;

import speakit.dictionary.files.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RemovableBlockFileIterator implements Iterator<RemovableBlock> {

	private final RemovableBlockFile bytesBlocksFile;
	private int current = 0;
	private RemovableBlock nextBlock = null;

	public RemovableBlockFileIterator(RemovableBlockFile bytesBlocksFile) throws RecordSerializationException {
		this.bytesBlocksFile = bytesBlocksFile;
		this.current = 0;
		try {
			this.nextBlock = (RemovableBlock) this.bytesBlocksFile.getBlock(current);
		} catch (IOException e) {
			this.nextBlock = null;
		}
	}

	@Override
	public boolean hasNext() {
		return (this.nextBlock != null);
	}

	@Override
	public RemovableBlock next() {
		RemovableBlock currentBlock;
		do {
			currentBlock = this.nextBlock;
			this.current++;
			try {
				this.nextBlock = (RemovableBlock) bytesBlocksFile.getBlock(current);
			} catch (IOException e) {
				this.nextBlock = null;
			} catch (RecordSerializationException e) {
				this.nextBlock = null;
			}
		} while (currentBlock != null && currentBlock.isRemoved());
		return currentBlock;
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

}
