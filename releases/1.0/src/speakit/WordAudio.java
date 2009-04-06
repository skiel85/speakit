package speakit;

import speakit.audio.Audio;

public class WordAudio {

	private Audio audio;
	private String word;

	public WordAudio(String word, Audio audio) {
		this.word = word;
		this.audio = audio;
	}

	public void setAudio(Audio audio) {
		this.audio = audio;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

}
