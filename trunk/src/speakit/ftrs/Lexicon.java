package speakit.ftrs;

import java.util.ArrayList;
import java.util.Iterator;

import speakit.ftrs.index.Term;

public class Lexicon {

	protected ArrayList<Term> terms;
	
	
	
	public Lexicon() {
		terms = new ArrayList<Term>();
	}


	public void add(String word) {
		if (!this.has(word)) {
			int order = getNextAppearanceNumber();
			Term newTerm = new Term(order, word);
			terms.add(newTerm);
		}
	}
	

	private int getNextAppearanceNumber() {
		return terms.size() + 1;
	}
	
	public boolean has(String word) {
		Term tempTerm = new Term(-1, word);
		return terms.contains(tempTerm);
	}


	public int size() {
		return terms.size();
	}


	public Iterator<Term> iterator() {
		return terms.iterator();
	}
	
	public int getAppearanceOrder(String word) {
		Term tempTerm = new Term(-1, word);
		int index = terms.indexOf(tempTerm);
		return terms.get(index).getTermId();
	}
}
