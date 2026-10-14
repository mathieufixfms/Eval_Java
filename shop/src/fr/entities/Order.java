package fr.entities;

public class Order {
    private Command command;
    private UserConnected userConnected;
    private Formation formation;
    private int quantity;

    public Order(Command command, UserConnected userConnected, Formation formation, int quantity) {
        this.command = command;
        this.userConnected = userConnected;
        this.formation = formation;
        this.quantity = quantity;
    }

    public Command getCommand() { return command; }
    public UserConnected getUserConnected() { return userConnected; }
    public Formation getFormation() { return formation; }
    public int getQuantity() { return quantity; }

    public void setCommand(Command command) { this.command = command; }
    public void setUserConnected(UserConnected userConnected) { this.userConnected = userConnected; }
    public void setFormation(Formation formation) { this.formation = formation; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return command.getId() + " - " + userConnected.getLogin() + " : "
            + formation.getName() + " (" + quantity + ")";
    }
}
