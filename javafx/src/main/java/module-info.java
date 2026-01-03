module pt.ul.fc.css.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310; // <-- para LocalDateTime
    requires java.net.http;

    opens pt.ul.fc.css.javafx to javafx.fxml, javafx.web;
    opens pt.ul.fc.css.javafx.controller to javafx.fxml;
    opens pt.ul.fc.css.javafx.dto to com.fasterxml.jackson.databind; // <-- necessário para Jackson

    exports pt.ul.fc.css.javafx;
    exports pt.ul.fc.css.javafx.dto;
}
