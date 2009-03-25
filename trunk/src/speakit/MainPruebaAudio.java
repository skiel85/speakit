package speakit;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;

import datos.capturaaudio.core.SimpleAudioRecorder;
import datos.capturaaudio.exception.SimpleAudioRecorderException;
import datos.reproduccionaudio.core.SimpleAudioPlayer;
import datos.reproduccionaudio.exception.SimpleAudioPlayerException;

public class MainPruebaAudio {


/**
 * Prueba el audio
 * @param args
 * @throws SimpleAudioRecorderException
 * @throws InterruptedException
 * @throws SimpleAudioPlayerException
 * @throws IOException
 */
	public static void main(String[] args) throws SimpleAudioRecorderException, InterruptedException, SimpleAudioPlayerException, IOException {
		String soundFilePath = "prueba.wav";
		FileOutputStream file = new FileOutputStream(soundFilePath); 
		
		SimpleAudioRecorder recorder = new SimpleAudioRecorder(AudioFileFormat.Type.AU, file  );
		
		recorder.init();
		recorder.startRecording( );
		Thread.sleep(3000);
		recorder.stopRecording();
		file.flush();
		file.close(); 
		
		InputStream stream=new FileInputStream(soundFilePath); 
		BufferedInputStream fstream = new BufferedInputStream(stream);
		
		SimpleAudioPlayer player = new SimpleAudioPlayer(fstream);
		player.init();
		player.startPlaying();
		Thread.sleep(3000);
		player.stopPlaying();
	}

}
