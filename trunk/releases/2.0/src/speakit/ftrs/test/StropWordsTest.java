package speakit.ftrs.test;

import junit.framework.Assert;

import org.junit.Test;

import speakit.ftrs.StopWords;

public class StropWordsTest {

	@Test
	public void testGetStopWords(){
		StopWords stopWords = new StopWords();
		Assert.assertTrue(stopWords.getStopWords().contains("un"));
		Assert.assertTrue(stopWords.getStopWords().contains("una"));
		Assert.assertTrue(stopWords.getStopWords().contains("tambien"));
		Assert.assertTrue(stopWords.getStopWords().contains("estamos"));
		
	}
}
