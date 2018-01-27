package siliconwally.net.wallyapp.model;

import org.json.JSONObject;

/**
 * Created by jhamil on 23-01-18.
 */

public class Login {
    JSONObject currentUser;
    String csrfToken;
    String logoutToken;

    public Login() {}

    public JSONObject getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(JSONObject currentUser) {
        this.currentUser = currentUser;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getLogoutToken() {
        return logoutToken;
    }

    public void setLogoutToken(String logoutToken) {
        this.logoutToken = logoutToken;
    }
}
