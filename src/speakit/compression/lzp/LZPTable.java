package speakit.compression.lzp;

import java.util.HashMap;

import speakit.compression.arithmetic.Context;


public class LZPTable {

		private static final int DEFAULT_CONTEXT_LENGTH = 2;
		private HashMap<String, Integer> matchs;
		private int minContextLength;
		/**
		 */
		
		public LZPTable(int minContextLength) {
			matchs = new HashMap<String, Integer>();
			this.minContextLength = minContextLength;
		}
		
		public LZPTable() {			
			this(DEFAULT_CONTEXT_LENGTH);
		}
		
		public Integer getLastMatchPosition(Context context){
			if (matchs.containsKey(context.toString())) {
				return matchs.get(context.toString());
			}
			return -1;
		}

			
		/**
		 */
		public void update(Context context, Integer position){
			if (isActualizable(context)) {
				if (matchs.containsKey(context.toString())) {
					matchs.remove(context.toString());
				}
				matchs.put(context.toString(), position);
			}
		}
		
		public String toString() {
			return matchs.toString();
		}
		
		private boolean isActualizable(Context context) {
			return context.size() >= minContextLength;
		}
}
