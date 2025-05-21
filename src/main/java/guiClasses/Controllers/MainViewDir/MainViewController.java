package guiClasses.Controllers.MainViewDir;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private Button btCopyLogin;

    @FXML
    private Button btCopyPassword;

    @FXML
    private Label lbLogin;

    @FXML
    private Label lbPassword;

    @FXML
    private void onBtCopyLoginOn(){
        copyText(lbLogin);
    }

    @FXML
    private void onBtCopyPasswordOn(){
        copyText(lbPassword);
    }

    private void copyText(Label lb){
        String txtLogin = lb.getText();

        ClipboardContent content = new ClipboardContent();
        content.putString(txtLogin);

        Clipboard.getSystemClipboard().setContent(content);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}