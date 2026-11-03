package operaciones;

import java.util.Objects;
import java.util.function.Supplier;

import excepciones.ExcepcionDeListaVacia;
import listas.Lista;

/**
 * Código común a las versiones iterativa y recursiva. Recibe una "fábrica"
 * que sabe crear listas vacías, así las operaciones funcionan igual con
 * ListaEstatica que con ListaDinamica:
 *
 *   new OperacionesIterativas<>(() -> new ListaEstatica<>(50));
 *   new OperacionesRecursivas<>(ListaDinamica::new);
 */
public abstract class OperacionesBase<E> implements Operaciones<E> {

    private final Supplier<Lista<E>> fabrica;

    protected OperacionesBase(Supplier<Lista<E>> fabrica) {
        this.fabrica = Objects.requireNonNull(fabrica, "La fábrica de listas no puede ser null");
    }

    /** Crea una lista vacía nueva para guardar el resultado. */
    protected Lista<E> nueva() {
        return fabrica.get();
    }

    /** true si x aparece en l (search lanza excepción con lista vacía, aquí se evita). */
    protected boolean contiene(Lista<E> l, E x) throws ExcepcionDeListaVacia {
        return !l.isEmpty() && l.search(x) >= 0;
    }
}
