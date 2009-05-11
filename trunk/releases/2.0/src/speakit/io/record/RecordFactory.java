package speakit.io.record;

/**
 * F�brica de registros. La clases que implementan esta interfaz, tienen un
 * m�todo para crear un registro, es decir, un objeto que implemente Record.
 */
public interface RecordFactory {
	/**
	 * Crea un registro.
	 * 
	 * @return Registro nuevo.
	 */
	public Record createRecord();
}
