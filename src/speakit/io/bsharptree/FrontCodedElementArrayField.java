//package speakit.io.bsharptree;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import speakit.compression.FrontCodedWord;
//import speakit.compression.FrontCodingWordEncoder;
//import speakit.io.record.ArrayField;
//import speakit.io.record.CompositeField;
//import speakit.io.record.Field;
//import speakit.io.record.Record;
//import speakit.io.record.RecordFactory;
//import speakit.io.record.RecordSerializationException;
//import speakit.io.record.ShortField;
//import speakit.io.record.StringField;
//
//public class FrontCodedElementArrayField extends Field {
//
//	private class FrontCodedElement extends CompositeField{
//		
//		private ShortField matchingCharacters;
//		private BSharpTreeLeafNodeElement element;
//		
////		public FrontCodedElement(RecordFactory recordFactory){
////			this.matchingCharacters=new ShortField();
////			this.element=new BSharpTreeLeafNodeElement(recordFactory);
////		}
//		
////		public void encode(FrontCodingWordEncoder ){
////			
////		}
//
//		public FrontCodedElement(short matchingCharacters, Record clonedRecord) {
//			this.matchingCharacters.setShort(matchingCharacters);
//			this.element=new BSharpTreeLeafNodeElement(clonedRecord);
//		}
//
//		@Override
//		protected Field[] getFields() { 
//			return new Field[]{matchingCharacters,element};
//		}
//
//		@Override
//		protected int compareToSameClass(Field o) {
//			return 0;
//		}
//		
//	}
//	
//	private ArrayField<BSharpTreeLeafNodeElement>	elements;
//	private final RecordFactory						recordFactory;
//	public FrontCodedElementArrayField(ArrayField<BSharpTreeLeafNodeElement> elements, RecordFactory recordFactory) {
//		this.elements = elements;
//		this.recordFactory = recordFactory;
//	}
//
//	public void fillElements(ArrayField<BSharpTreeLeafNodeElement>	to){
//		for (BSharpTreeLeafNodeElement item : elements) {
//			to.addItem(item);
//		}
//	}
//	
//	@Override
//	protected void actuallyDeserialize(InputStream in) throws IOException {
//
//	}
//
//	@Override
//	protected void actuallySerialize(OutputStream out) throws IOException {
//		FrontCodingWordEncoder codingWordEncoder=new FrontCodingWordEncoder();
//		for (BSharpTreeLeafNodeElement element : elements) {
//			Record clonedRecord = element.getRecord().clone();
//			StringField key=(StringField)clonedRecord.getKey();
//			FrontCodedWord encodedKey = codingWordEncoder.encode(key.getString());
//			key.setString(encodedKey.getEndingCharacters());
//			FrontCodedElement codedElement = new FrontCodedElement(encodedKey.getMatchingCharacters(),clonedRecord);
//		}
//	}
//
//	@Override
//	protected int compareToSameClass(Field o) {
//		return 0;
//	}
//
//	@Override
//	public int getSerializationSize() throws RecordSerializationException, IOException {
//		return 0;
//	}
//
//	@Override
//	protected String getStringRepresentation() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
