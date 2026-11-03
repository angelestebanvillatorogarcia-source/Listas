package operaciones;

import java.util.Objects;
import java.util.function.Supplier;

import excepciones.ExcepcionDeListaLlena;
import excepciones.ExcepcionDeListaVacia;
import listas.Lista;

/**
 * Versión recursiva de las operaciones de la práctica 3.
 * No hay ciclos: cada método auxiliar tiene un caso base y un paso recursivo
 * que avanza un índice sobre la lista original (que nunca se modifica).
 */
public class OperacionesRecursivas<E> extends OperacionesBase<E> {

    public OperacionesRecursivas(Supplier<Lista<E>> fabrica) {
        super(fabrica);
    }

    // ---------- Auxiliar recursivo reutilizado por varias operaciones ----------

    /** Copia src[desde .. hasta-1] al final de dst. Caso base: desde >= hasta. */
    private void copiarRango(Lista<E> src, int desde, int hasta, Lista<E> dst)
            throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        if (desde >= hasta) return;                 // caso base
        dst.addLast(src.getItem(desde));
        copiarRango(src, desde + 1, hasta, dst);    // paso recursivo
    }

    // ---------- A) reverse ----------

    @Override
    public Lista<E> reverse(Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        reverseRec(l, l.size() - 1, res);
        return res;
    }

    /** Recorre l de atrás hacia adelante agregando cada elemento al final de res. */
    private void reverseRec(Lista<E> l, int i, Lista<E> res)
            throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        if (i < 0) return;                          // caso base
        res.addLast(l.getItem(i));
        reverseRec(l, i - 1, res);
    }

    // ---------- B) take, C) drop, D) init ----------

    @Override
    public Lista<E> take(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        copiarRango(l, 0, Math.min(n, l.size()), res);
        return res;
    }

    @Override
    public Lista<E> drop(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        copiarRango(l, Math.max(n, 0), l.size(), res);
        return res;
    }

    @Override
    public Lista<E> init(Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        if (l.isEmpty()) throw new ExcepcionDeListaVacia("init no está definida para una lista vacía");
        Lista<E> res = nueva();
        copiarRango(l, 0, l.size() - 1, res);
        return res;
    }

    // ---------- E) union ----------

    @Override
    public Lista<E> union(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        copiarRango(a, 0, a.size(), res);
        copiarRango(b, 0, b.size(), res);
        return res;
    }

    // ---------- F) diferencia ----------

    @Override
    public Lista<E> diferencia(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        diferenciaRec(a, b, 0, res);
        return res;
    }

    private void diferenciaRec(Lista<E> a, Lista<E> b, int i, Lista<E> res)
            throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        if (i >= a.size()) return;                  // caso base
        E x = a.getItem(i);
        if (!contiene(b, x) && !contiene(res, x)) {
            res.addLast(x);
        }
        diferenciaRec(a, b, i + 1, res);
    }

    // ---------- G) interseccion ----------

    @Override
    public Lista<E> interseccion(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        interseccionRec(a, b, 0, res);
        return res;
    }

    private void interseccionRec(Lista<E> a, Lista<E> b, int i, Lista<E> res)
            throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        if (i >= a.size()) return;                  // caso base
        E x = a.getItem(i);
        if (contiene(b, x) && !contiene(res, x)) {
            res.addLast(x);
        }
        interseccionRec(a, b, i + 1, res);
    }

    // ---------- H) rotaDerecha, I) rotaIzquierda ----------
    // Igual que en Haskell:  rotaIzquierda k xs = drop k xs ++ take k xs
    //                        rotaDerecha   k xs = drop (n-k) xs ++ take (n-k) xs

    @Override
    public Lista<E> rotaDerecha(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        int tam = l.size();
        if (tam == 0) return res;
        int corte = tam - Math.floorMod(n, tam);
        copiarRango(l, corte, tam, res);            // los últimos k pasan al frente
        copiarRango(l, 0, corte, res);
        return res;
    }

    @Override
    public Lista<E> rotaIzquierda(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        int tam = l.size();
        if (tam == 0) return res;
        int corte = Math.floorMod(n, tam);
        copiarRango(l, corte, tam, res);            // lo que queda después de los k primeros
        copiarRango(l, 0, corte, res);              // y los k primeros al final
        return res;
    }

    // ---------- J) tieneRepetidos ----------

    @Override
    public boolean tieneRepetidos(Lista<E> l) throws ExcepcionDeListaVacia {
        return repetidosDesde(l, 0);
    }

    /** ¿Hay algún elemento repetido en l[i .. fin]? */
    private boolean repetidosDesde(Lista<E> l, int i) throws ExcepcionDeListaVacia {
        if (i >= l.size() - 1) return false;        // caso base: ya no hay con quién comparar
        if (aparece(l, l.getItem(i), i + 1)) return true;
        return repetidosDesde(l, i + 1);
    }

    /** ¿Aparece x en l[j .. fin]? */
    private boolean aparece(Lista<E> l, E x, int j) throws ExcepcionDeListaVacia {
        if (j >= l.size()) return false;            // caso base
        return Objects.equals(x, l.getItem(j)) || aparece(l, x, j + 1);
    }
}
