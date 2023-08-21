module first.sqlapp.movietipsql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens first.sqlapp.movietipsql to javafx.fxml;
    exports first.sqlapp.movietipsql;
}