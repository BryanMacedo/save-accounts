package guiClasses.Controllers.MainViewDir;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.entities.Account;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private Accordion accordionMain;

    @FXML
    private VBox vbAccordion;

    @FXML
    private Button btAdd;

    @FXML
    private void onBtAddClick(){
        //abrir a addView
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource
                    ("/org/bryanmacedo/saveaccounts/gui/MainViewDir/AddView.fxml"));
            Parent root = loader.load();
            Scene scene = btAdd.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBtClick(Label lb){
        copyText(lb);
    }


    private void copyText(Label lb) {
        String txtLogin = lb.getText();

        ClipboardContent content = new ClipboardContent();
        content.putString(txtLogin);

        Clipboard.getSystemClipboard().setContent(content);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // quando tiver salvando os dados no db criar os objs com eles.
        Account account01 = new Account("Netlix", "Bryansm12", "123456");
        Account account02 = new Account("HBO", "HBOBryansm12", "HBO123456");
        Account account03 = new Account("Valorant", "vlrBryansm12", "vlr123456");

        List<Account> accountList = new ArrayList<>(Arrays.asList(account01, account02, account03));

        for (Account account : accountList) {
            TitledPane newTitledPane = new TitledPane();

            Label labelLogintxt = new Label();
            Label labelPasswordtxt = new Label();

            Label labelLoginContent = new Label();
            Label labelPasswordContent = new Label();

            VBox newVbox = new VBox();

            Button bt01 = new Button();
            Button bt02 = new Button();

            bt01.setOnAction(e -> onBtClick(labelLoginContent));
            bt02.setOnAction(e -> onBtClick(labelPasswordContent));

            HBox hBox01 = new HBox();
            HBox hBox02 = new HBox();

            labelLogintxt.setText("Login: ");
            labelPasswordtxt.setText("Senha: ");

            labelLoginContent.setText(account.getLogin());
            labelPasswordContent.setText(account.getPassword());

            bt01.setText("copiar");
            bt02.setText("copiar");

            hBox01.getChildren().addAll(labelLogintxt, labelLoginContent, bt01);
            hBox02.getChildren().addAll(labelPasswordtxt, labelPasswordContent, bt02);

            newVbox.getChildren().addAll(hBox01, hBox02);

            newTitledPane.setText(account.getNameTitle());
            newTitledPane.setContent(newVbox);

            accordionMain.getPanes().add(newTitledPane);
        }

        boolean isEmpty = accordionMain.getPanes().isEmpty();

        Label lbWarning = new Label();
        lbWarning.setText("Sem dados.");

        if (isEmpty){
            vbAccordion.setAlignment(Pos.CENTER);
            vbAccordion.getChildren().add(lbWarning);
        }


    }
}