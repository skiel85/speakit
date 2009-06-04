package speakit.compression.lzp;

import speakit.TextDocument;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.Symbol;


public class TextDocumentInterpreter {

		private TextDocument document;
		/**
		 */
		public TextDocumentInterpreter(TextDocument textDocument){
			this.document = textDocument;
		}

			
		/**
		 */
		public Symbol getActualSymbol(){
			return null;
		}
	
		/**
		 */
		public Context getContext(Integer length){
			return null;
		}
			
		/**
		 */
		public void advance(Integer pos){
		}

		public boolean hasData() {
			return false;
		}

}
