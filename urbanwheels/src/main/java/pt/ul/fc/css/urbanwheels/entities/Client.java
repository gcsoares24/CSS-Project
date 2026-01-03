package pt.ul.fc.css.urbanwheels.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client extends User {

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_type_id", nullable = false)
    private Subscription subscription;

    @OneToMany(mappedBy = "user")
    private List<Trip> trips = new ArrayList<>();
    

    public Client() {
    }
    
    public Client(String email, String name,  Subscription subscription) {
        super(email, name); 
        this.subscription = subscription;
    }
    
    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}
