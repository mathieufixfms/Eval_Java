package fr.entities;

public class Client {
    private final long id;

    private String name;
    private String surname;
    private String email;
    private String address;
    private String phoneNumber;
    private UserConnected userConnected;


    public Client(long id, String name, String surname, String email, String address, String phoneNumber,
                 UserConnected userConnected) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userConnected = userConnected;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public UserConnected getUserConnected() { return userConnected; }

    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setUserConnected(UserConnected userConnected) { this.userConnected = userConnected; }

    @Override
    public String toString() {
        return id + " - " + name + " " + surname + " : " + email + ", " + address + ", " + phoneNumber + ", "
                + userConnected.getId() + " - " + userConnected.getLogin() + " : " + userConnected.getPasseword();
    }
}
