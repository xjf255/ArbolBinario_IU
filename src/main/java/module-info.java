module org.example.arbolbinarioiu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires gs.core;
    requires gs.ui.javafx;

    opens org.example.arbolbinarioiu to javafx.fxml;
    exports org.example.arbolbinarioiu;
    exports org.example.arbolbinarioiu.controller;
    opens org.example.arbolbinarioiu.controller to javafx.fxml;
}