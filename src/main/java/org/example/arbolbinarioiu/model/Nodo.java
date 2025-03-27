package org.example.arbolbinarioiu.model;

public class Nodo {
    private int valor;
    private Nodo izq, der;

    public Nodo(int valor) {
        this.valor = valor;
        izq = null;
        der = null;
    }
    public int getValor() {
        return valor;
    }
    public void setValor(int valor) {
        this.valor = valor;
    }
    public Nodo getIzq() {
        return izq;
    }
    public void setIzq(Nodo izq) {
        this.izq = izq;
    }
    public Nodo getDer() {
        return der;
    }
    public void setDer(Nodo der) {
        this.der = der;
    }
}