package speakit.compression.arithmetic;

import java.util.ArrayList;
import java.util.List;


public class Context {

		private List<Symbol> chars;
		private int maxDepth;
		/**
		 */
		public Context(int maxDepth){
			chars = new ArrayList<Symbol>(maxDepth);
			this.maxDepth = maxDepth;
		}
		
		
		/**
		 */
		public Context subContext(int depth){
			Context sub = new Context(depth);
			if (depth > this.maxDepth)
				depth = maxDepth;
			int init = (this.chars.size() - depth) < 0 ? 0 : this.chars.size() - depth;  
			for (int i = init ; i < this.chars.size(); i++) {
				sub.add(chars.get(i));
			}
			return sub;
		}
			
				
		/**
		 */
		public void add(Symbol symbol){
			if (chars.size() < maxDepth )
				chars.add(symbol);
			else {
				List<Symbol> subList = chars.subList(1, chars.size());
				subList.add(symbol);
				chars = subList;
			}
		}

		public Integer size() {
			return chars.size();
		}
		
		public String toString() {
			String out = "";
			for (Symbol symbol : chars) {
				out = out.concat(symbol.toString());
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
		
		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}
		
}
