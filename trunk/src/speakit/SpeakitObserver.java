package speakit;

public interface SpeakitObserver {

	byte[] getAudio(String text);

	void start();

	void playSound(byte[] audioWord);

	void notifyAlreadyHaveIt(String word);

}
