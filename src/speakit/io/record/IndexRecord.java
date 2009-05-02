package speakit.io.record;

public abstract class IndexRecord<KEYTYPE extends Field> extends Record<KEYTYPE> {
	IntegerField block = new IntegerField();

	public int getBlockNumber() {
		return this.block.getInteger();
	}

	public void setBlockNumber(int block) {
		this.block.setInteger(block);
	}

	@Override
	protected Field[] getFields() {
		return new Field[] { this.getKey(), this.block };
	}
}
