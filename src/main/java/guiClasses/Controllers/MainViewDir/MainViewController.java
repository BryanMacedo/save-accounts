package guiClasses.Controllers.MainViewDir;

import DB.DB_connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.entities.Account;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private Button btEdit;


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
                // limitar o tamanho do titulo depois
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

        Connection conn = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try {
            conn = DB_connection.getConnection();
            String sql = "INSERT INTO Accounts (NameTitle, Login, Password) VALUES (?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, ac.getNameTitle());
            ps.setString(2, ac.getLogin());
            ps.setString(3, ac.getPassword());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (rowsAffected > 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource
                        ("/org/bryanmacedo/saveaccounts/gui/MainViewDir/MainView.fxml"));
                Parent root = loader.load();
                Scene scene = btAdd.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        List<Account> accountList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DB_connection.getConnection();
            st = conn.prepareStatement("SELECT * FROM Accounts");
            rs = st.executeQuery();

            while (rs.next()) {
                Account ac = new Account
                        (rs.getString("NameTitle"), rs.getString("Login"), rs.getString("Password"));
                accountList.add(ac);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


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

            HBox hBoxTitleArea = new HBox();
            Label lbNameTile = new Label(account.getNameTitle());
            Button btEdit = new Button("Editar");

            Region spacer = new Region();
            spacer.setPrefWidth(20);

            hBoxTitleArea.getChildren().addAll(lbNameTile, spacer, btEdit);

            newTitledPane.setGraphic(hBoxTitleArea);
            labelLoginContent.setText(account.getLogin());
            labelPasswordContent.setText(account.getPassword());

            Image copyIcon = new Image(getClass().getResourceAsStream("/imgs/ic_copy.png"));


            ImageView copyIconView01 = new ImageView(copyIcon);
            copyIconView01.setFitWidth(20);
            copyIconView01.setFitHeight(20);
            copyIconView01.setPreserveRatio(true);

            ImageView copyIconView02 = new ImageView(copyIcon); // Nova instância
            copyIconView02.setFitWidth(20);
            copyIconView02.setFitHeight(20);
            copyIconView02.setPreserveRatio(true);

            bt01.setGraphic(copyIconView01);
            bt02.setGraphic(copyIconView02);

            hBox01.getChildren().addAll(labelLogintxt, labelLoginContent, bt01);
            hBox02.getChildren().addAll(labelPasswordtxt, labelPasswordContent, bt02);

            newVbox.getChildren().addAll(hBox01, hBox02);

            //newTitledPane.setText(account.getNameTitle());
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