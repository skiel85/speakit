package speakit.compression.lzp;

import speakit.TextDocument;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.Symbol;


public class TextDocumentInterpreter {

		private static final int MAX_MATCH_LENGTH = 65536;
		private TextDocument document;
		private char[] charData;
		private Integer pointer;
		private Integer matchPointer;
		/**
		 */
		public TextDocumentInterpreter(TextDocument textDocument){
			this.document = textDocument;
			//byteData = new byte[document.getText().getBytes().length];
			charData = document.getText().toCharArray();
			pointer = 0;
		}
		
		public TextDocumentInterpreter() {
			pointer = 0;
		}

			
		/**
		 */
		public Symbol getActualSymbol(){
			if (pointer >= charData.length)
				return Symbol.getEof();
			return new Symbol(charData[pointer]);
		}
	
		/**
		 */
		public Context getContext(Integer length){
			Context context = new Context(length);
			if (this.pointer >= length) {
				for (int i = pointer - length; i < pointer; i++) {
					char ch = charData[i];
					context.add(new Symbol(ch));
				}
			} else {
				context = getContext(getCurrentPosition());
			}
			return context;
		}
			
		public void advance() {
			this.advance(1);
		}
		/**
		 */
		public void advance(Integer pos){
			pointer += pos;
		}

		public boolean hasData() {
			return pointer <= charData.length;
		}


		public Integer getMatchLength(Integer matchPos) {
			if (matchPos == -1)
				return 0;
			matchPointer = matchPos;
			Integer match = 0;
			while (pointer < charData.length && match <= MAX_MATCH_LENGTH 
					&& charData[matchPointer] == charData[pointer]) {
				match++;
				pointer++;
				matchPointer++;
			}
			return match;
		}


		public Integer getCurrentPosition() {
			return pointer;
		}


		public void setPosition(int i) {
			pointer = i;
		}


		public TextDocument getDocument() {
			return document;
		}

}
