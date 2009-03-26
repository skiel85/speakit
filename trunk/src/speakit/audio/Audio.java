package speakit.audio;

public class Audio {
   
	private   byte[] bytes;
	private   long duration;

	public Audio(byte[] bytes, long duration) {
		this.bytes = bytes;
		this.duration = duration; 
	}

	public byte[] getBytes() {
		return this.bytes; 
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
