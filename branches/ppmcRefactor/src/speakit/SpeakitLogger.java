package speakit;

import java.util.logging.Logger;

public class SpeakitLogger {
	private static boolean	active	= false;
	private static Logger	logger	= Logger.getLogger("Speakit");

	public static void Log(String message) {
		if (isActive())
			logger.info(message);
	}

	public static void Log(char message) {
		Log(new Character(message).toString());
	}
	
	public static void Log(Object obj) {
		Log(obj.toString());
	}

	public static void activate() {
		active = true;
	}
	
	public static void deactivate() {
		active = false;
	}
	
	private static boolean isActive() {
		return active;
	}
}
