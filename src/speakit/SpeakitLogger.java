package speakit;

import java.util.logging.Logger;

public class SpeakitLogger {
	private static boolean active = false;
	private static Logger logger = Logger.getLogger("Speakit");
	
	public static void Log(String message) {
		if (isActive())
			logger.info(message);
	}

	public static void activate() {
		active = true;
	}
	private static boolean isActive() {
		return active;
	}
}
