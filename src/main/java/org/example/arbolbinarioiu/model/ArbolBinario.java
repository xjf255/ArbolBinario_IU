package org.example.arbolbinarioiu.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArbolBinario {
    private Nodo raiz;
    private int alt;
    public ArbolBinario() {
        raiz = null;
        alt = 0;
    }

    public boolean insert(int valor) {
        if (search(raiz, valor, 1) != 0) return false;
        raiz = insert(raiz, valor,1);
        return true;
    }
    private Nodo insert (Nodo root, int value,int currentAlt) {
        if(!validRoot(root)) {
            alt = Math.max(alt, currentAlt);
            return new Nodo(value);
        }
        if(value < root.getValor()){
            root.setIzq(insert(root.getIzq(), value, currentAlt+1));
        }
        else if(value > root.getValor()) {
            root.setDer(insert(root.getDer(), value, currentAlt+1));
        }
        return root;
    }

    public void remove(int valor) {
        raiz = remove(raiz, valor);
    }

    private Nodo remove(Nodo root, int value) {
        if(!validRoot(root)) {
            return null;
        }
        System.out.println( root.getValor() + "," + value);

        if(value == root.getValor()) {
            if(root.getIzq() == null  && root.getDer() == null) {
                return null;
            } else if(root.getIzq() != null && root.getDer() == null) {
                root = root.getIzq();
            } else if(root.getIzq() == null && root.getDer() != null) {
                root = root.getDer();
            } else {
                root.setValor(compatibleNodo(root.getDer()));
                root.setDer(remove(root.getDer(),root.getValor()));
            }
        }
        if(value > root.getValor()) {
            root.setDer(remove(root.getDer(), value));
        }else if(value < root.getValor()) {
            root.setIzq(remove(root.getIzq(), value));
        } return root;
    }

    //todo: buscar un nodo compatible
    private Integer compatibleNodo (Nodo root) {
        int minv = root.getValor();
        while (root.getIzq() != null) {
            minv = root.getIzq().getValor();
            root = root.getIzq();
        }
        return minv;
    }

    // search
    public String search( int value) {
        if(raiz == null) {
            return "Nodo no encontrado";
        }
        int level =  search(raiz,value,1);
        if(level == 0) {
            return "Nodo no encontrado";
        }
        return "Nodo encontrado en el nivel: " + level;
    }

    private Integer search(Nodo root, int value,int level) {
        if(validRoot(root)) {
            if(value == root.getValor()) {
                return level;
            }
            if(value < root.getValor()) return search(root.getIzq(),value,level+1);
            if(value > root.getValor()) return search(root.getDer(),value, level +1);
        }
        return 0;
    }
    // root => izq => der
    public String recorridoPreOrder() {
        if(raiz == null) {
            System.out.println("Recorrido no encontrado");
            return "Recorrido no encontrado en PreOrden";
        }
        String recorrido = recorridoPreOrder(raiz);
        return  "Recorrido PreOrden : " + recorrido;
    }
    private String recorridoPreOrder(Nodo root) {
        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.getValor());
        String left = recorridoPreOrder(root.getIzq());
        String right = recorridoPreOrder(root.getDer());

        if (!left.isEmpty()) {
            sb.append(",").append(left);
        }
        if (!right.isEmpty()) {
            sb.append(",").append(right);
        }

        return sb.toString();
    }

    // izq, der, root
    public String recorridoPostOrder() {
        if(raiz == null) {
            System.out.println("Recorrido no encontrado");
            return "Recorrido no encontrado en PostOrden";
        }
        String postOrder = recorridoPostOrder(raiz);
        return  "Recorrido PostOrden : " + postOrder;
    }
    private String recorridoPostOrder(Nodo root) {
        if (root == null) {
            return "";
        }
        String left = recorridoPostOrder(root.getIzq());
        String right = recorridoPostOrder(root.getDer());
        StringBuilder sb = new StringBuilder();
        if (!left.isEmpty()) {
            sb.append(left);
        }
        if (!right.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(right);
        }
        if (sb.length() > 0) {
            sb.append(",");
        }
        sb.append(root.getValor());
        return sb.toString();
    }

    // izq => root => der
    public String recorridoInOrder() {
        if(raiz == null) {
            System.out.println("Recorrido no encontrado en InOrden");
            return "Recorrido no encontrado en InOrden";
        }
        String inOrder = recorridoInOrder(raiz);
        System.out.println("Recorrido InOrden : " + inOrder);
        return  "Recorrido InOrden : " + inOrder;
    }
    private String recorridoInOrder(Nodo root) {
        if(root == null) {
            return "";
        }
        String left = recorridoPostOrder(root.getIzq());
        String right = recorridoPostOrder(root.getDer());
        StringBuilder sb = new StringBuilder();

        if (!left.isEmpty()) {
            if(sb.length() > 0) {
                sb.append(",");
            }
            sb.append(left);
        }
        if(sb.length() > 0) {
            sb.append(",");
        }
        sb.append(root.getValor());
        if (!right.isEmpty()) {
            if(sb.length() > 0) {
                sb.append(",");
            }
            sb.append(right);
        }

        return sb.toString();
    }

    //todo:crear un archivo con los valores del recorrido PreOrder
    void createFile() {
        Path ruta = Paths.get("ArbolBinario.txt");
        try {
            String message = recorridoPreOrder(raiz);
            Files.writeString(ruta, message);
            System.out.println("archivo creado "+ ruta.getFileName());
        }catch (IOException e) {
            System.out.println("ocurrio un error");
            e.printStackTrace();
        }
    }

    public void printAlt() {
        System.out.println("Altura: " + alt);
    }

    private boolean validRoot(Nodo root){
        return root != null;
    }

    public Nodo getRoot() {
        if(raiz == null) return null;
        return raiz;
    }
}