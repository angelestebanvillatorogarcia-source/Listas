package excepciones;

/** Se lanza al intentar consultar o eliminar elementos de una lista vacía. */
public class ExcepcionDeListaVacia extends Exception {
    private static final long serialVersionUID = 1L;

    public ExcepcionDeListaVacia() {
        super("La lista está vacía");
    }

    public ExcepcionDeListaVacia(String mensaje) {
        super(mensaje);
    }
}
