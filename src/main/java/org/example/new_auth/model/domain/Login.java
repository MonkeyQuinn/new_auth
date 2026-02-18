package org.example.new_auth.model.domain;

public class Login {

    private Token access;
    private Token refresh;
    private Boolean passChangeNeeded;

    public Login(Token access, Token refresh, Boolean passChangeNeeded) {
        this.access = access;
        this.refresh = refresh;
        this.passChangeNeeded = passChangeNeeded;
    }

    public Token getAccess() {
        return access;
    }

    public void setAccess(Token access) {
        this.access = access;
    }

    public Token getRefresh() {
        return refresh;
    }

    public void setRefresh(Token refresh) {
        this.refresh = refresh;
    }

    public Boolean getPassChangeNeeded() {
        return passChangeNeeded;
    }

    public void setPassChangeNeeded(Boolean passChangeNeeded) {
        this.passChangeNeeded = passChangeNeeded;
    }

}
