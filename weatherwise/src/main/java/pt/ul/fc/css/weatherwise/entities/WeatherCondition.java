package pt.ul.fc.css.weatherwise.entities;

public enum WeatherCondition {
    SUN(1, "Sun"),
    RAIN(2, "Rain"),
    THUNDERSTORM(3, "Thunderstorm"),
    CLOUDY(4, "Cloudy"),
    SNOW(5, "Snow"),
    WINDY(6, "Windy");

    private final int id;
    private final String description;

    WeatherCondition(int id, String descricao) {
        this.id = id;
        this.description = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        // Retorns the name only.
        return description;
    }

    // Opcional: buscar por ID
    public static WeatherCondition fromId(int id) {
        for (WeatherCondition condition : values()) {
            if (condition.id == id) {
                return condition;
            }
        }
        throw new IllegalArgumentException("invalid ID: " + id);
    }
}
