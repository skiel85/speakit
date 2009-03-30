package speakit.audio;

public class Audio {

	private byte[] bytes;
	private int duration;

	public Audio(byte[] bytes,int duration) {
		this.bytes = bytes;
		this.duration=duration;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
