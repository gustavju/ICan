public class GlassCan extends Trashcan {

    public GlassCan(Location location) {
        super(location, false);
    }

    @Override
    public void specificCalc() {
    }

    @Override
    public String toString() {
        return "{" +
                "location=" + location +
                ", trashcanId='" + trashcanId + '\'' +
                '}';
    }
}
