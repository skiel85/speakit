package speakit.compression.lzp;

import speakit.TextDocument;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.Symbol;


public class TextDocumentInterpreter {

		private TextDocument document;
		private char[] charData;
		private Integer pointer;
		/**
		 */
		public TextDocumentInterpreter(TextDocument textDocument){
			this.document = textDocument;
			//byteData = new byte[document.getText().getBytes().length];
			charData = document.getText().toCharArray();
			pointer = 0;
		}

			
		/**
		 */
		public Symbol getActualSymbol(){
			return null;
		}
	
		/**
		 */
		public Context getContext(Integer length){
			if (this.pointer >= length) {
				Context context = new Context(length);
				for (int i = pointer - length; i < pointer; i++) {
					char ch = charData[i];
					context.add(new Symbol(ch));
				}
				return context;
			}
			return null;
		}
			
		/**
		 */
		public void advance(Integer pos){
		}

		public boolean hasData() {
			return pointer == charData.length;
		}

}
