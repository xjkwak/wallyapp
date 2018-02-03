package siliconwally.net.wallyapp.model;

import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by jhamil on 23-01-18.
 */

public class Login {
    LinkedHashMap current_user;
    String csrf_token;
    String logout_token;

    public Login() {}

    public LinkedHashMap getCurrentUser() {
        return current_user;
    }

    public void setCurrentUser(LinkedHashMap currentUser) {
        this.current_user = currentUser;
    }

    public String getCsrfToken() {
        return csrf_token;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrf_token = csrfToken;
    }

    public String getLogoutToken() {
        return logout_token;
    }

    public void setLogoutToken(String logoutToken) {
        this.logout_token = logoutToken;
    }
}
