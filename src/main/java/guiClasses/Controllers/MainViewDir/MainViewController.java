package guiClasses.Controllers.MainViewDir;

import DB.DB_connection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.entities.Account;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MainViewController implements Initializable {
    private int auxAdd = 0;
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

    private void reloadView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource
                    ("/org/bryanmacedo/saveaccounts/gui/MainViewDir/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = accordionMain.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBtAddClick() {
        Dialog<Account> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Conta");

        ButtonType btAdd = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btAdd, ButtonType.CANCEL);

        TextField tfTitle = new TextField();
        tfTitle.setPromptText("Título");

        TextField tfLogin = new TextField();
        tfLogin.setPromptText("Login");

        TextField tfPassword = new TextField();
        tfPassword.setPromptText("Senha");

        VBox content = new VBox(10);

        HBox hBoxTitle = new HBox(10);
        HBox hBoxLogin = new HBox(10);
        HBox hBoxPassword = new HBox(10);

        hBoxTitle.getChildren().addAll(new Label("Título:"), tfTitle);
        hBoxLogin.getChildren().addAll(new Label("Login:"), tfLogin);
        hBoxPassword.getChildren().addAll(new Label("Senha:"), tfPassword);

        for (HBox hBox : Arrays.asList(hBoxTitle, hBoxLogin, hBoxPassword)) {
            hBox.setAlignment(Pos.CENTER);
        }

        content.getChildren().addAll(hBoxTitle, hBoxLogin, hBoxPassword);
        dialog.getDialogPane().setContent(content);

        // Pega o botão Adicionar e impede o fechamento se a validação falhar
        Node btAddNode = dialog.getDialogPane().lookupButton(btAdd);
        btAddNode.addEventFilter(ActionEvent.ACTION, event -> {
            String title = tfTitle.getText();
            String login = tfLogin.getText();
            String password = tfPassword.getText();

            if (title.isEmpty() || login.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Preencha todos os campos.");
                event.consume(); // impede o fechamento
                return;
            }

            if (isTitleAlreadyUsed(title)) {
                showAlert(Alert.AlertType.ERROR, "Título já utilizado", "Já existe uma conta com este título.");
                event.consume(); // impede o fechamento
            } else {
                Account ac = new Account(title, login, password);
                onBtAddAccountClick(ac);
            }
        });

        dialog.showAndWait();
    }

    private boolean isTitleAlreadyUsed(String title) {
        try (Connection conn = DB_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Accounts WHERE NameTitle = ?")) {
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void onBtDeleteAccountClick(Account ac) {
        // depois adicionar um aviso ao antes de deletar.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Excluir conta");
        alert.setHeaderText("Confirmar Exclusão");
        alert.setContentText("Tem certeza que deseja excluir a conta \"" + ac.getNameTitle() + "\"?");

        ButtonType btYes = new ButtonType("Sim");
        ButtonType btNo = new ButtonType("Não");
        alert.getButtonTypes().setAll(btYes, btNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == btYes) {
            System.out.println(ac);

            Connection conn = null;
            PreparedStatement ps = null;
            int rowsAffected = 0;

            try {
                conn = DB_connection.getConnection();
                String sql = "DELETE FROM Accounts WHERE NameTitle = ?";
                ps = conn.prepareStatement(sql);

                ps.setString(1, ac.getNameTitle());

                rowsAffected = ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (rowsAffected > 0) reloadView();
        }
    }

    private void onBtEditAccountClick(Account oldAc, Account newAc) {
        System.out.println(oldAc);
        System.out.println(newAc);

        Connection conn = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try {
            conn = DB_connection.getConnection();
            String sql = "UPDATE Accounts SET NameTitle = ?, Login = ?, Password = ? WHERE NameTitle = ? AND Login = ? AND Password = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, newAc.getNameTitle());
            ps.setString(2, newAc.getLogin());
            ps.setString(3, newAc.getPassword());

            ps.setString(4, oldAc.getNameTitle());
            ps.setString(5, oldAc.getLogin());
            ps.setString(6, oldAc.getPassword());

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (rowsAffected > 0) reloadView();

    }

    private void onBtAddAccountClick(Account ac) {
        System.out.println(ac);

        Connection conn = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try {
            conn = DB_connection.getConnection();
            String sqlInset = "INSERT INTO Accounts (NameTitle, Login, Password) VALUES (?, ?, ?)";

            ps = conn.prepareStatement(sqlInset);
            ps.setString(1, ac.getNameTitle());
            ps.setString(2, ac.getLogin());
            ps.setString(3, ac.getPassword());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (rowsAffected > 0) reloadView();
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

        Collections.sort(accountList, Comparator.comparing(a -> a.getNameTitle().toLowerCase()));

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
            hBoxTitleArea.setAlignment(Pos.CENTER);
            Label lbNameTile = new Label(account.getNameTitle());
            Button btEdit = new Button("Editar");
            Button btDelete = new Button("Deletar");

            btEdit.setOnAction(e -> {
                System.out.println(account);
                //editat o account no db

                Dialog<Account> dialog = new Dialog<>();
                dialog.setTitle("Editar Conta");

                // tentar trocar esta parte por um hbox com um button
                ButtonType btAdd = new ButtonType("Editar", ButtonBar.ButtonData.OK_DONE);

                dialog.getDialogPane().getButtonTypes().addAll(btAdd);

                // Campos do formulário
                TextField tfTitle = new TextField();
                tfTitle.setPromptText("Título");
                tfTitle.setText(account.getNameTitle());

                TextField tfLogin = new TextField();
                tfLogin.setPromptText("Login");
                tfLogin.setText(account.getLogin());

                TextField tfPassword = new TextField();
                tfPassword.setPromptText("Senha");
                tfPassword.setText(account.getPassword());

                VBox content = new VBox(10);

                HBox hBoxTitle = new HBox(10);
                HBox hBoxLogin = new HBox(10);
                HBox hBoxPassword = new HBox(10);

                hBoxTitle.getChildren().addAll(new Label("Título:"), tfTitle);
                hBoxLogin.getChildren().addAll(new Label("Login:"), tfLogin);
                hBoxPassword.getChildren().addAll(new Label("Senha:"), tfPassword);

                List<HBox> hBoxList = new ArrayList<>(Arrays.asList(hBoxTitle, hBoxLogin, hBoxPassword));

                for (HBox hBox : hBoxList) {
                    hBox.setAlignment(Pos.CENTER);
                }

                content.getChildren().addAll(
                        hBoxTitle,
                        hBoxLogin,
                        hBoxPassword
                );

                dialog.getDialogPane().setContent(content);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == btAdd) {
                        if (tfTitle.getText().isEmpty() || tfLogin.getText().isEmpty() || tfPassword.getText().isEmpty())
                            return null;
                        Account newAc = new Account(tfTitle.getText(), tfLogin.getText(), tfPassword.getText());
                        //onBtAddAccountClick(ac);
                        onBtEditAccountClick(account, newAc);
                    }
                    return null;
                });

                dialog.showAndWait();
            });

            btDelete.setOnAction(e -> {
                System.out.println(account);
                onBtDeleteAccountClick(account);
            });

            Region spacer = new Region();
            spacer.setPrefWidth(20);

            hBoxTitleArea.getChildren().addAll(lbNameTile, spacer, btEdit, btDelete);

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