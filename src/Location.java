public class Location {

    private double longitude;
    private double latitude;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void updateLocation(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
