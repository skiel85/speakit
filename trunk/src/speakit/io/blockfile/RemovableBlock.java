package speakit.io.blockfile;

import speakit.io.record.BooleanField;

public class RemovableBlock extends Block {

	private BooleanField isRemoved = new BooleanField();

	public RemovableBlock(int blockNumber) {
		super(blockNumber);
		this.addField(this.isRemoved);
	}

	public void setRemoved(boolean isRemoved) {
		this.isRemoved.setBoolean(isRemoved);
	}

	public boolean isRemoved() {
		return isRemoved.getBoolean();
	}

}
