package listas;

import excepciones.ExcepcionDeListaLlena;
import excepciones.ExcepcionDeListaVacia;

/**
 * Interfaz de lista vista en clase. Los índices son base 0:
 * el primer elemento está en la posición 0 y el último en size() - 1.
 */
public interface Lista<E> {

    public void addLast(E item) throws ExcepcionDeListaLlena;
    public void addFirst(E item) throws ExcepcionDeListaLlena;
    public void addEnesima(E item, int i) throws ExcepcionDeListaLlena;
    public void addOrd(E item) throws ExcepcionDeListaLlena;
    public E removeLast() throws ExcepcionDeListaVacia;
    public E removeFirst() throws ExcepcionDeListaVacia;
    public E removeEnesima(int i) throws ExcepcionDeListaVacia;
    public E removeItem(E item) throws ExcepcionDeListaVacia;
    public E first() throws ExcepcionDeListaVacia;
    public E last() throws ExcepcionDeListaVacia;
    public int size();
    public boolean isEmpty();
    public boolean isFull();
    public E getItem(int i) throws ExcepcionDeListaVacia;
    public int search(E item) throws ExcepcionDeListaVacia;
    public void clear();
}
