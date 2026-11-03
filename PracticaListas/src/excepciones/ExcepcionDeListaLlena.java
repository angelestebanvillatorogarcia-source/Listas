package excepciones;

/** Se lanza al intentar agregar un elemento a una lista que ya no tiene espacio. */
public class ExcepcionDeListaLlena extends Exception {
    private static final long serialVersionUID = 1L;

    public ExcepcionDeListaLlena() {
        super("La lista está llena");
    }

    public ExcepcionDeListaLlena(String mensaje) {
        super(mensaje);
    }
}
