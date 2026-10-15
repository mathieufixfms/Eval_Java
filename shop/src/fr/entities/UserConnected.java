package fr.entities;

public class UserConnected {
    private long id;
    private String login;
    private String passeword;

    public UserConnected(long id, String login, String passeword) {
        this.id = id;
        this.login = login;
        this.passeword = passeword;
    }

    public UserConnected() {
        this.id = 0;
        //TODO Auto-generated constructor stub
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

    public void setId(long userId) {
        this.id = userId;
    }

    public char[] getUserConnected() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserConnected'");
    }
 }  
