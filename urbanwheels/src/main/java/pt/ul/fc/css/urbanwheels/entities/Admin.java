package pt.ul.fc.css.urbanwheels.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin")
public class Admin extends User {

    @OneToMany(mappedBy = "admin")
    private List<Maintenance> maintenances = new ArrayList<>();

    public Admin() {
    }
    
    
    public Admin(String name, String email) {
        super(name, email);
    }
    
    // getters + setters
    
    public List<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(List<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }
}
