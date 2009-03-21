package speakit.dictionary;

public class ByteArrayConverter {
	/**	
	 * Convierte un array de bytes a un int. Cada byte del array formar� parte del int, 
	 * el 1er byte ser� el que ocupe los primeros 8 bits del int, el segundo ocupar� los proximos 8 y as� sucesivamente.
	 * @param b
	 * @return
	 */
	 public int toInt(byte[] b) {
        int value = 0;
     
        value+=(b[3])<<0; //0000 0000|0000 0000|0000 0000|XXXX XXXX
        value+=(b[2])<<8; //0000 0000|0000 0000|XXXX XXXX|0000 0000  
        value+=(b[1])<<16;//0000 0000|XXXX XXXX|0000 0000|0000 0000
        value+=(b[0])<<24;//XXXX XXXX|0000 0000|0000 0000|0000 0000
        return value;
    }
}
