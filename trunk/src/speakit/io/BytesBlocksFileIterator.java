package speakit.io;

import java.io.IOException;
import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BytesBlocksFileIterator implements Iterator<BytesBlock> {

	private final BytesBlocksFile	bytesBlocksFile;
	private int	current=0;
	

	public BytesBlocksFileIterator(BytesBlocksFile bytesBlocksFile) {
		this.bytesBlocksFile = bytesBlocksFile; 
		this.current=0;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public BytesBlock next() {
		try {
			BytesBlock currentBlock; 
			do{
				currentBlock = bytesBlocksFile.getBlock(current);				 
				current++;
			}while(currentBlock.getIsRemoved());
			
			return currentBlock;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

}
