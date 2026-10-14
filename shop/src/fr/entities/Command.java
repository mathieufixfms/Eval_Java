package fr.entities;

import java.util.Date;

public class Command {
    private final String id;
    private Date date_command;
    
    public Command(String id, Date date_command) {
        this.id = id;
        this.date_command = date_command;
    }

    public String getId() { return id; }
    public Date getDate_command() { return date_command; }

    public void setDate_command(Date date_command) { this.date_command = date_command; }

    @Override
    public String toString() {
        return id + " - " + date_command;
    }
}
