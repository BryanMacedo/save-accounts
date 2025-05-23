package guiClasses.Controllers.MainViewDir;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.entities.Account;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    private int aux = 0;
    private HBox hBoxTite = new HBox();
    private HBox hBoxLogin = new HBox();
    private HBox hBoxPassword = new HBox();
    private Button btAddAccount;
    @FXML
    private Accordion accordionMain;

    @FXML
    private VBox vbTop;

    @FXML
    private VBox vbAccordion;

    @FXML
    private Button btAdd;


    @FXML
    private void onBtAddClick() {
        if (aux == 0) {
            Label lbTitleText = new Label("Titulo ");
            TextField tfTitle = new TextField();
            tfTitle.setPromptText("ex: Netflix");

            Label lbLoginText = new Label("Login ");
            TextField tfLogin = new TextField();
            tfLogin.setPromptText("ex: seuNome07");

            Label lbPasswordText = new Label("Senha ");
            TextField tfPassword = new TextField();
            tfPassword.setPromptText("ex: 132mdsj2@");

            hBoxTite.getChildren().setAll(lbTitleText, tfTitle);
            hBoxLogin.getChildren().setAll(lbLoginText, tfLogin);
            hBoxPassword.getChildren().setAll(lbPasswordText, tfPassword);


            if (btAddAccount == null) {
                btAddAccount = new Button();
                btAddAccount.setText("Adicionar conta");
            }

            vbTop.setAlignment(Pos.CENTER);


            vbTop.getChildren().addAll(hBoxTite, hBoxLogin, hBoxPassword, btAddAccount);

            btAddAccount.setOnAction(e -> {
                Account ac = new Account(tfTitle.getText(), tfLogin.getText(), tfPassword.getText());
                onBtAddAccountClick(ac);
            });

            aux = 1;
        } else {
            vbTop.getChildren().removeAll(hBoxTite, hBoxLogin, hBoxPassword, btAddAccount);
            aux = 0;
        }


    }

    private void onBtAddAccountClick(Account ac) {
        System.out.println(ac);
        // adicionar ao db
    }

    @FXML
    private void onBtClick(Label lb) {
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

        if (isEmpty) {
            vbAccordion.setAlignment(Pos.CENTER);
            vbAccordion.getChildren().add(lbWarning);
        }
    }
}