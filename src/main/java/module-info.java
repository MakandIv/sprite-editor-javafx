module com.example.spriteeditorfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;

    opens com.example.spriteeditorfx to javafx.fxml;
    exports com.example.spriteeditorfx;
    exports com.example.spriteeditorfx.model;
    opens com.example.spriteeditorfx.model to javafx.fxml;
}