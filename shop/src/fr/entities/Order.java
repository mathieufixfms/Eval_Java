package fr.entities;

public class Order {
    private final long id;
    private UserConnected userConnected;
    private Formation formation;
    private int quantity;

    public Order(long id, UserConnected userConnected  , Formation formation, int quantity) {
        this.id = id;
        this.userConnected = userConnected                              ;
        this.formation = formation;
        this.quantity = quantity;
    }

    public long getId() { return id; }
    public UserConnected getUserConnected() { return userConnected; }
    public Formation getFormation() { return formation; }
    public int getQuantity() { return quantity; }

    public void setUserConnected(UserConnected userConnected) { this.userConnected = userConnected; }
    public void setFormation(Formation formation) { this.formation = formation; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return id + " - " + userConnected.getLogin() + " : " + formation.getName() + " (" + quantity + ")";
    }


}
