package guiClasses.Controllers.MainViewDir;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddViewController implements Initializable {
    @FXML
    private Button btBack;

    @FXML
    private void onBtBackClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource
                    ("/org/bryanmacedo/saveaccounts/gui/MainViewDir/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = btBack.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
