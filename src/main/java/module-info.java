module org.bryanmacedo.saveaccounts {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.bryanmacedo.saveaccounts to javafx.fxml;
    exports org.bryanmacedo.saveaccounts;
    exports guiClasses.Controllers.MainViewDir;
    opens guiClasses.Controllers.MainViewDir to javafx.fxml;
}