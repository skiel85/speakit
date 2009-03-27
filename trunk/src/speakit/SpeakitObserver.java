package speakit;

import java.io.IOException;

@Deprecated
public interface SpeakitObserver {

	void start() throws IOException;

	void notifyAlreadyHaveIt(String word);

}
