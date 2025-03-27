package org.example.arbolbinarioiu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.arbolbinarioiu.model.ArbolBinario;
import org.example.arbolbinarioiu.model.Nodo;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class HelloController {
    @FXML private TextField txtNumber;
    @FXML private TextArea console;
    @FXML private StackPane canvasContainer;
    private FxViewPanel panel;
    private ArbolBinario ab = new ArbolBinario();

    @FXML
    private Stage primaryStage; // Agregar referencia al stage principal

    // Método para inyectar el Stage principal
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void onLoadFile() {
        if (primaryStage == null) {
            console.appendText("Error: No se ha establecido el Stage principal\n");
            return;
        }

        File selectedFile = showFileChooser(primaryStage);
        if (selectedFile != null) {
            try {
                List<String> lines = Files.readAllLines(selectedFile.toPath());
                processFileContent(lines);
                console.appendText("Árbol cargado desde: " + selectedFile.getName() + "\n");
                updatedTree();
            } catch (IOException e) {
                console.appendText("Error al leer el archivo: " + e.getMessage() + "\n");
            } catch (NumberFormatException e) {
                console.appendText("Error: El archivo contiene valores no numéricos\n");
            }
        }
    }

    private void processFileContent(List<String> lines) throws NumberFormatException {
        ab = new ArbolBinario(); // Reiniciamos el árbol

        for (String line : lines) {
            String[] numbers = line.split(","); // Separar por espacios
            for (String numStr : numbers) {
                if (!numStr.isEmpty()) {
                    int num = Integer.parseInt(numStr.trim());
                    ab.insert(num);
                }
            }
        }
    }

    private File showFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo con datos del árbol");

        // Configurar directorio inicial (opcional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Agregar filtros de extensión
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        return fileChooser.showOpenDialog(stage);
    }

    @FXML
    protected void onSearchNodoClicked(){
        String value = txtNumber.getText();
        txtNumber.clear();
        try {
            int num = Integer.parseInt(value.trim());
            String result = ab.search(num);
            console.appendText(result + "\n");
        } catch (NumberFormatException e) {
            console.appendText("Error: Dato no valido\n");
        }
    }

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