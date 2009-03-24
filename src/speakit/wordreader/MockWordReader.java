package speakit.wordreader;

import java.util.ArrayList;
import java.util.Arrays;

import org.jcp.xml.dsig.internal.dom.Utils;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/**
 * Clase Dummy q implementa WordREader
 * @author 
 *
 */
public class MockWordReader implements WordReader {
	int count;
	 
	String[] words;
	int it=0;
	public MockWordReader() { 
//		words  = "puede estar temporalmente inactiva o tal vez se haya trasladado definitivamente a una nueva".split(" ");
		words  = "puede estar temporalmente no si".split(" ");
	}
	@Override
	public boolean hasNext() {
		return it< words.length;
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
		return words[it++];
	}

}
