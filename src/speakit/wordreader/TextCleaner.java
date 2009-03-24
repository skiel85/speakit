package speakit.wordreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextCleaner {
	public String replaceStrangeCharacters(String text) {
		Pattern p = Pattern.compile("[^a-z0-9ביםףתאטלעשגךמפûסüח]");
		Matcher m = p.matcher(text.toLowerCase());
		return m.replaceAll(" ");
	}

	public String collapseSpaces(String text) {
		Pattern p = Pattern.compile(" +");
		Matcher m = p.matcher(text.toLowerCase());
		return m.replaceAll(" ").trim();
	}

	public String cleanText(String text) {
		return this.collapseSpaces(this.replaceStrangeCharacters(text));
	}

	public String[] getWords(String text) {
		return this.cleanText(text).split(" ");
	}
}
