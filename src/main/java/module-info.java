module dylanJaxon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    opens dylanJaxon to javafx.fxml;
    exports dylanJaxon;
}