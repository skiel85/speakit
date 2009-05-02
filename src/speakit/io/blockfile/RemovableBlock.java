package speakit.io.blockfile;

import speakit.io.record.BooleanField;
import speakit.io.record.Field;

public class RemovableBlock extends Block {

	private BooleanField isRemoved = new BooleanField();

	public RemovableBlock(int blockNumber) {
		super(blockNumber);
	}

	public void setRemoved(boolean isRemoved) {
		this.isRemoved.setBoolean(isRemoved);
	}

	public boolean isRemoved() {
		return isRemoved.getBoolean();
	}

	@Override
	protected Field[] getFields() {
		return this.JoinFields(super.getFields(),new Field[]{ this.isRemoved});
	}
}
