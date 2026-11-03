package listas;

import java.util.NoSuchElementException;
import java.util.Objects;

import excepciones.ExcepcionDeListaLlena;
import excepciones.ExcepcionDeListaVacia;

/**
 * Lista en almacenamiento dinámico: nodos enlazados (simplemente ligada)
 * con referencia a la cabeza y a la cola. Nunca se llena. Índices base 0.
 */
public class ListaDinamica<E> implements Lista<E> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> sig;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<E> cabeza;
    private Nodo<E> cola;
    private int tam;

    public ListaDinamica() {
        cabeza = null;
        cola = null;
        tam = 0;
    }

    @Override
    public void addLast(E item) throws ExcepcionDeListaLlena {
        Nodo<E> nuevo = new Nodo<>(item);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            cola.sig = nuevo;
        }
        cola = nuevo;
        tam++;
    }

    @Override
    public void addFirst(E item) throws ExcepcionDeListaLlena {
        Nodo<E> nuevo = new Nodo<>(item);
        nuevo.sig = cabeza;
        cabeza = nuevo;
        if (cola == null) cola = nuevo;
        tam++;
    }

    @Override
    public void addEnesima(E item, int i) throws ExcepcionDeListaLlena {
        if (i < 0 || i > tam) throw new IndexOutOfBoundsException("Posición inválida: " + i);
        if (i == 0) {
            addFirst(item);
        } else if (i == tam) {
            addLast(item);
        } else {
            Nodo<E> ant = nodoEn(i - 1);
            Nodo<E> nuevo = new Nodo<>(item);
            nuevo.sig = ant.sig;
            ant.sig = nuevo;
            tam++;
        }
    }

    /** Inserta en orden ascendente (se asume que la lista ya está ordenada). */
    @Override
    @SuppressWarnings("unchecked")
    public void addOrd(E item) throws ExcepcionDeListaLlena {
        Comparable<E> c = (Comparable<E>) item;
        if (cabeza == null || c.compareTo(cabeza.dato) < 0) {
            addFirst(item);
            return;
        }
        Nodo<E> act = cabeza;
        while (act.sig != null && c.compareTo(act.sig.dato) >= 0) {
            act = act.sig;
        }
        Nodo<E> nuevo = new Nodo<>(item);
        nuevo.sig = act.sig;
        act.sig = nuevo;
        if (act == cola) cola = nuevo;
        tam++;
    }

    @Override
    public E removeFirst() throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        E x = cabeza.dato;
        cabeza = cabeza.sig;
        if (cabeza == null) cola = null;
        tam--;
        return x;
    }

    @Override
    public E removeLast() throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        if (cabeza == cola) return removeFirst();
        Nodo<E> penultimo = cabeza;
        while (penultimo.sig != cola) {
            penultimo = penultimo.sig;
        }
        E x = cola.dato;
        penultimo.sig = null;
        cola = penultimo;
        tam--;
        return x;
    }

    @Override
    public E removeEnesima(int i) throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        validarIndice(i);
        if (i == 0) return removeFirst();
        Nodo<E> ant = nodoEn(i - 1);
        Nodo<E> borrar = ant.sig;
        ant.sig = borrar.sig;
        if (borrar == cola) cola = ant;
        tam--;
        return borrar.dato;
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
        return cabeza.dato;
    }

    @Override
    public E last() throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        return cola.dato;
    }

    @Override
    public int size() {
        return tam;
    }

    @Override
    public boolean isEmpty() {
        return tam == 0;
    }

    /** Una lista dinámica nunca se llena (el límite es la memoria). */
    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public E getItem(int i) throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        validarIndice(i);
        return nodoEn(i).dato;
    }

    /** Devuelve la posición de la primera aparición de item, o -1 si no está. */
    @Override
    public int search(E item) throws ExcepcionDeListaVacia {
        if (isEmpty()) throw new ExcepcionDeListaVacia();
        int pos = 0;
        for (Nodo<E> act = cabeza; act != null; act = act.sig) {
            if (Objects.equals(item, act.dato)) return pos;
            pos++;
        }
        return -1;
    }

    @Override
    public void clear() {
        cabeza = null;
        cola = null;
        tam = 0;
    }

    private Nodo<E> nodoEn(int i) {
        Nodo<E> act = cabeza;
        for (int k = 0; k < i; k++) {
            act = act.sig;
        }
        return act;
    }

    private void validarIndice(int i) {
        if (i < 0 || i >= tam) throw new IndexOutOfBoundsException("Posición inválida: " + i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Nodo<E> act = cabeza; act != null; act = act.sig) {
            if (act != cabeza) sb.append(", ");
            sb.append(act.dato);
        }
        return sb.append("]").toString();
    }
}
