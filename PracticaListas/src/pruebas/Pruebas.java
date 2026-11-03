package pruebas;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import excepciones.ExcepcionDeListaLlena;
import excepciones.ExcepcionDeListaVacia;
import listas.Lista;
import listas.ListaDinamica;
import listas.ListaEstatica;
import operaciones.Operaciones;
import operaciones.OperacionesIterativas;
import operaciones.OperacionesRecursivas;

/**
 * Programa de pruebas de la práctica 3. Ejecuta cada operación con las dos
 * implementaciones de lista (estática y dinámica) y los dos estilos
 * (iterativo y recursivo), y comprueba que las listas originales no cambian.
 */
public class Pruebas {

    private static int total = 0;
    private static int fallos = 0;

    // ---------------------------------------------------------------- utilerías

    private static Lista<Integer> crear(Supplier<Lista<Integer>> fab, Integer... xs)
            throws ExcepcionDeListaLlena {
        Lista<Integer> l = fab.get();
        for (Integer x : xs) l.addLast(x);
        return l;
    }

    /** "Foto" del contenido de listas para comprobar que no se modificaron. */
    private static String foto(Lista<?>... listas) {
        StringBuilder sb = new StringBuilder();
        for (Lista<?> l : listas) sb.append(l).append('|');
        return sb.toString();
    }

    private static void caso(String nombre, String obtenido, String esperado, String antes, String despues) {
        total++;
        boolean ok = obtenido.equals(esperado) && antes.equals(despues);
        if (!ok) fallos++;
        String extra = antes.equals(despues) ? "" : "  (¡la lista original cambió: " + antes + " -> " + despues + "!)";
        System.out.println((ok ? "  [OK]    " : "  [FALLO] ") + nombre + " => " + obtenido
                + (ok ? "" : "   (esperado " + esperado + ")") + extra);
    }

    private static void caso(String nombre, String obtenido, String esperado) {
        caso(nombre, obtenido, esperado, "", "");
    }

    // ------------------------------------------------- pruebas de la propia lista

    private static void pruebasLista(String nombre, Supplier<Lista<Integer>> fab) throws Exception {
        System.out.println("\n=== Operaciones básicas de la lista " + nombre + " ===");
        String p = "[" + nombre + "] ";

        Lista<Integer> l = fab.get();
        l.addLast(1);
        l.addFirst(0);
        l.addEnesima(5, 1);
        caso(p + "addLast/addFirst/addEnesima", l.toString(), "[0, 5, 1]");
        caso(p + "first, last, size", l.first() + "," + l.last() + "," + l.size(), "0,1,3");
        caso(p + "search(1), search(99)", l.search(1) + "," + l.search(99), "2,-1");
        caso(p + "getItem(1)", String.valueOf(l.getItem(1)), "5");

        l.clear();
        for (int x : new int[] {1, 3, 5}) l.addOrd(x);
        l.addOrd(4);
        l.addOrd(0);
        l.addOrd(9);
        l.addOrd(3);
        caso(p + "addOrd", l.toString(), "[0, 1, 3, 3, 4, 5, 9]");

        caso(p + "removeFirst", String.valueOf(l.removeFirst()), "0");
        caso(p + "removeLast", String.valueOf(l.removeLast()), "9");
        caso(p + "removeEnesima(1)", String.valueOf(l.removeEnesima(1)), "3");
        caso(p + "removeItem(4)", String.valueOf(l.removeItem(4)), "4");
        caso(p + "contenido final", l.toString(), "[1, 3, 5]");

        // la cola debe seguir apuntando al último elemento (importante en la dinámica)
        l.removeLast();
        l.addLast(9);
        l.removeEnesima(l.size() - 1);
        l.addLast(7);
        caso(p + "last() tras quitar/agregar al final", l.last() + " " + l, "7 [1, 3, 7]");

        try {
            l.removeItem(99);
            caso(p + "removeItem inexistente", "sin excepción", "NoSuchElementException");
        } catch (NoSuchElementException e) {
            caso(p + "removeItem inexistente", "NoSuchElementException", "NoSuchElementException");
        }

        l.clear();
        caso(p + "clear / isEmpty", l.size() + "," + l.isEmpty(), "0,true");
        try {
            l.removeFirst();
            caso(p + "removeFirst en vacía", "sin excepción", "ExcepcionDeListaVacia");
        } catch (ExcepcionDeListaVacia e) {
            caso(p + "removeFirst en vacía", "ExcepcionDeListaVacia", "ExcepcionDeListaVacia");
        }
    }

    private static void pruebaListaLlena() throws Exception {
        System.out.println("\n=== Lista estática llena ===");
        Lista<Integer> l = new ListaEstatica<>(3);
        l.addLast(1);
        l.addLast(2);
        l.addLast(3);
        caso("[Estática] isFull con 3 de 3", String.valueOf(l.isFull()), "true");
        try {
            l.addLast(4);
            caso("[Estática] addLast en lista llena", "sin excepción", "ExcepcionDeListaLlena");
        } catch (ExcepcionDeListaLlena e) {
            caso("[Estática] addLast en lista llena", "ExcepcionDeListaLlena", "ExcepcionDeListaLlena");
        }
        caso("[Dinámica] isFull", String.valueOf(new ListaDinamica<Integer>().isFull()), "false");

        // si la fábrica crea listas muy chicas, el resultado de una operación puede no caber
        Supplier<Lista<Integer>> chica = () -> new ListaEstatica<>(4);
        List<Operaciones<Integer>> ops = List.of(new OperacionesIterativas<Integer>(chica),
                                                 new OperacionesRecursivas<Integer>(chica));
        String[] estilos = {"iterativa", "recursiva"};
        for (int k = 0; k < ops.size(); k++) {
            Lista<Integer> a = crear(chica, 1, 2, 3);
            Lista<Integer> b = crear(chica, 4);
            Lista<Integer> c = crear(() -> new ListaEstatica<>(10), 5, 6);
            try {
                ops.get(k).union(a, c);
                caso("[Estática] union que no cabe (" + estilos[k] + ")", "sin excepción", "ExcepcionDeListaLlena");
            } catch (ExcepcionDeListaLlena e) {
                caso("[Estática] union que no cabe (" + estilos[k] + ")", "ExcepcionDeListaLlena", "ExcepcionDeListaLlena");
            }
        }
    }

    // ------------------------------------------- pruebas de las 10 operaciones

    private static void pruebasOperaciones(String almacenamiento, String estilo,
                                           Operaciones<Integer> op,
                                           Supplier<Lista<Integer>> fab) throws Exception {
        System.out.println("\n=== " + almacenamiento + " / " + estilo + " ===");
        String p = "[" + almacenamiento.substring(0, 3) + "/" + estilo.substring(0, 3) + "] ";
        Lista<Integer> l, a, b;
        String antes;

        // A) reverse
        l = crear(fab, 4, 5, 3, 2, 3); antes = foto(l);
        caso(p + "reverse [4,5,3,2,3]", op.reverse(l).toString(), "[3, 2, 3, 5, 4]", antes, foto(l));
        l = crear(fab);
        caso(p + "reverse []", op.reverse(l).toString(), "[]");
        l = crear(fab, 7);
        caso(p + "reverse [7]", op.reverse(l).toString(), "[7]");

        // B) take
        l = crear(fab, 4, 5, 3, 2, 3, 4, 1, 6); antes = foto(l);
        caso(p + "take 3 [4,5,3,2,3,4,1,6]", op.take(3, l).toString(), "[4, 5, 3]", antes, foto(l));
        caso(p + "take 0", op.take(0, l).toString(), "[]");
        caso(p + "take -1", op.take(-1, l).toString(), "[]");
        caso(p + "take 20 (más que el tamaño)", op.take(20, l).toString(), "[4, 5, 3, 2, 3, 4, 1, 6]");

        // C) drop
        l = crear(fab, 5, 6, 3, 1, 2); antes = foto(l);
        caso(p + "drop 2 [5,6,3,1,2]", op.drop(2, l).toString(), "[3, 1, 2]", antes, foto(l));
        caso(p + "drop 0", op.drop(0, l).toString(), "[5, 6, 3, 1, 2]");
        caso(p + "drop -3", op.drop(-3, l).toString(), "[5, 6, 3, 1, 2]");
        caso(p + "drop 10 (más que el tamaño)", op.drop(10, l).toString(), "[]");

        // D) init
        l = crear(fab, 3, 4, 5, 6, 3, 2); antes = foto(l);
        caso(p + "init [3,4,5,6,3,2]", op.init(l).toString(), "[3, 4, 5, 6, 3]", antes, foto(l));
        caso(p + "init [9]", op.init(crear(fab, 9)).toString(), "[]");
        try {
            op.init(crear(fab));
            caso(p + "init []", "sin excepción", "ExcepcionDeListaVacia");
        } catch (ExcepcionDeListaVacia e) {
            caso(p + "init []", "ExcepcionDeListaVacia", "ExcepcionDeListaVacia");
        }

        // E) union
        a = crear(fab, 4, 5, 6); b = crear(fab, 7, 3, 2, 3); antes = foto(a, b);
        caso(p + "union [4,5,6] [7,3,2,3]", op.union(a, b).toString(), "[4, 5, 6, 7, 3, 2, 3]", antes, foto(a, b));
        caso(p + "union [] [1]", op.union(crear(fab), crear(fab, 1)).toString(), "[1]");
        caso(p + "union [] []", op.union(crear(fab), crear(fab)).toString(), "[]");

        // F) diferencia
        a = crear(fab, 4, 5, 2, 3); b = crear(fab, 3, 4, 1, 2); antes = foto(a, b);
        caso(p + "diferencia [4,5,2,3] [3,4,1,2]", op.diferencia(a, b).toString(), "[5]", antes, foto(a, b));
        caso(p + "diferencia [1,1,2] []", op.diferencia(crear(fab, 1, 1, 2), crear(fab)).toString(), "[1, 2]");
        caso(p + "diferencia [] [1]", op.diferencia(crear(fab), crear(fab, 1)).toString(), "[]");
        caso(p + "diferencia [1,2] [2,1]", op.diferencia(crear(fab, 1, 2), crear(fab, 2, 1)).toString(), "[]");

        // G) interseccion
        a = crear(fab, 3, 4, 2, 2); b = crear(fab, 4, 1, 3); antes = foto(a, b);
        caso(p + "interseccion [3,4,2,2] [4,1,3]", op.interseccion(a, b).toString(), "[3, 4]", antes, foto(a, b));
        caso(p + "interseccion [1,1,2] [1]", op.interseccion(crear(fab, 1, 1, 2), crear(fab, 1)).toString(), "[1]");
        caso(p + "interseccion [1] []", op.interseccion(crear(fab, 1), crear(fab)).toString(), "[]");
        caso(p + "interseccion [] [1]", op.interseccion(crear(fab), crear(fab, 1)).toString(), "[]");

        // H) rotaDerecha
        l = crear(fab, 3, 4, 2, 2, 5); antes = foto(l);
        caso(p + "rotaDerecha 2 [3,4,2,2,5]", op.rotaDerecha(2, l).toString(), "[2, 5, 3, 4, 2]", antes, foto(l));
        caso(p + "rotaDerecha 0", op.rotaDerecha(0, l).toString(), "[3, 4, 2, 2, 5]");
        caso(p + "rotaDerecha 5 (vuelta completa)", op.rotaDerecha(5, l).toString(), "[3, 4, 2, 2, 5]");
        caso(p + "rotaDerecha 7 (= 2)", op.rotaDerecha(7, l).toString(), "[2, 5, 3, 4, 2]");
        caso(p + "rotaDerecha -1 (= izquierda 1)", op.rotaDerecha(-1, l).toString(), "[4, 2, 2, 5, 3]");
        caso(p + "rotaDerecha 3 []", op.rotaDerecha(3, crear(fab)).toString(), "[]");

        // I) rotaIzquierda
        l = crear(fab, 3, 4, 2, 2, 5); antes = foto(l);
        caso(p + "rotaIzquierda 3 [3,4,2,2,5]", op.rotaIzquierda(3, l).toString(), "[2, 5, 3, 4, 2]", antes, foto(l));
        caso(p + "rotaIzquierda 1", op.rotaIzquierda(1, l).toString(), "[4, 2, 2, 5, 3]");
        caso(p + "rotaIzquierda 0", op.rotaIzquierda(0, l).toString(), "[3, 4, 2, 2, 5]");
        caso(p + "rotaIzquierda 8 (= 3)", op.rotaIzquierda(8, l).toString(), "[2, 5, 3, 4, 2]");
        caso(p + "rotaIzquierda 3 []", op.rotaIzquierda(3, crear(fab)).toString(), "[]");

        // J) tieneRepetidos
        l = crear(fab, 4, 5, 3, 2, 6, 7); antes = foto(l);
        caso(p + "tieneRepetidos [4,5,3,2,6,7]", String.valueOf(op.tieneRepetidos(l)), "false", antes, foto(l));
        caso(p + "tieneRepetidos [4,5,3,4]", String.valueOf(op.tieneRepetidos(crear(fab, 4, 5, 3, 4))), "true");
        caso(p + "tieneRepetidos [1,2,2]", String.valueOf(op.tieneRepetidos(crear(fab, 1, 2, 2))), "true");
        caso(p + "tieneRepetidos [5]", String.valueOf(op.tieneRepetidos(crear(fab, 5))), "false");
        caso(p + "tieneRepetidos []", String.valueOf(op.tieneRepetidos(crear(fab))), "false");
    }

    // ------------------------------------------------------------------- main

    public static void main(String[] args) throws Exception {
        Supplier<Lista<Integer>> estatica = () -> new ListaEstatica<>(50);
        Supplier<Lista<Integer>> dinamica = ListaDinamica::new;

        pruebasLista("Estática", estatica);
        pruebasLista("Dinámica", dinamica);
        pruebaListaLlena();

        pruebasOperaciones("Estática", "Iterativa", new OperacionesIterativas<>(estatica), estatica);
        pruebasOperaciones("Estática", "Recursiva", new OperacionesRecursivas<>(estatica), estatica);
        pruebasOperaciones("Dinámica", "Iterativa", new OperacionesIterativas<>(dinamica), dinamica);
        pruebasOperaciones("Dinámica", "Recursiva", new OperacionesRecursivas<>(dinamica), dinamica);

        System.out.println("\n----------------------------------------");
        System.out.println("Pruebas ejecutadas: " + total + "   Fallos: " + fallos);
        System.exit(fallos == 0 ? 0 : 1);
    }
}
