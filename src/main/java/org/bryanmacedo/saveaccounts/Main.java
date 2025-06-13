package org.bryanmacedo.saveaccounts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader
                (Main.class.getResource("/org/bryanmacedo/saveaccounts/gui/MainViewDir/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SAVE ACCOUNTS");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Image icon = new Image(getClass().getResourceAsStream("/imgs/ic_app.png"));
        stage.getIcons().add(icon);
    }

    public static void main(String[] args) {
        launch();
    }
}