package speakit.compression.lzp;

import java.util.HashMap;

import speakit.compression.arithmetic.Context;


public class LZPTable {

		private HashMap<Context, Integer> matchs;
		/**
		 */
		
		public LZPTable() {
			matchs = new HashMap<Context, Integer>();
		}
		public Integer getLastMatchPosition(Context context){
			if (matchs.containsKey(context)) {
				return matchs.get(context);
			}
			return 0;
		}

			
		/**
		 */
		public void update(Context context, Integer position){
			if (matchs.containsKey(context)) {
				matchs.remove(context);
			}
			matchs.put(context, position);
		}
		
		public String toString() {
			return matchs.toString();
		}

}
