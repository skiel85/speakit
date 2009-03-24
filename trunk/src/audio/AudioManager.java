package audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFileFormat;

import datos.capturaaudio.core.SimpleAudioRecorder;
import datos.capturaaudio.exception.SimpleAudioRecorderException;
import datos.reproduccionaudio.core.SimpleAudioPlayer;
import datos.reproduccionaudio.exception.SimpleAudioPlayerException;

public class AudioManager {
	ByteArrayOutputStream output = null;
	SimpleAudioRecorder recorder = null;
	
	
	public void startRecording(){
		try {
			output = new ByteArrayOutputStream();
			recorder = new SimpleAudioRecorder(AudioFileFormat.Type.AU, output  );					
			recorder.init();
			recorder.startRecording( );
		} catch (SimpleAudioRecorderException exc) {
			// resolver la excepcion y lanzar una controlada
		}
		
		
	}
	
	public byte[] stopRecording(){
		if (recorder != null) {
			recorder.stopRecording();
			recorder = null;
			return output.toByteArray();
		}
		return null;
	}
	
	public void play(byte[] sound){
		ByteArrayInputStream input = new ByteArrayInputStream(sound);		
		SimpleAudioPlayer player = new SimpleAudioPlayer(input);
		try {
			player.init();
			player.startPlaying();
			
			Thread.sleep(3000);
			player.stopPlaying();
		} catch (SimpleAudioPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
