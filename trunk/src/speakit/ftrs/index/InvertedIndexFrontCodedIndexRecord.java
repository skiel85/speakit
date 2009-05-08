package speakit.ftrs.index;

import speakit.compression.FrontCodedWord;
import speakit.io.record.Field;
import speakit.io.record.FrontCodedStringField;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class InvertedIndexFrontCodedIndexRecord extends Record<FrontCodedStringField>  {
	private FrontCodedStringField encodedKey = new FrontCodedStringField();
	IntegerField block = new IntegerField();
 
	@Override
	public FrontCodedStringField getKey() {
		return this.encodedKey;
	}
	
	public int getBlockNumber() {
		return this.block.getInteger();
	}

	public void setBlockNumber(int block) {
		this.block.setInteger(block);
	}
	
	
	@Override
	protected Field[] getFields() {
		return new Field[] { this.encodedKey, this.block };
	}
	
	public void setEncodedWord(FrontCodedWord codedWord){
		this.encodedKey.load(codedWord);
	}
	
	public FrontCodedWord getEncodedKey(){
		return this.encodedKey.asFrontCodedWord();
	}

	public InvertedIndexFrontCodedIndexRecord(FrontCodedWord codedWord, int block) {
		this.encodedKey.load(codedWord);
		this.setBlockNumber(block);
	} 

	public InvertedIndexFrontCodedIndexRecord() { 
	}
}
