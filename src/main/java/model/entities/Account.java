package model.entities;

public class Account {
    private String nameTitle;
    private String login;
    private String password;


    public Account(String nameTitle, String login, String password) {
        this.nameTitle = nameTitle;
        this.login = login;
        this.password = password;
    }

    public Account(String nameTitle, String password) {
        this.nameTitle = nameTitle;
        this.password = password;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "nameTitle='" + nameTitle + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
