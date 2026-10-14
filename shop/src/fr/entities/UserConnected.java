package fr.entities;

public class UserConnected {
    private final long id;
    private String login;
    private String passeword;

    public UserConnected(long id, String login, String passeword) {
        this.id = id;
        this.login = login;
        this.passeword = passeword;
    }

    public long getId() { return id; }
    public String getLogin() { return login; }
    public String getPasseword() { return passeword; }

    public void setLogin(String login) { this.login = login; }
    public void setPasseword(String passeword) { this.passeword = passeword;}

    @Override
    public String toString() {
        return id + " - " + login + " : " + passeword;
    }
 }  
