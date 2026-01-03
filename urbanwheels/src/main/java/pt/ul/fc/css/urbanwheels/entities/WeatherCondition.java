package pt.ul.fc.css.urbanwheels.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "weather_conditions")
public class WeatherCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String condition;

    public WeatherCondition() {}

    public WeatherCondition(String condition) {
        this.condition = condition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
