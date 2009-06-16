package speakit.compression.lzp.test;

import speakit.TextDocument;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.Symbol;

public class TextDocumentBuilder {
	StringBuilder builder;
	public TextDocumentBuilder() {
		builder = new StringBuilder();
	}
	
	public String getMatch(Integer matchPos, Integer matchLength) {
		return builder.substring(matchPos, matchPos + matchLength);
	}


	public void add(String match) {
		builder.append(match);
	}


	public void add(Symbol symbol) {
		if (symbol.isDecodificable()) {
			builder.append(symbol.getChar());
		}
	}

	public Context getContext(Integer length) {
		Context context = new Context(length);
		if (this.builder.length() >= length) {
			for (int i = this.builder.length() - length; i < this.builder.length(); i++) {
				char ch = this.builder.charAt(i);
				context.add(new Symbol(ch));
			}
		}
		return context;
	}

	public TextDocument getDocument() {
		return new TextDocument(this.builder.toString());
	}

	public Integer getPosition() {
		return builder.length() - 1;
	}
}
