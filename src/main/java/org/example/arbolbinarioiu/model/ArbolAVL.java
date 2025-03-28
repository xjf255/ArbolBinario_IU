package org.example.arbolbinarioiu.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArbolAVL extends ArbolBinario {

    public ArbolAVL() {}

    public void balance(ArbolBinario ab) {
        if (ab == null || ab.getAlt() == 0) {
            System.out.println("No se puede balancear un árbol vacío");
            return;
        }

        String inOrder = ab.recorridoInOrder();

        List<Integer> inOrderList = Arrays.stream(inOrder.split(","))
                .map(String::trim) // Elimina espacios en blanco
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        ab.setRaiz(construirAVL(new ArrayList<>(inOrderList), 0, inOrderList.size() - 1));
    }

    private Nodo construirAVL(ArrayList<Integer> lista, int inicio, int fin) {
        if (inicio > fin) return null;

        int medio = (inicio + fin) / 2;
        Nodo nodo = new Nodo(lista.get(medio));

        nodo.setIzq(construirAVL(lista, inicio, medio - 1));
        nodo.setDer(construirAVL(lista, medio + 1, fin));

        return nodo;
    }
}
