package datos.capturaaudio.exception;

@SuppressWarnings("serial")
public class ForgotInitSimpleAudioRecorderException extends SimpleAudioRecorderException {
	
	public ForgotInitSimpleAudioRecorderException(){
		super("ERROR- Forgot to call SimpleAudioRecorder->init()");
	}
}
