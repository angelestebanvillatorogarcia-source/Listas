package operaciones;

import excepciones.ExcepcionDeListaLlena;
import excepciones.ExcepcionDeListaVacia;
import listas.Lista;

/**
 * Operaciones de la práctica 3. Todas reciben una o dos listas y DEVUELVEN
 * una lista nueva: la(s) lista(s) original(es) nunca se modifican.
 * Hay dos implementaciones: OperacionesIterativas y OperacionesRecursivas.
 */
public interface Operaciones<E> {

    /** reverse [4,5,3,2,3] => [3,2,3,5,4] */
    Lista<E> reverse(Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** take 3 [4,5,3,2,3,4,1,6] => [4,5,3] */
    Lista<E> take(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** drop 2 [5,6,3,1,2] => [3,1,2] */
    Lista<E> drop(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** init [3,4,5,6,3,2] => [3,4,5,6,3]. Lanza ExcepcionDeListaVacia si l está vacía. */
    Lista<E> init(Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** union [4,5,6] [7,3,2,3] => [4,5,6,7,3,2,3] (concatenación, conserva repetidos). */
    Lista<E> union(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** diferencia [4,5,2,3] [3,4,1,2] => [5] (elementos de a que no están en b, sin repetir). */
    Lista<E> diferencia(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** interseccion [3,4,2,2] [4,1,3] => [3,4] (elementos de a que también están en b, sin repetir). */
    Lista<E> interseccion(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** rotaDerecha 2 [3,4,2,2,5] => [2,5,3,4,2] */
    Lista<E> rotaDerecha(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** rotaIzquierda 3 [3,4,2,2,5] => [2,5,3,4,2] */
    Lista<E> rotaIzquierda(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena;

    /** tieneRepetidos [4,5,3,2,6,7] => false */
    boolean tieneRepetidos(Lista<E> l) throws ExcepcionDeListaVacia;
}
