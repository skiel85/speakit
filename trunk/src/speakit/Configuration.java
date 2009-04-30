package speakit;

public class Configuration {
	private int	blockSize	= 0;
	private int	trieDepth	= 0;
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public void setTrieDepth(int trieDepth) {
		this.trieDepth = trieDepth;
	}
	public int getTrieDepth() {
		return trieDepth;
	}
}
