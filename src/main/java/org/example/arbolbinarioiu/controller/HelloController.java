package org.example.arbolbinarioiu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.example.arbolbinarioiu.model.ArbolBinario;
import org.example.arbolbinarioiu.model.Nodo;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

public class HelloController {
    @FXML private TextField txtNumber;
    @FXML private TextArea console;
    @FXML private StackPane canvasContainer;
    private FxViewPanel panel;
    private ArbolBinario ab = new ArbolBinario();

    @FXML
    protected void onAddNodoButtonClick() {
        String value = txtNumber.getText();
        txtNumber.clear();
        try {
            int num = Integer.parseInt(value.trim());
            if (ab.insert(num)) {
                console.appendText("Nodo agregado: " + num + "\n");
                updatedTree();
            } else {
                console.appendText("¡El nodo " + num + " ya existe!\n");
            }
        } catch (NumberFormatException e) {
            console.appendText("Error: Ingrese un número válido.\n");
        }
    }

    @FXML
    protected void onRemoveNodoButtonClick() {
        String value = txtNumber.getText();
        txtNumber.clear();
        try {
            int num = Integer.parseInt(value.trim());
            ab.remove(num);
            console.appendText("Nodo eliminado: " + num + "\n");
            updatedTree();
        } catch (NumberFormatException e) {
            console.appendText("Error: Ingrese un número válido.\n");
        }
    }

    @FXML
    protected void onGetOrderButtonClick() {
        console.appendText(ab.recorridoPreOrder() + "\n");
        console.appendText(ab.recorridoInOrder() + "\n");
        console.appendText(ab.recorridoPostOrder() + "\n");
    }

    private void updatedTree() {
        if (panel != null) {
            canvasContainer.getChildren().remove(panel);
        }
        Graph graph = new SingleGraph("Árbol Binario");
        construirGrafoDesdeArbol(graph, ab);
        graph.setAttribute("ui.stylesheet",
                "node { fill-color: #FFA07A; size: 30px; text-size: 14; }" +
                        "edge { fill-color: #4682B4; size: 3px; }");
        FxViewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        panel = (FxViewPanel) viewer.addDefaultView(false, new FxGraphRenderer());
        canvasContainer.getChildren().add(panel);
    }

    private void construirGrafoDesdeArbol(Graph graph, ArbolBinario arbol) {
        if (arbol.getRoot() != null) {
            agregarNodoYHijos(graph, arbol.getRoot());
        }
    }

    private void agregarNodoYHijos(Graph graph, Nodo nodo) {
        if (nodo == null) return;

        // First ensure the current node exists
        String id = String.valueOf(nodo.getValor());
        if (graph.getNode(id) == null) {
            graph.addNode(id).setAttribute("ui.label", id);
        }

        // Then handle left child
        if (nodo.getIzq() != null) {
            String idIzq = String.valueOf(nodo.getIzq().getValor());
            // Ensure left child exists before creating edge
            if (graph.getNode(idIzq) == null) {
                graph.addNode(idIzq).setAttribute("ui.label", idIzq);
            }
            graph.addEdge(id + "-" + idIzq, id, idIzq, true);
            agregarNodoYHijos(graph, nodo.getIzq());
        }

        // Then handle right child
        if (nodo.getDer() != null) {
            String idDer = String.valueOf(nodo.getDer().getValor());
            // Ensure right child exists before creating edge
            if (graph.getNode(idDer) == null) {
                graph.addNode(idDer).setAttribute("ui.label", idDer);
            }
            graph.addEdge(id + "-" + idDer, id, idDer, true);
            agregarNodoYHijos(graph, nodo.getDer());
        }
    }
}