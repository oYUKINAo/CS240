package service;

/**
 * Location class (makes parsing locations.json easier)
 */
public class Location {
    private String country;
    private String city;
    private float latitude;
    private float longitude;

    public Location(String country, String city, float latitude, float longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Location) {
            Location location = (Location) o;
            return location.getCountry().equals(getCountry())
                    && location.getCity().equals(getCity())
                    && location.getLatitude() == getLatitude()
                    && location.getLongitude() == getLongitude();
        }
        else {
            return false;
        }
    }
}


