package operaciones;

import java.util.Objects;
import java.util.function.Supplier;

import excepciones.ExcepcionDeListaLlena;
import excepciones.ExcepcionDeListaVacia;
import listas.Lista;

/** Versión iterativa (ciclos for) de las operaciones de la práctica 3. */
public class OperacionesIterativas<E> extends OperacionesBase<E> {

    public OperacionesIterativas(Supplier<Lista<E>> fabrica) {
        super(fabrica);
    }

    @Override
    public Lista<E> reverse(Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        for (int i = 0; i < l.size(); i++) {
            res.addFirst(l.getItem(i));      // cada elemento nuevo pasa al frente
        }
        return res;
    }

    @Override
    public Lista<E> take(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        int limite = Math.min(n, l.size());  // si n <= 0 el ciclo no se ejecuta
        for (int i = 0; i < limite; i++) {
            res.addLast(l.getItem(i));
        }
        return res;
    }

    @Override
    public Lista<E> drop(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        for (int i = Math.max(n, 0); i < l.size(); i++) {
            res.addLast(l.getItem(i));
        }
        return res;
    }

    @Override
    public Lista<E> init(Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        if (l.isEmpty()) throw new ExcepcionDeListaVacia("init no está definida para una lista vacía");
        Lista<E> res = nueva();
        for (int i = 0; i < l.size() - 1; i++) {
            res.addLast(l.getItem(i));
        }
        return res;
    }

    @Override
    public Lista<E> union(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        for (int i = 0; i < a.size(); i++) {
            res.addLast(a.getItem(i));
        }
        for (int i = 0; i < b.size(); i++) {
            res.addLast(b.getItem(i));
        }
        return res;
    }

    @Override
    public Lista<E> diferencia(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        for (int i = 0; i < a.size(); i++) {
            E x = a.getItem(i);
            if (!contiene(b, x) && !contiene(res, x)) {
                res.addLast(x);
            }
        }
        return res;
    }

    @Override
    public Lista<E> interseccion(Lista<E> a, Lista<E> b) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        for (int i = 0; i < a.size(); i++) {
            E x = a.getItem(i);
            if (contiene(b, x) && !contiene(res, x)) {
                res.addLast(x);
            }
        }
        return res;
    }

    /** El elemento que queda en la posición i vino de la posición (i - k) mod tamaño. */
    @Override
    public Lista<E> rotaDerecha(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        int tam = l.size();
        if (tam == 0) return res;
        int k = Math.floorMod(n, tam);       // rotar tam veces = no rotar
        for (int i = 0; i < tam; i++) {
            res.addLast(l.getItem(Math.floorMod(i - k, tam)));
        }
        return res;
    }

    /** El elemento que queda en la posición i vino de la posición (i + k) mod tamaño. */
    @Override
    public Lista<E> rotaIzquierda(int n, Lista<E> l) throws ExcepcionDeListaVacia, ExcepcionDeListaLlena {
        Lista<E> res = nueva();
        int tam = l.size();
        if (tam == 0) return res;
        int k = Math.floorMod(n, tam);
        for (int i = 0; i < tam; i++) {
            res.addLast(l.getItem((i + k) % tam));
        }
        return res;
    }

    @Override
    public boolean tieneRepetidos(Lista<E> l) throws ExcepcionDeListaVacia {
        for (int i = 0; i < l.size() - 1; i++) {
            E x = l.getItem(i);
            for (int j = i + 1; j < l.size(); j++) {
                if (Objects.equals(x, l.getItem(j))) return true;
            }
        }
        return false;
    }
}
