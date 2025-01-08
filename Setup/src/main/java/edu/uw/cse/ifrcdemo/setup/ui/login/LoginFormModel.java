package edu.uw.cse.ifrcdemo.setup.ui.login;

import edu.uw.cse.ifrcdemo.setup.ui.common.CloudEndpointAuthFormModel;
import org.springframework.context.annotation.Conditional;

import javax.validation.constraints.NotNull;


public class LoginFormModel {
    private String serverUrl;
    private String username;
    private String password;

    public LoginFormModel(){
    }

    public static LoginFormModel createLogin(){
        return new LoginFormModel();
    }
    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        if (serverUrl != null) { this.serverUrl = serverUrl.trim(); }
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        if (username != null) { this.username = username.trim(); }
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        if (password != null) { this.password = password.trim(); }
    }



    @Override
    public String toString() {
        return "LoginFormModel{" +
                "serverUrl='" + serverUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
