package edu.uw.cse.ifrcdemo.setup.model.login;

import edu.uw.cse.ifrcdemo.setup.ui.common.CloudEndpointAuthFormModel;

import javax.validation.constraints.NotNull;

public class Login implements CloudEndpointAuthFormModel {
    private String serverUrl;
    private String username;
    private String password;
    private String inputDataDirectory;
    @NotNull
    private String type;

    private Login(){
    }

    public static Login createLogin(){
        return new Login();
    }
    @Override
    public String getServerUrl() {
        return serverUrl;
    }

    @Override
    public void setServerUrl(String serverUrl) {
        if (serverUrl != null) { this.serverUrl = serverUrl.trim(); }
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        if (username != null) { this.username = username.trim(); }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        if (password != null) { this.password = password.trim(); }
    }

    public String getInputDataDirectory() {
        return inputDataDirectory;
    }

    public void setInputDataDirectory(String inputDataDirectory) {
        this.inputDataDirectory = inputDataDirectory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "LoginFormModel{" +
                "serverUrl='" + serverUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", inputDataDirectory='" + inputDataDirectory + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
