package speakit.compression.arithmetic;

import java.util.ArrayList;


public class Context {

		private ArrayList<Symbol> chars;
		
		/**
		 */
		public Context subContext(int depth){
			return null;
		}

			
		/**
		 */
		public Context(int maxDepth){
			chars = new ArrayList<Symbol>(maxDepth);
		}
				
		/**
		 */
		public void add(Symbol symbol){
		}

		public Integer size() {
			return chars.size();
		}
		
		public String toString() {
			String out = "";
			for (Symbol symbol : chars) {
				out.concat(symbol.toString());
			}
			return out;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Context) {
				Context other = (Context) obj;
				if (this.size() == other.size() && this.toString().equals(other.toString()) ) {
					return true;
				}
			}	
			return false;
		}
		
}
