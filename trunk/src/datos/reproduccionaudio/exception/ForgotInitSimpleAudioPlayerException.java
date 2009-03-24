package datos.reproduccionaudio.exception;

@SuppressWarnings("serial")
public class ForgotInitSimpleAudioPlayerException extends SimpleAudioPlayerException{

	public ForgotInitSimpleAudioPlayerException(){
		super("ERROR- Forgot to call SimpleAudioPlayer->init()");
	}
}
