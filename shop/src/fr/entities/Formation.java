package fr.entities;

public class Formation {
    private final long id;
    private String name;
    private String description;
    private int duration;
    private boolean type;
    private double price;

    public Formation(long id, String name, String description, int duration, boolean type, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.type = type;
        this.price = price;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public boolean isType() { return type; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setType(boolean type) { this.type = type; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return id + " - " + name + " : " + description + " (" + duration + " hours, " + price + " EUR)";
    }
}
