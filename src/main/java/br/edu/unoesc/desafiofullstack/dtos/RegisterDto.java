package br.edu.unoesc.desafiofullstack.dtos;

import java.io.Serializable;

public class RegisterDto implements Serializable {

    private String username;
    private String password;
    private String repeatPassword;

    public boolean isPasswordValid() {
        return 6 <= password.length() && password.length() <= 40;
    }

    public boolean arePasswordsEqual() {
        return password.equals(repeatPassword);
    }

    public void clearPasswords() {
        this.password = "";
        this.repeatPassword = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

}
