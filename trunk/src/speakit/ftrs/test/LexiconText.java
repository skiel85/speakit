package speakit.ftrs.test;

import org.junit.Assert;
import org.junit.Test;

import speakit.ftrs.Lexicon;


public class LexiconText {
	
	@Test
	public void testHas() {
		Lexicon lexicon =  new Lexicon();
		lexicon.add("uno");
		lexicon.add("dos");
		Assert.assertTrue(lexicon.has("uno"));
		Assert.assertTrue(lexicon.has("dos"));
		Assert.assertFalse(lexicon.has("tres"));
		
	}
	
	@Test
	public void testAddOnlyOnceWords() {
		Lexicon lexicon =  new Lexicon();
		lexicon.add("uno");
		lexicon.add("dos");
		lexicon.add("tres");
		lexicon.add("uno");
		Assert.assertTrue(lexicon.size() == 3);
	}
	
	@Test
	public void testAppearanceOrder() {
		Lexicon lexicon =  new Lexicon();
		lexicon.add("uno");
		lexicon.add("dos");
		lexicon.add("tres");
		lexicon.add("uno");
		Assert.assertTrue(lexicon.getAppearanceOrder("uno") == 1);
		Assert.assertTrue(lexicon.getAppearanceOrder("dos") == 2);
		Assert.assertTrue(lexicon.getAppearanceOrder("tres") == 3);
	}

}
