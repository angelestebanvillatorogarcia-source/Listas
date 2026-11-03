package listas;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import excepciones.ExcepcionDeListaLlena;
import excepciones.ExcepcionDeListaVacia;

/**
 * Lista en almacenamiento estático: los elementos viven en un arreglo de
 * capacidad fija. Los índices son base 0.
 */
public class ListaEstatica<E> implements Lista<E> {

    public static final int CAPACIDAD_POR_DEFECTO = 100;

    private final E[] datos;
    private int n; // número de elementos almacenados

    @SuppressWarnings("unchecked")
    public ListaEstatica(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que 0");
        }
        datos = (E[]) new Object[capacidad];
        n = 0;
    }

    public ListaEstatica() {
        this(CAPACIDAD_POR_DEFECTO);
    }

    @Override
    public void addLast(E item) throws ExcepcionDeListaLlena {
        if (isFull()) throw new ExcepcionDeListaLlena();
        datos[n++] = item;
    }

    @Override
    public void addFirst(E item) throws ExcepcionDeListaLlena {
        addEnesima(item, 0);
    }

    @Override
    public void addEnesima(E item, int i) throws ExcepcionDeListaLlena {
        if (isFull()) throw new ExcepcionDeListaLlena();
        if (i < 0 || i > n) throw new IndexOutOfBoundsException("Posición inválida: " + i);
        for (int k = n; k > i; k--) {   // abre hueco recorriendo a la derecha
            datos[k] = datos[k - 1];
        }
        datos[i] = item;
        n++;
    }

    /** Inserta en orden ascendente (se asume que la lista ya está ordenada). */
    @Override
    @SuppressWarnings("unchecked")
    public void addOrd(E item) throws ExcepcionDeListaLlena {
        if (isFull()) throw new ExcepcionDeListaLlena();
        Comparable<E> c = (Comparable<E>) item;
        int pos = 0;
        while (pos < n && c.compareTo(datos[pos]) >= 0) pos++;
        addEnesima(item, pos);
    }

    @Override
    public E removeLast() throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        E x = datos[--n];
        datos[n] = null;
        return x;
    }

    @Override
    public E removeFirst() throws ExcepcionDeListaVacia {
        return removeEnesima(0);
    }

    @Override
    public E removeEnesima(int i) throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        validarIndice(i);
        E x = datos[i];
        for (int k = i; k < n - 1; k++) {   // cierra el hueco recorriendo a la izquierda
            datos[k] = datos[k + 1];
        }
        datos[--n] = null;
        return x;
    }

    /** Elimina la primera aparición de item; si no existe lanza NoSuchElementException. */
    @Override
    public E removeItem(E item) throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        int pos = search(item);
        if (pos < 0) throw new NoSuchElementException("El elemento no está en la lista: " + item);
        return removeEnesima(pos);
    }

    @Override
    public E first() throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        return datos[0];
    }

    @Override
    public E last() throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        return datos[n - 1];
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public boolean isFull() {
        return n == datos.length;
    }

    @Override
    public E getItem(int i) throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        validarIndice(i);
        return datos[i];
    }

    /** Devuelve la posición de la primera aparición de item, o -1 si no está. */
    @Override
    public int search(E item) throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        for (int k = 0; k < n; k++) {
            if (Objects.equals(item, datos[k])) return k;
        }
        return -1;
    }

    @Override
    public void clear() {
        Arrays.fill(datos, 0, n, null);
        n = 0;
    }

    private void validarIndice(int i) {
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException("Posición inválida: " + i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int k = 0; k < n; k++) {
            if (k > 0) sb.append(", ");
            sb.append(datos[k]);
        }
        return sb.append("]").toString();
    }
}
