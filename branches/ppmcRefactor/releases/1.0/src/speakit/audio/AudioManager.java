package speakit.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFileFormat;

import datos.capturaaudio.core.SimpleAudioRecorder;
import datos.capturaaudio.exception.SimpleAudioRecorderException;
import datos.reproduccionaudio.core.SimpleAudioPlayer;
import datos.reproduccionaudio.exception.SimpleAudioPlayerException;

/**
 * Administra la salida y la entrada de audio.
 */
public class AudioManager {
	ByteArrayOutputStream	output		= null;
	SimpleAudioRecorder		recorder	= null;

	/**
	 * Comienza la grabaci�n del audio (as�ncrono).
	 * 
	 * @throws AudioManagerException
	 */
	public void startRecording() throws AudioManagerException {
		try {
			output = new ByteArrayOutputStream();
			recorder = new SimpleAudioRecorder(AudioFileFormat.Type.AU, output);
			recorder.init();
			recorder.startRecording();
		} catch (SimpleAudioRecorderException exc) {
			recorder = null;
			throw new AudioManagerException("No se puede inicializar la grabaci�n. Verifique que el dispositivo de grabaci�n est� correctamente instalado.", exc);
		} catch (Exception ex) {
			recorder = null;
			throw new AudioManagerException("No se puede inicializar la grabaci�n. Verifique que el dispositivo de grabaci�n est� correctamente instalado.", ex);
		}
	}

	/**
	 * Detiene la grabaci�n del audio.
	 * 
	 * @return El arreglo de bytes que contienen el sonido.
	 */
	public byte[] stopRecording() {
		if (recorder != null) {
			recorder.stopRecording();
			recorder = null;
			return output.toByteArray();
		}
		return null;
	}

	/**
	 * Reproduce un audio sincr�nicamente (devuelve el control cuando termina).
	 * 
	 * @param sound
	 *            Audio a reproducir.
	 */
	public void play(byte[] sound) {
		if (sound.length > 0) {
			ByteArrayInputStream input = new ByteArrayInputStream(sound);
			SimpleAudioPlayer player = new SimpleAudioPlayer(input);
			try {
				player.init();
				player.startPlaying();
				player.join();
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
}
