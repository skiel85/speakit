package speakit.io.record;

import speakit.compression.frontcoding.FrontCodedWord;

public class FrontCodedStringField extends CompositeField {

	private ShortField matchingCharacters =new ShortField();
	private StringField endingCharacters =new StringField();
	
	public FrontCodedStringField(){
	}
	
	public void load(FrontCodedWord codedWord){
		this.matchingCharacters.setShort(codedWord.getMatchingCharacters());
		this.endingCharacters.setString(codedWord.getEndingCharacters());
	}
	
	public FrontCodedWord asFrontCodedWord(){
		return new FrontCodedWord(this.matchingCharacters.getShort(),this.endingCharacters.getString());
	}
	
	@Override
	protected Field[] getFields() {
		return new Field[]{this.matchingCharacters,this.endingCharacters};
	}

	@Override
	protected int compareToSameClass(Field o) {
		FrontCodedStringField frontCodedStringField = (FrontCodedStringField)o;
		if(matchingCharacters.compareTo(frontCodedStringField.matchingCharacters)==0){
			return  endingCharacters.compareTo(frontCodedStringField.endingCharacters);
		}else{
			return  matchingCharacters.compareTo(frontCodedStringField.matchingCharacters);
		}
	}

}
