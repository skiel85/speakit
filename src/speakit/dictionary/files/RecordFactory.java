package speakit.dictionary.files;

/**
 * F�brica de registros. La clases que implementan esta interfaz, tienen un
 * m�todo para crear un registro, es decir, un objeto que implemente Record.
 */
public interface RecordFactory<RECTYPE> {
	/**
	 * Crea un registro.
	 * 
	 * @return Registro nuevo.
	 */
	public RECTYPE createRecord();
}
